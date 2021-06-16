package com.cars.management.manufacturer.service.Impl;

import com.cars.management.core.service.impl.CoreCRUDServiceImpl;
import com.cars.management.manufacturer.entity.ManufacturerEntity;
import com.cars.management.manufacturer.service.ManufacturerService;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public List<ManufacturerEntity> findByManufactureName(String text) {
        return entityManager.createQuery("SELECT n FROM ManufacturerEntity n WHERE n.manufacturerName LIKE :name", ManufacturerEntity.class)
                .setParameter("name", "%"+text+"%")
                .getResultList();
    }

    @Override
    public Long countManufacturesByName(String text) {
        return entityManager.createQuery("SELECT COUNT(n) FROM ManufacturerEntity n WHERE n.manufacturerName LIKE :name", Long.class)
                .setParameter("name", text)
                .getSingleResult();
    }
}
