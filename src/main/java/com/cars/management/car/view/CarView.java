package com.cars.management.car.view;

import com.cars.management.components.Navbar;
import com.cars.management.core.view.CoreView;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.router.Route;

@Route
public class CarView extends CoreView {

    public CarView() {
        add(new Navbar());
        add(new Text("Ez az autó oldal"));
    }
}
