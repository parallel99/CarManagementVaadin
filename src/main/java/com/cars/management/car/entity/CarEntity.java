package com.cars.management.car.entity;

import com.cars.management.core.entity.CoreEntity;
import com.cars.management.manufacturer.entity.ManufacturerEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "car")
@Entity
public class CarEntity extends CoreEntity {

    @Column(name = "model")
    private String model;

    @Column(name = "door_number")
    private Integer doorNumber;

    @Column(name = "release_date")
    private Integer releaseDate;

    @ManyToOne
    private ManufacturerEntity manufacturer;

    public CarEntity(String model, Integer doorNumber, Integer releaseDate, ManufacturerEntity manufacturer) {
        this.model = model;
        this.doorNumber = doorNumber;
        this.releaseDate = releaseDate;
        this.manufacturer = manufacturer;
    }

    public CarEntity() {

    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getDoorNumber() {
        return doorNumber;
    }

    public void setDoorNumber(Integer doorNumber) {
        this.doorNumber = doorNumber;
    }

    public Integer getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Integer releaseDate) {
        this.releaseDate = releaseDate;
    }

    public ManufacturerEntity getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(ManufacturerEntity manufacturer) {
        this.manufacturer = manufacturer;
    }

    @Override
    public String toString() {
        return "CarEntity{" +
                "model='" + model + '\'' +
                ", doorNumber=" + doorNumber +
                ", releaseDate=" + releaseDate +
                ", manufacturer=" + manufacturer +
                '}';
    }
}
