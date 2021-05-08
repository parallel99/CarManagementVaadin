package com.cars.management.manufacturer.view;

import com.cars.management.components.Navbar;
import com.cars.management.core.view.CoreView;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.router.Route;

@Route
public class ManufacturerView extends CoreView {

    private ManufacturerView () {
        add(new Navbar());
        add(new Text("Ez az gyártó oldal"));
    }
}
