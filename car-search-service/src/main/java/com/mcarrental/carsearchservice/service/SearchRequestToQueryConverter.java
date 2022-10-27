package com.mcarrental.carsearchservice.service;

import com.mcarrental.carsearchservice.dto.CarSearchRequestDTO;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.lang.NonNull;

public interface SearchRequestToQueryConverter {

    @NonNull
    QueryBuilder convert(@NonNull CarSearchRequestDTO carSearchRequest);
}