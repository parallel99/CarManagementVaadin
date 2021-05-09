package com.cars.management.manufacturer.service.Impl;

import com.cars.management.core.service.impl.CoreCRUDServiceImpl;
import com.cars.management.manufacturer.entity.ManufacturerEntity;
import com.cars.management.manufacturer.service.ManufacturerService;
import org.springframework.stereotype.Service;

@Service
public class ManufacturerServiceImpl extends CoreCRUDServiceImpl<ManufacturerEntity> implements ManufacturerService {

    @Override
    protected void updateCore(ManufacturerEntity persistedEntity, ManufacturerEntity entity) {
        persistedEntity.setManufacturerName(entity.getManufacturerName());
    }

    @Override
    protected Class<ManufacturerEntity> getManagedClass() {
        return ManufacturerEntity.class;
    }
}
