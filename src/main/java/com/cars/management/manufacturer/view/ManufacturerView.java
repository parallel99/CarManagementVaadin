package com.cars.management.manufacturer.view;

import com.cars.management.components.Navbar;
import com.cars.management.core.view.CoreView;
import com.cars.management.manufacturer.entity.ManufacturerEntity;
import com.cars.management.manufacturer.service.ManufacturerService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
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
    private Button saveBtn;
    private Button searchBtn;

    @Autowired
    private ManufacturerService service;

    @PostConstruct
    public void init() {
        add(new Navbar());
        Grid<ManufacturerEntity> grid = addGrid();
        form = new FormLayout();
        selectedManufacturer = new ManufacturerEntity();
        addButtonBar("Add Manufacturer","Search Manufacture", form, grid);
        addForm(addSaveBtn(grid), searchBtn(grid));
        add(grid);
    }

    private void addForm(Button addBtn, Button searchBtn) {
        manufacturer = new TextField();
        manufacturer.setLabel("Manufacturer");
        manufacturer.setPlaceholder("Please enter the manufacturer name");
        manufacturer.setMaxLength(500);

        searchBtn.setVisible(false);

        form.add(manufacturer);
        form.add(addBtn);
        form.add(searchBtn);
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
        grid.addColumn(ManufacturerEntity::getId).setHeader("Id").setFlexGrow(0).setAutoWidth(true).setSortable(true);
        grid.addColumn(ManufacturerEntity::getManufacturerName).setHeader("Manufacturer").setSortable(true);
        grid.addComponentColumn(item -> createRemoveButton(grid, item)).setHeader("Actions").setFlexGrow(0).setAutoWidth(true);
        grid.asSingleSelect().addValueChangeListener(event -> {
            selectedManufacturer = event.getValue();
            loadData(grid);
            form.setVisible(!grid.asSingleSelect().isEmpty());
        });
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.setMultiSort(true);

        return grid;
    }

    private Button addSaveBtn(Grid<ManufacturerEntity> grid) {
        saveBtn = new Button("Save");
        saveBtn.addClickListener(buttonClickEvent -> {
            try {
                if (grid.asSingleSelect().isEmpty()) {
                    ManufacturerEntity manufacturerEntity = new ManufacturerEntity();
                    manufacturerEntity.setManufacturerName(manufacturer.getValue());
                    service.add(manufacturerEntity);
                    Notification.show("Successful save");
                    form.setVisible(false);
                    grid.setItems(service.getAll());
                } else {
                    selectedManufacturer.setManufacturerName(manufacturer.getValue());
                    System.out.println(selectedManufacturer.getId() + " : " + selectedManufacturer.getManufacturerName());
                    service.update(selectedManufacturer);
                    grid.getDataProvider().refreshAll();
                    Notification.show("Successful update");
                }
                clearInputs();
                grid.select(null);
            } catch (Exception e) {
                System.out.println(e);
                Notification.show("Something went wrong");
            }
        });

        return saveBtn;
    }

    private Button searchBtn(Grid<ManufacturerEntity> grid) {
        searchBtn = new Button("Search", VaadinIcon.SEARCH.create());
        searchBtn.addClickListener(buttonClickEvent -> {
            try {
                grid.setItems(service.findByManufactureName(manufacturer.getValue()));
                Notification.show("Successful Search");
            } catch (Exception e) {
                System.out.println(e);
                Notification.show("Something went wrong");
            }
            clearInputs();
        });

        return searchBtn;
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

    protected void addButtonBar(String addText, String searchText, FormLayout form, Grid<ManufacturerEntity> grid) {
        Button addBtnLocal = new Button();
        addBtnLocal.setText(addText);
        addBtnLocal.setIcon(VaadinIcon.PLUS.create());

        addBtnLocal.addClickListener(buttonClickEvent -> {
            saveBtn.setVisible(true);
            searchBtn.setVisible(false);
            grid.select(null);
            clearInputs();
            form.setVisible(!form.isVisible());
        });

        Button searchBtnLocal = new Button();
        searchBtnLocal.setText(searchText);
        searchBtnLocal.setIcon(VaadinIcon.SEARCH.create());

        searchBtnLocal.addClickListener(buttonClickEvent -> {
            searchBtn.setVisible(true);
            saveBtn.setVisible(false);
            form.setVisible(!form.isVisible());
        });

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(addBtnLocal, searchBtnLocal);
        verticalLayout.setHorizontalComponentAlignment(Alignment.CENTER, addBtnLocal, searchBtnLocal);

        add(verticalLayout);
    }

    private void loadData(Grid<ManufacturerEntity> grid) {
        if (!grid.asSingleSelect().isEmpty()) {
            manufacturer.setValue(grid.asSingleSelect().getValue().getManufacturerName());
        }
    }

    private void clearInputs() {
        manufacturer.setValue("");
    }
}
