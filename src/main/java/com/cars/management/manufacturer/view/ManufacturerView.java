package com.cars.management.manufacturer.view;

import com.cars.management.components.Navbar;
import com.cars.management.core.view.CoreView;
import com.cars.management.manufacturer.entity.ManufacturerEntity;
import com.cars.management.manufacturer.service.ManufacturerService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;


@Route
public class ManufacturerView extends CoreView {

    private FormLayout form;
    private TextField manufacturer;
    private ManufacturerEntity selectedManufacturer;
    private Binder<ManufacturerEntity> binder;
    private Dialog dialog;

    @Autowired
    private ManufacturerService service;

    @PostConstruct
    public void init() {
        add(new Navbar());
        Grid<ManufacturerEntity> grid = addGrid();
        form = new FormLayout();
        addButtonBar("Add Manufacturer", form);
        addForm(grid);
        add(grid);
    }

    private void addForm(Grid<ManufacturerEntity> grid) {
        Button saveBtn = addSaveBtn(grid);
        binder = new Binder<>(ManufacturerEntity.class);

        manufacturer = new TextField();
        manufacturer.setLabel("Manufacturer");
        manufacturer.setPlaceholder("Please enter the manufacturer name");
        manufacturer.setMaxLength(500);

        form.add(manufacturer);
        form.add(saveBtn);
        form.setWidth("600px");
        form.setVisible(false);

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(form);

        verticalLayout.setHorizontalComponentAlignment(Alignment.CENTER, form);
        add(verticalLayout);
    }

    private Grid<ManufacturerEntity> addGrid() {
        Grid<ManufacturerEntity> grid = new Grid<>();
        grid.setItems(service.getAll());
        grid.addColumn(ManufacturerEntity::getId).setHeader("Id").setFlexGrow(0).setAutoWidth(true);
        grid.addColumn(ManufacturerEntity::getManufacturerName).setHeader("Manufacturer");
        grid.addComponentColumn(item -> createRemoveButton(grid, item)).setHeader("Actions").setFlexGrow(0).setAutoWidth(true);
        grid.addComponentColumn(item -> changeData(grid, item)).setFlexGrow(0).setAutoWidth(true);
        grid.asSingleSelect().addValueChangeListener(event -> {
            selectedManufacturer = event.getValue();
            binder.setBean(selectedManufacturer);
            form.setVisible(selectedManufacturer != null);
        });
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);

        return grid;
    }

    private Button addSaveBtn(Grid<ManufacturerEntity> grid) {
        Button saveBtn = new Button("Save");
        saveBtn.addClickListener(buttonClickEvent -> {
            try {
                ManufacturerEntity manufacturerEntity = new ManufacturerEntity();
                manufacturerEntity.setManufacturerName(manufacturer.getValue());
                service.add(manufacturerEntity);
                grid.setItems(service.getAll());
                Notification.show("Successful save");
                form.setVisible(false);
            } catch (Exception e) {
                System.out.println(e);
                Notification.show("Something went wrong");
            }
        });
        return saveBtn;
    }

    private Button createRemoveButton(Grid<ManufacturerEntity> grid, ManufacturerEntity item) {
        Button button = new Button("Remove", clickEvent -> {
            ListDataProvider<ManufacturerEntity> dataProvider = (ListDataProvider<ManufacturerEntity>) grid.getDataProvider();
            dataProvider.getItems().remove(item);
            service.remove(item);
            dataProvider.refreshAll();
            grid.recalculateColumnWidths();
            Notification.show("Successful delete");
        });
        button.setIcon(VaadinIcon.TRASH.create());
        return button;
    }

    private Button changeData(Grid<ManufacturerEntity> grid, ManufacturerEntity item) {
        Button button = new Button("Change name", clickEvent -> {
            /*ListDataProvider<ManufacturerEntity> dataProvider = (ListDataProvider<ManufacturerEntity>) grid.getDataProvider();
            dataProvider.getItems().remove(item);
            service.remove(item);
            dataProvider.refreshAll();
            grid.recalculateColumnWidths();
            Notification.show("Successful delete");*/
            dialog = changeNameDialog(item);
            dialog.open();
        });
        button.setIcon(VaadinIcon.PENCIL.create());
        return button;
    }

    private Dialog changeNameDialog(ManufacturerEntity item) {
        Dialog dialog = new Dialog();

        TextField newName = new TextField();
        newName.setLabel("New name of manufacturer");
        newName.setPlaceholder("Please enter the new name");
        newName.setMaxLength(500);

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(newName);

        dialog.setWidth("600px");
        dialog.setHeight("200px");

        return dialog;
    }
}
