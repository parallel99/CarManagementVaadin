package com.cars.management.security;

import com.cars.management.core.view.CoreView;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("403")
@StyleSheet("styles/styles.css")
public class ForbiddenView extends CoreView {

    public ForbiddenView(){
        add(navbar());

        Div text = new Div();
        text.setText("Access Denied");
        text.setClassName("access-d");

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(text);
        verticalLayout.setHorizontalComponentAlignment(Alignment.CENTER, text);

        add(verticalLayout);
    }
}
