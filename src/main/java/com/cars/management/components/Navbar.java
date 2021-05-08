package com.cars.management.components;

import com.cars.management.MainView;
import com.cars.management.car.view.CarView;
import com.cars.management.manufacturer.view.ManufacturerView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class Navbar extends VerticalLayout {

    public Navbar() {
        MenuBar menuBar = new MenuBar();
        menuBar.setOpenOnHover(true);

        menuBar.addItem("Home Page", e -> UI.getCurrent().navigate(MainView.class));
        menuBar.addItem("Car management", e -> UI.getCurrent().navigate(CarView.class));
        menuBar.addItem("Manufacturer management", e -> UI.getCurrent().navigate(ManufacturerView.class));

        MenuItem admin = menuBar.addItem("Admin");
        SubMenu adminSubMenu = admin.getSubMenu();
        adminSubMenu.addItem("Users management");

        menuBar.addItem("Sign Out");

        add(menuBar);
    }
}
