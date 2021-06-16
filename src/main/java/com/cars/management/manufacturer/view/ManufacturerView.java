package com.cars.management.manufacturer.view;

import com.cars.management.components.Navbar;
import com.cars.management.core.view.CoreView;
import com.cars.management.manufacturer.entity.ManufacturerEntity;
import com.cars.management.manufacturer.service.ManufacturerService;
import com.vaadin.flow.component.button.Button;
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
    private FormLayout formSearch;
    private VerticalLayout mainFormPlace;
    private TextField manufacturer;
    private TextField manufacturerSearch;
    private ManufacturerEntity selectedManufacturer;
    private Button saveBtn;
    private Button searchBtn;
    private Binder<ManufacturerEntity> binder;

    @Autowired
    private ManufacturerService service;

    @PostConstruct
    public void init() {
        add(new Navbar());
        Grid<ManufacturerEntity> grid = addGrid();
        mainFormPlace = new VerticalLayout();
        form = new FormLayout();
        formSearch = new FormLayout();
        selectedManufacturer = new ManufacturerEntity();
        addButtonBar("Add Manufacturer","Search Manufacture", grid);
        addForm(addSaveBtn(grid));
        addSearchForm(searchBtn(grid));
        add(mainFormPlace);

        setSizeFull();
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(grid);
        verticalLayout.setSizeFull();
        add(verticalLayout);
    }

    private void addForm(Button addBtn) {
        binder = new Binder<>(ManufacturerEntity.class);

        manufacturer = new TextField();
        manufacturer.setLabel("Manufacturer");
        manufacturer.setPlaceholder("Please enter the manufacturer name");
        manufacturer.setMaxLength(500);

        form.add(manufacturer);
        form.add(addBtn);
        form.setWidth("600px");
        form.setVisible(false);
        validation();

        mainFormPlace.add(form);
        mainFormPlace.setHorizontalComponentAlignment(Alignment.CENTER, form);

        binder.bindInstanceFields(this);
    }

    private void addSearchForm(Button btn) {
        manufacturerSearch = new TextField();
        manufacturerSearch.setLabel("Manufacturer");
        manufacturerSearch.setPlaceholder("Please enter the manufacturer name");
        manufacturerSearch.setMaxLength(500);

        formSearch.add(manufacturerSearch);
        formSearch.add(btn);
        formSearch.setWidth("600px");
        formSearch.setVisible(false);
        validation();

        mainFormPlace.add(formSearch);
        mainFormPlace.setHorizontalComponentAlignment(Alignment.CENTER, formSearch);
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
            formSearch.setVisible(false);
            form.setVisible(!grid.asSingleSelect().isEmpty());
        });
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.setMultiSort(true);
        grid.setMinHeight("350px");

        return grid;
    }

    private Button addSaveBtn(Grid<ManufacturerEntity> grid) {
        saveBtn = new Button("Save");
        saveBtn.addClickListener(buttonClickEvent -> {
            try {
                binder.validate();
                if (grid.asSingleSelect().isEmpty() && binder.isValid()) {
                    ManufacturerEntity manufacturerEntity = new ManufacturerEntity();
                    manufacturerEntity.setManufacturerName(manufacturer.getValue());
                    service.add(manufacturerEntity);
                    Notification.show("Successful save");
                    form.setVisible(false);
                    grid.setItems(service.getAll());
                    clearInputs();
                    grid.select(null);
                } else if (binder.isValid()) {
                    selectedManufacturer.setManufacturerName(manufacturer.getValue());
                    service.update(selectedManufacturer);
                    grid.getDataProvider().refreshAll();
                    clearInputs();
                    grid.select(null);
                    Notification.show("Successful update");
                }

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
                grid.setItems(service.findByManufactureName(manufacturerSearch.getValue()));
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

    protected void addButtonBar(String addText, String searchText, Grid<ManufacturerEntity> grid) {
        Button addBtnLocal = new Button();
        addBtnLocal.setText(addText);
        addBtnLocal.setIcon(VaadinIcon.PLUS.create());

        addBtnLocal.addClickListener(buttonClickEvent -> {
            grid.select(null);
            clearInputs();
            formSearch.setVisible(false);
            form.setVisible(!form.isVisible());
        });

        Button searchBtnLocal = new Button();
        searchBtnLocal.setText(searchText);
        searchBtnLocal.setIcon(VaadinIcon.SEARCH.create());

        searchBtnLocal.addClickListener(buttonClickEvent -> {
            form.setVisible(false);
            formSearch.setVisible(!formSearch.isVisible());
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

    private void validation() {
        binder.forField(manufacturer)
                .asRequired("The manufacturer field is required")
                .withValidator(name -> name.length() >= 6, "The manufacturer name is too short")
                .withValidator(name -> name.length() <= 500, "The manufacturer name is too long")
                .withValidator(name -> service.countManufacturesByName(name) == 0, "The manufacturer name is not unique")
                .bind(ManufacturerEntity::getManufacturerName, ManufacturerEntity::setManufacturerName);
    }

    private void clearInputs() {
        manufacturer.setValue("");
    }
}
