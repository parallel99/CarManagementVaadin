package com.cars.management.user.entity;

import com.cars.management.core.entity.CoreEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Table(name = "User")
@Entity
public class UserEntity extends CoreEntity {

    @Column(name = "first_name")
    private String firstname;

    @Column(name = "last_name")
    private String lastname;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;
}
