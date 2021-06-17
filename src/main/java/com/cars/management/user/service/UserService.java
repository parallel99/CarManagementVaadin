package com.cars.management.user.service;

import com.cars.management.core.service.CoreCRUDService;
import com.cars.management.user.entity.UserEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends CoreCRUDService<UserEntity>, UserDetailsService {

    String generatePassayPassword();

    List<UserEntity> filteredSearch(String username, String firstName, String lastName);
}
