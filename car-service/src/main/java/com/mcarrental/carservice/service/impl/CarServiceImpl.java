package com.mcarrental.carservice.service.impl;

import com.mcarrental.carservice.converter.CarConverter;
import com.mcarrental.carservice.dto.ItemsDTO;
import com.mcarrental.carservice.dto.car.CarCreateRequestDTO;
import com.mcarrental.carservice.dto.car.CarUpdateRequestDTO;
import com.mcarrental.carservice.dto.car.CarViewDTO;
import com.mcarrental.carservice.event.CarCreateEvent;
import com.mcarrental.carservice.event.CarPriceUpdateEvent;
import com.mcarrental.carservice.event.CarUpdateEvent;
import com.mcarrental.carservice.exception.BadRequestException;
import com.mcarrental.carservice.exception.ConflictException;
import com.mcarrental.carservice.model.Car;
import com.mcarrental.carservice.repository.CarRepository;
import com.mcarrental.carservice.security.Role;
import com.mcarrental.carservice.security.SecurityInfoManager;
import com.mcarrental.carservice.service.BookingServiceClient;
import com.mcarrental.carservice.service.CarService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.persistence.EntityNotFoundException;
import java.util.Objects;
import java.util.UUID;

import static com.mcarrental.carservice.exception.ExceptionMessage.NULL_ARG;

@RequiredArgsConstructor
@Component
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final CarConverter converter;
    private final SecurityInfoManager securityInfoManager;
    private final BookingServiceClient bookingServiceClient;
    private final ApplicationEventPublisher eventPublisher;

    @NonNull
    @Override
    @Transactional
    public CarViewDTO create(@NonNull CarCreateRequestDTO createRequest) {
        Assert.notNull(createRequest, NULL_ARG);

        checkVinUniqueness(createRequest.getVin());
        var car = converter.fromCreateRequest(createRequest);
        car.setOwnerId(securityInfoManager.getUserId());
        car.setVisible(true);
        car = carRepository.save(car);
        eventPublisher.publishEvent(CarCreateEvent.fromCar(car));

        return converter.toView(car);
    }

    @NonNull
    @Override
    public ItemsDTO<CarViewDTO> getAll(UUID ownerId, Pageable pageable) {
        pageable = pageable == null ? Pageable.unpaged() : pageable;
        boolean hasOwnerIdParam = ownerId != null;
        if (Role.CAR_OWNER.equals(securityInfoManager.getUserRole())) {
            // restricting car owner to see only his cars
            ownerId = hasOwnerIdParam ? ownerId : securityInfoManager.getUserId();
            securityInfoManager.checkOwnerOrClientRights(ownerId);
        }
        var resultSlice = hasOwnerIdParam ?
                carRepository.findAllByOwnerId(ownerId, pageable) : carRepository.findAllBy(pageable);
        return ItemsDTO.fromSlice(resultSlice);
    }

    @NonNull
    @Override
    public CarViewDTO getById(@NonNull UUID carId) {
        Assert.notNull(carId, NULL_ARG);

        return converter.toView(safeGetCar(carId));
    }

    @NonNull
    @Override
    public String getShortInfo(@NonNull UUID carId) {
        Assert.notNull(carId, NULL_ARG);

        var car = safeGetCar(carId);
        return car.getBrand() + " " + car.getModel();
    }

    @NonNull
    @Override
    @Transactional
    public CarViewDTO update(@NonNull UUID carId, @NonNull CarUpdateRequestDTO updateRequest) {
        Assert.notNull(carId, NULL_ARG);

        var car = safeGetCar(carId);
        securityInfoManager.checkOwnerOrClientRights(car.getOwnerId());
        boolean hasNewData = mergeWithUpdateRequest(car, updateRequest);
        if (hasNewData) {
            car = carRepository.save(car);
            eventPublisher.publishEvent(CarUpdateEvent.fromCar(car));
            return converter.toView(car);
        }
        return converter.toView(car);
    }

    @Override
    @Transactional
    public void changePrice(@NonNull UUID carId, @NonNull Integer newPricePerDay) {
        Assert.notNull(carId, NULL_ARG);
        Assert.notNull(newPricePerDay, NULL_ARG);

        checkNewPrice(newPricePerDay);
        var car = safeGetCar(carId);
        securityInfoManager.checkOwnerOrClientRights(car.getOwnerId());
        if (!Objects.equals(car.getPricePerDay(), newPricePerDay)) {
            car.setPricePerDay(newPricePerDay);
            car = carRepository.save(car);
            eventPublisher.publishEvent(CarPriceUpdateEvent.fromCar(car));
        }
    }

    @Override
    @Transactional
    public void activation(@NonNull UUID carId, boolean visible) {
        Assert.notNull(carId, NULL_ARG);

        var car = safeGetCar(carId);
        securityInfoManager.checkOwnerOrClientRights(car.getOwnerId());
        boolean isActivated = car.isVisible();
        if (isActivated != visible) {
            if (isActivated && bookingServiceClient.carHasActiveBookings(carId)) {
                throw new ConflictException("Cannot deactivate car with active bookings");
            }
            car.setVisible(visible);
            carRepository.save(car);
        }
    }

    @Override
    @Transactional
    public void remove(@NonNull UUID carId) {
        Assert.notNull(carId, NULL_ARG);

        var car = safeGetCar(carId);
        securityInfoManager.checkOwnerOrClientRights(car.getOwnerId());
        boolean canBeDeleted = bookingServiceClient.carHasActiveBookings(carId);
        if (!canBeDeleted) {
            throw new ConflictException("Cannot delete car with active bookings");
        }
        carRepository.delete(car);
    }

    private Car safeGetCar(UUID carId) {
        return carRepository.findById(carId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Car with id %s not found", carId)));
    }

    private void checkVinUniqueness(String vinNumber) {
        if (carRepository.existsByVin(vinNumber)) {
            throw new ConflictException(String.format("Car with vinNumber %s already exists", vinNumber));
        }
    }

    private boolean mergeWithUpdateRequest(Car car, CarUpdateRequestDTO updateRequest) {
        boolean hasNewData = false;

        var brand = updateRequest.getBrand();
        if (brand != null && !brand.equals(car.getBrand())) {
            car.setBrand(brand);
            hasNewData = true;
        }

        var model = updateRequest.getModel();
        if (StringUtils.isNotBlank(model) && !model.equals(car.getModel())) {
            car.setModel(model);
            hasNewData = true;
        }

        var carType = updateRequest.getType();
        if (carType != null && !carType.equals(car.getType())) {
            car.setType(carType);
            hasNewData = true;
        }

        var fuel = updateRequest.getFuel();
        if (fuel != null && !fuel.equals(car.getFuel())) {
            car.setFuel(fuel);
            hasNewData = true;
        }

        var vehicleLayout = updateRequest.getLayout();
        if (vehicleLayout != null && !vehicleLayout.equals(car.getLayout())) {
            car.setLayout(vehicleLayout);
            hasNewData = true;
        }

        var gearBox = updateRequest.getGearBox();
        if (gearBox != null && !gearBox.equals(car.getGearBox())) {
            car.setGearBox(gearBox);
            hasNewData = true;
        }

        var engineCapacity = updateRequest.getEngineCapacity();
        if (engineCapacity != null && !engineCapacity.equals(car.getEngineCapacity())) {
            car.setEngineCapacity(engineCapacity);
            hasNewData = true;
        }

        var productionYear = updateRequest.getProductionYear();
        if (productionYear != null && productionYear.getValue() != car.getProductionYear()) {
            car.setProductionYear(productionYear.getValue());
            hasNewData = true;
        }

        var numberOfSeats = updateRequest.getNumberOfSeats();
        if (numberOfSeats != null && !numberOfSeats.equals(car.getNumberOfSeats())) {
            car.setNumberOfSeats(numberOfSeats);
            hasNewData = true;
        }

        var vin = updateRequest.getVin();
        if (StringUtils.isNotBlank(vin) && !vin.equals(car.getVin())) {
            car.setVin(vin);
            hasNewData = true;
        }

        var numberPlate = updateRequest.getNumberPlate();
        if (StringUtils.isNotBlank(numberPlate) && !numberPlate.equals(car.getNumberPlate())) {
            car.setNumberPlate(numberPlate);
            hasNewData = true;
        }

        return hasNewData;
    }

    private void checkNewPrice(Integer newPrice) {
        if (newPrice < 0) {
            throw new BadRequestException("Price per day cannot be less than 0");
        }
    }
}