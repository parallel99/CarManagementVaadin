package com.cars.management;

import com.cars.management.components.Navbar;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route
public class MainView extends VerticalLayout {

    public MainView() {
        add(new Navbar());
        Text text = new Text("Fő oldal");
        add(text);
    }
}
