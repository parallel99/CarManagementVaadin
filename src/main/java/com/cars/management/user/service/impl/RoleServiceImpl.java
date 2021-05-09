package com.cars.management.user.service.impl;

import com.cars.management.core.service.impl.CoreCRUDServiceImpl;
import com.cars.management.user.entity.RoleEntity;
import com.cars.management.user.service.RoleService;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl extends CoreCRUDServiceImpl<RoleEntity> implements RoleService {

    @Override
    protected void updateCore(RoleEntity persistedEntity, RoleEntity entity) {
        persistedEntity.setAuthority(entity.getAuthority());
    }

    @Override
    protected Class<RoleEntity> getManagedClass() {
        return RoleEntity.class;
    }
}
