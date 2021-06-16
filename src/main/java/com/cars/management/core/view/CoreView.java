package com.cars.management.core.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

public abstract class CoreView extends VerticalLayout {

    public CoreView() {

    }

    public TextField readOnlyField(String label, String text) {
        if (!text.isEmpty()) {
            TextField field = new TextField();
            field.setLabel(label);
            field.setValue(text);
            field.setReadOnly(true);
            field.setWidthFull();
            return field;
        }

        TextField fake = new TextField("fake");
        fake.setVisible(false);
        return fake;
    }

    protected Button disabledButton(String text) {
        Button btn = new Button();
        btn.setText(text);
        btn.setEnabled(false);
        return btn;
    }
}
