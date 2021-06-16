package com.cars.management.security;

import com.cars.management.core.view.CoreView;
import com.vaadin.flow.router.Route;

@Route("403")
public class ForbiddenView extends CoreView {

    public ForbiddenView(){
        add(navbar());
        add("Access Denied");
    }
}
