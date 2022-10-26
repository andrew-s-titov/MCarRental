package com.mcarrental.carsearchservice.service.impl;

import com.mcarrental.carsearchservice.converter.CarConverter;
import com.mcarrental.carsearchservice.dto.CarSearchRequestDTO;
import com.mcarrental.carsearchservice.dto.CarViewDTO;
import com.mcarrental.carsearchservice.repository.elastic.CarRepository;
import com.mcarrental.carsearchservice.service.CarSearchRequestToQueryBuilderAdapter;
import com.mcarrental.carsearchservice.service.CarSearchService;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ElasticCarSearchServiceImpl implements CarSearchService {

    private final CarRepository carRepository;
    private final CarConverter converter;
    private final CarSearchRequestToQueryBuilderAdapter adapter;

    @Override
    public Page<CarViewDTO> searchCars(CarSearchRequestDTO carSearchRequest, Pageable pageable) {
        QueryBuilder query = adapter.convert(carSearchRequest);
        return carRepository.search(query, pageable).map(converter::toView);
    }
}