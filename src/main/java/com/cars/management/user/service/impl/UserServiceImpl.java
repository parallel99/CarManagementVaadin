package com.cars.management.user.service.impl;

import com.cars.management.core.service.impl.CoreCRUDServiceImpl;
import com.cars.management.user.entity.UserEntity;
import com.cars.management.user.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.persistence.TypedQuery;

@Service
public class UserServiceImpl extends CoreCRUDServiceImpl<UserEntity> implements UserService {

    @Override
    protected void updateCore(UserEntity persistedEntity, UserEntity entity) {
        persistedEntity.setAuthorities(entity.getAuthorities());
        persistedEntity.setUsername(entity.getUsername());
        persistedEntity.setFirstName(entity.getFirstName());
        persistedEntity.setLastName(entity.getLastName());
    }

    @Override
    protected Class<UserEntity> getManagedClass() {
        return UserEntity.class;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        TypedQuery<UserEntity> query = entityManager.createNamedQuery(UserEntity.FIND_USER_BY_USERNAME, UserEntity.class);
        query.setParameter("username", username);
        return query.getSingleResult();
    }
}
