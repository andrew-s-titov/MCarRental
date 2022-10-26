package com.mcarrental.carsearchservice.service;

import com.mcarrental.carsearchservice.dto.CarSearchRequestDTO;
import org.elasticsearch.index.query.QueryBuilder;

public interface CarSearchRequestToQueryBuilderAdapter {

    QueryBuilder convert(CarSearchRequestDTO carSearchRequest);
}
