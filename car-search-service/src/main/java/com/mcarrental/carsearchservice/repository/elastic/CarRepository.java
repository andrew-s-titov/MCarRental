package com.mcarrental.carsearchservice.repository.elastic;

import com.mcarrental.carsearchservice.model.Car;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.UUID;

public interface CarRepository extends ElasticsearchRepository<Car, UUID>, CustomElasticsearchRepo<Car> {
}