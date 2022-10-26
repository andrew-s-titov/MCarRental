package com.mcarrental.carsearchservice.repository.elastic;

import com.mcarrental.carsearchservice.model.Car;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHitSupport;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.SearchPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@RequiredArgsConstructor
@Component
public class CustomElasticsearchRepoImpl implements CustomElasticsearchRepo<Car> {

    private final ElasticsearchRestTemplate elastic;

    @Override
    public Page<Car> search(@NonNull QueryBuilder queryBuilder, @NonNull Pageable pageable) {

        Assert.notNull(queryBuilder, "Cannot search with 'null' queryBuilder");
        Assert.notNull(pageable, "'pageable' cannot be 'null'");

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .withPageable(pageable)
                .build();
        SearchHits<Car> searchHits = elastic.search(searchQuery, Car.class);

        SearchPage<Car> searchPage = SearchHitSupport.searchPageFor(searchHits, pageable);
        @SuppressWarnings("unchecked")
        Page<Car> page = (Page<Car>) SearchHitSupport.unwrapSearchHits(searchPage);
        return page;
    }
}