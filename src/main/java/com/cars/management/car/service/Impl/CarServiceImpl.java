package com.cars.management.car.service.Impl;

import com.cars.management.car.entity.CarEntity;
import com.cars.management.car.service.CarService;
import com.cars.management.core.service.impl.CoreCRUDServiceImpl;
import com.cars.management.manufacturer.entity.ManufacturerEntity;
import org.springframework.stereotype.Service;

import java.util.List;

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


    @Override
    public List<CarEntity> filteredCarSearch(String modelSearch, Integer releaseDateSearch, Integer doorNumberSearch, ManufacturerEntity manufactureSearch) {
        return entityManager.createQuery("SELECT c FROM CarEntity c WHERE ((c.doorNumber = :door_number OR :door_number IS NULL) AND c.model LIKE :model AND (c.releaseDate = :release_date OR :release_date IS NULL)) AND c.manufacturer.manufacturerName = :manufacturer", CarEntity.class)
                .setParameter("door_number", doorNumberSearch)
                .setParameter("model", "%"+modelSearch+"%")
                .setParameter("manufacturer", manufactureSearch.getManufacturerName())
                .setParameter("release_date", releaseDateSearch)
                .getResultList();
    }
}
