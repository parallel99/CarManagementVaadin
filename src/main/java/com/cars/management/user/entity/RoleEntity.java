package com.cars.management.user.entity;

import com.cars.management.core.entity.CoreEntity;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Table(name = "app_role")
@Entity
public class RoleEntity extends CoreEntity implements GrantedAuthority {

    @Column(name = "authority")
    private String authority;

    @Override
    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
