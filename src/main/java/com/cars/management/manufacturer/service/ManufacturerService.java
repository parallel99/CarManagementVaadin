package com.cars.management.manufacturer.service;

import com.cars.management.core.service.CoreCRUDService;
import com.cars.management.manufacturer.entity.ManufacturerEntity;

import java.util.List;

public interface ManufacturerService extends CoreCRUDService<ManufacturerEntity> {

    List<ManufacturerEntity> findByManufactureName(String text);

    Long countManufacturesByName(String text);
}
