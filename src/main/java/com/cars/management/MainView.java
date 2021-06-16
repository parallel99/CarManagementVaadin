package com.cars.management;

import com.cars.management.core.view.CoreView;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route
@StyleSheet("styles/styles.css")
public class MainView extends CoreView {

    public MainView() {
        VerticalLayout nav = navbar();
        nav.addClassName("menubar");

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(nav);
        verticalLayout.setHorizontalComponentAlignment(Alignment.CENTER, nav);

        add(verticalLayout);
        addClassName("main");
        setSizeFull();
    }
}
