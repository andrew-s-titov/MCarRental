package com.mcarrental.carsearchservice.service.impl;

import com.mcarrental.carsearchservice.dto.CarSearchRequestDTO;
import com.mcarrental.carsearchservice.service.SearchRequestToQueryConverter;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class SearchRequestToQueryConverterImpl implements SearchRequestToQueryConverter {

    private static final String BRAND_FIELD = "brand";
    private static final String TYPE_FIELD = "type";
    private static final String FUEL_FIELD = "fuel";
    private static final String LAYOUT_FIELD = "layout";
    private static final String GEARBOX_FIELD = "gearBox";
    private static final String PRODUCTION_YEAR_FIELD = "productionYear";
    private static final String NUMBER_OF_SEATS_FIELD = "numberOfSeats";
    private static final String PRICE_FIELD = "pricePerDay";
    private static final String BOOKINGS_FIELDS = "bookings";
    private static final String START_FIELD = BOOKINGS_FIELDS + "." + "start";
    private static final String END_FIELD = BOOKINGS_FIELDS + "." + "end";

    @NonNull
    @Override
    public QueryBuilder convert(@NonNull CarSearchRequestDTO searchRequest) {
        Assert.notNull(searchRequest, "search request cannot be null");

        BoolQueryBuilder query = new BoolQueryBuilder();

        addMustTermsInQuery(query, BRAND_FIELD, searchRequest.getBrands());
        addMustTermsInQuery(query, TYPE_FIELD, searchRequest.getTypes());
        addMustTermsInQuery(query, FUEL_FIELD, searchRequest.getFuelTypes());
        addMustTermsInQuery(query, LAYOUT_FIELD, searchRequest.getLayouts());
        addMustTermsInQuery(query, GEARBOX_FIELD, searchRequest.getGearBoxTypes());

        addProdYearQuery(query, searchRequest.getProductionYear());
        addNumberOfSeatsQuery(query, searchRequest.getNumberOfSeats());
        addPriceQuery(query, searchRequest.getPricePerDay());

        addDateRangeQuery(query, searchRequest);

        return computeFinalQuery(query);
    }

    private void addMustTermsInQuery(BoolQueryBuilder queryBuilder, String field, Collection<?> values) {
        Assert.notNull(field, "Cannot add query for 'null' field");
        if (!CollectionUtils.isEmpty(values)) {
            queryBuilder.must(QueryBuilders.termsQuery(field, normalizeValues(values)));
        }
    }

    private void addProdYearQuery(BoolQueryBuilder queryBuilder, Year min) {
        if (min != null) {
            queryBuilder.must(QueryBuilders.rangeQuery(PRODUCTION_YEAR_FIELD).gte(min));
        }
    }

    private void addNumberOfSeatsQuery(BoolQueryBuilder queryBuilder, Integer seats) {
        if (seats != null) {
            queryBuilder.must(QueryBuilders.rangeQuery(NUMBER_OF_SEATS_FIELD).gte(seats));
        }
    }

    private void addPriceQuery(BoolQueryBuilder queryBuilder, Integer price) {
        if (price != null) {
            queryBuilder.must(QueryBuilders.rangeQuery(PRICE_FIELD).lte(price));
        }
    }

    private void addDateRangeQuery(BoolQueryBuilder queryBuilder, CarSearchRequestDTO searchRequest) {
        LocalDateTime startDate = searchRequest.getStart();
        LocalDateTime endDate = searchRequest.getEnd();
        if (startDate != null && endDate != null) {
            var dateFormatter = DateTimeFormatter.ofPattern(DateFormat.date_hour_minute.getPattern());
            var formattedStartDate = searchRequest.getStart().format(dateFormatter);
            var formattedEndDate = searchRequest.getEnd().format(dateFormatter);
            var startDateRangeQuery = QueryBuilders.rangeQuery(START_FIELD).lt(formattedEndDate);
            var endDateRangeQuery = QueryBuilders.rangeQuery(END_FIELD).gt(formattedStartDate);
            var nestedBookingBoolQuery = QueryBuilders.boolQuery().must(startDateRangeQuery).must(endDateRangeQuery);
            queryBuilder.mustNot(QueryBuilders.nestedQuery(BOOKINGS_FIELDS, nestedBookingBoolQuery, ScoreMode.Avg));
        }
    }

    private Collection<Object> normalizeValues(Collection<?> values) {
        Function<Object, Object> normalizer = e -> {
            if (e instanceof Enum<?>) {
                return ((Enum<?>) e).name();
            } else {
                return e;
            }
        };
        return values.stream().map(normalizer).collect(Collectors.toList());
    }

    private QueryBuilder computeFinalQuery(BoolQueryBuilder result) {
        return result.hasClauses() ? result : QueryBuilders.matchAllQuery();
    }
}
