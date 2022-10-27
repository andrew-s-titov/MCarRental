package com.mcarrental.carsearchservice.repository.elastic;

import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;

public interface CustomElasticsearchRepo<T> {

    @NonNull
    Page<T> search(@NonNull QueryBuilder queryBuilder, @NonNull Pageable pageable);
}