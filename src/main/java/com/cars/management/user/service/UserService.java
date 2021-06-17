package com.cars.management.user.service;

import com.cars.management.core.service.CoreCRUDService;
import com.cars.management.user.entity.UserEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends CoreCRUDService<UserEntity>, UserDetailsService {

    String generatePassayPassword();
}
