package com.cars.management.car.service.Impl;

import com.cars.management.car.entity.CarEntity;
import com.cars.management.car.service.CarService;
import com.cars.management.core.service.impl.CoreCRUDServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class CarServiceImpl extends CoreCRUDServiceImpl<CarEntity> implements CarService {

    @Override
    protected void updateCore(CarEntity persistedEntity, CarEntity entity) {
        persistedEntity.setDoorNumber(entity.getDoorNumber());
        persistedEntity.setManufacturer(entity.getManufacturer());
        persistedEntity.setModel(entity.getModel());
        persistedEntity.setReleaseDate(entity.getReleaseDate());
    }

    @Override
    protected Class<CarEntity> getManagedClass() {
        return CarEntity.class;
    }
}
