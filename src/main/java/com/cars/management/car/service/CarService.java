package com.cars.management.car.service;

import com.cars.management.car.entity.CarEntity;
import com.cars.management.core.service.CoreCRUDService;
import com.cars.management.manufacturer.entity.ManufacturerEntity;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;

import java.util.List;

public interface CarService extends CoreCRUDService<CarEntity> {

    List<CarEntity> filteredCarSearch(String modelSearch, Integer releaseDateSearch, Integer doorNumberSearch, ManufacturerEntity manufactureSearch);
}
