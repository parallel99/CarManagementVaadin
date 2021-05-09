package com.cars.management.security;

import com.cars.management.components.Navbar;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("403")
public class ForbiddenView extends VerticalLayout {

    public ForbiddenView(){
        add(new Navbar());
        add("Access Denied");
    }
}
