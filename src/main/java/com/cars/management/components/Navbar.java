package com.cars.management.components;

import com.cars.management.MainView;
import com.cars.management.car.view.CarView;
import com.cars.management.manufacturer.view.ManufacturerView;
import com.cars.management.security.SecurityUtils;
import com.cars.management.user.view.UserView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class Navbar extends VerticalLayout {

    public Navbar() {
        VerticalLayout verticalLayout = new VerticalLayout();

        MenuBar menuBar = new MenuBar();
        menuBar.setOpenOnHover(true);

        menuBar.addItem("Home Page", e -> UI.getCurrent().navigate(MainView.class));
        menuBar.addItem("Cars", e -> UI.getCurrent().navigate(CarView.class));
        menuBar.addItem("Manufacturers", e -> UI.getCurrent().navigate(ManufacturerView.class));

        if(SecurityUtils.isAdmin()){
            MenuItem admin = menuBar.addItem("Admin");
            SubMenu adminSubMenu = admin.getSubMenu();
            adminSubMenu.addItem("Users", e -> UI.getCurrent().navigate(UserView.class));
            adminSubMenu.addItem("Statistics", e -> UI.getCurrent().navigate(UserView.class));
        }

        menuBar.addItem("Sign Out");

        verticalLayout.add(menuBar);
        verticalLayout.setHorizontalComponentAlignment(Alignment.CENTER, menuBar);

        add(menuBar);
    }
}
