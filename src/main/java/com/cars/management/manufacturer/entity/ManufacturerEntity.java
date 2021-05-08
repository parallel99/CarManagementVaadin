package com.cars.management.manufacturer.entity;

import com.cars.management.core.entity.CoreEntity;

import javax.persistence.*;

@Table(name = "Manufacturer")
@Entity
public class ManufacturerEntity extends CoreEntity {

    @Column(name = "manufacturer_name", unique = true, nullable = false)
    private String manufacturerName;

    public String getManufacturerName() {
        return manufacturerName;
    }

    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }

    @Override
    public String toString() {
        return "ManufacturerEntity{" +
                "manufacturerName='" + manufacturerName + '\'' +
                '}';
    }
}
