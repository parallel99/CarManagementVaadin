package com.cars.management.core.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public abstract class CoreView extends VerticalLayout {

    public CoreView() {

    }

    protected void addButtonBar(String addText, FormLayout form) {
        Button btn = new Button();
        btn.setText(addText);
        btn.setIcon(VaadinIcon.PLUS.create());
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(btn);
        verticalLayout.setHorizontalComponentAlignment(Alignment.CENTER, btn);
        btn.addClickListener(buttonClickEvent -> {
           form.setVisible(!form.isVisible());
        });

        add(verticalLayout);
    }
}
