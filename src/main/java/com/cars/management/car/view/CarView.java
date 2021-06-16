package com.cars.management.car.view;

import com.cars.management.car.entity.CarEntity;
import com.cars.management.car.service.CarService;
import com.cars.management.core.view.CoreView;
import com.cars.management.manufacturer.entity.ManufacturerEntity;
import com.cars.management.manufacturer.service.ManufacturerService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.Calendar;

@Route
public class CarView extends CoreView {

    private FormLayout form;
    private FormLayout formSearch;
    private VerticalLayout mainFormPlace;
    private TextField model;
    private TextField modelSearch;
    private ComboBox<ManufacturerEntity> manufacture;
    private ComboBox<ManufacturerEntity> manufactureSearch;
    private IntegerField doorNumber;
    private IntegerField doorNumberSearch;
    private IntegerField releaseDate;
    private IntegerField releaseDateSearch;
    private CarEntity selectedCar;
    private Button saveBtn;
    private Button searchBtn;
    private Binder<CarEntity> binder;

    @Autowired
    private CarService service;

    @Autowired
    private ManufacturerService manufacturerService;

    @PostConstruct
    public void init() {
        add(navbar());
        Grid<CarEntity> grid = addGrid();
        mainFormPlace = new VerticalLayout();
        form = new FormLayout();
        formSearch = new FormLayout();
        selectedCar = new CarEntity();
        addButtonBar("Add Car","Search Car", grid);
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
        binder = new Binder<>(CarEntity.class);

        model = new TextField();
        model.setLabel("Car model");
        model.setPlaceholder("Please enter the car model");
        model.setMaxLength(200);

        doorNumber = new IntegerField();
        doorNumber.setLabel("Door number");
        doorNumber.setPlaceholder("Please enter the door number");

        releaseDate = new IntegerField();
        releaseDate.setLabel("Release Date");
        releaseDate.setPlaceholder("Please enter the release date number");

        manufacture = new ComboBox<>();
        manufacture.setLabel("Manufacturer");
        manufacture.setPlaceholder("Please enter the manufacturer");
        manufacture.setItems(manufacturerService.getAll());
        manufacture.setItemLabelGenerator(ManufacturerEntity::getManufacturerName);

        form.add(manufacture, model, doorNumber, releaseDate);
        form.add(addBtn);
        form.setColspan(addBtn, 2);
        form.setWidth("1200px");
        form.setVisible(false);
        validation();

        mainFormPlace.add(form);
        mainFormPlace.setHorizontalComponentAlignment(Alignment.CENTER, form);

        binder.bindInstanceFields(this);
    }

    private void addSearchForm(Button btn) {
        modelSearch = new TextField();
        modelSearch.setLabel("Car model");
        modelSearch.setPlaceholder("Please enter the car model");
        modelSearch.setMaxLength(200);

        doorNumberSearch = new IntegerField();
        doorNumberSearch.setLabel("Door number");
        doorNumberSearch.setPlaceholder("Please enter the door number");

        releaseDateSearch = new IntegerField();
        releaseDateSearch.setLabel("Release Date");
        releaseDateSearch.setPlaceholder("Please enter the release date number");

        manufactureSearch = new ComboBox<>();
        manufactureSearch.setLabel("Manufacturer");
        manufactureSearch.setPlaceholder("Please enter the manufacturer");
        manufactureSearch.setItems(manufacturerService.getAll());
        manufactureSearch.setItemLabelGenerator(ManufacturerEntity::getManufacturerName);

        formSearch.add(manufactureSearch, modelSearch, doorNumberSearch, releaseDateSearch);
        formSearch.add(btn);
        formSearch.setColspan(btn, 2);
        formSearch.setWidth("1200px");
        formSearch.setVisible(false);
        validation();

        mainFormPlace.add(formSearch);
        mainFormPlace.setHorizontalComponentAlignment(Alignment.CENTER, formSearch);
    }

    private Grid<CarEntity> addGrid() {
        Grid<CarEntity> grid = new Grid<>();
        grid.setItems(service.getAll());
        grid.addColumn(CarEntity::getId).setHeader("Id").setFlexGrow(0).setAutoWidth(true).setSortable(true);
        grid.addColumn(CarEntity::getModel).setHeader("Model").setSortable(true);
        grid.addColumn(item -> item.getManufacturer().getManufacturerName()).setHeader("Manufacturer").setSortable(true);
        grid.addColumn(CarEntity::getDoorNumber).setHeader("Door Number").setFlexGrow(0).setAutoWidth(true).setSortable(true);
        grid.addColumn(CarEntity::getReleaseDate).setHeader("Release Date").setFlexGrow(0).setAutoWidth(true).setSortable(true);
        grid.addComponentColumn(item -> createRemoveButton(grid, item)).setHeader("Actions").setFlexGrow(0).setAutoWidth(true);
        grid.asSingleSelect().addValueChangeListener(event -> {
            selectedCar = event.getValue();
            loadData(grid);
            formSearch.setVisible(false);
            form.setVisible(!grid.asSingleSelect().isEmpty());
        });
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.setMultiSort(true);
        grid.setMinHeight("350px");

        return grid;
    }

    private Button addSaveBtn(Grid<CarEntity> grid) {
        saveBtn = new Button("Save");
        saveBtn.addClickListener(buttonClickEvent -> {
            try {
                binder.validate();
                if (grid.asSingleSelect().isEmpty() && binder.isValid()) {
                    CarEntity carEntity = new CarEntity();
                    carEntity.setReleaseDate(releaseDate.getValue());
                    carEntity.setModel(model.getValue());
                    carEntity.setDoorNumber(doorNumber.getValue());
                    carEntity.setManufacturer(manufacture.getValue());
                    service.add(carEntity);
                    Notification.show("Successful save");
                    form.setVisible(false);
                    grid.setItems(service.getAll());
                    clearInputs();
                    grid.select(null);
                } else if (binder.isValid()) {
                    selectedCar.setReleaseDate(releaseDate.getValue());
                    selectedCar.setModel(model.getValue());
                    selectedCar.setDoorNumber(doorNumber.getValue());
                    selectedCar.setManufacturer(manufacture.getValue());
                    service.update(selectedCar);
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

    private Button searchBtn(Grid<CarEntity> grid) {
        searchBtn = new Button("Search", VaadinIcon.SEARCH.create());
        searchBtn.addClickListener(buttonClickEvent -> {
            try {
                grid.setItems(service.filteredCarSearch(modelSearch.getValue(), releaseDateSearch.getValue(), doorNumberSearch.getValue(), manufactureSearch.getValue()));
                Notification.show("Successful Search");
            } catch (Exception e) {
                System.out.println(e);
                Notification.show("Manufacturer is required!");
            }
            clearInputs();
        });

        return searchBtn;
    }

    private Button createRemoveButton(Grid<CarEntity> grid, CarEntity item) {
        Button button = new Button("Remove", clickEvent -> {
            ListDataProvider<CarEntity> dataProvider = (ListDataProvider<CarEntity>) grid.getDataProvider();
            dataProvider.getItems().remove(item);
            service.remove(item);
            dataProvider.refreshAll();
            grid.recalculateColumnWidths();
            Notification.show("Successful delete");
        });
        button.setIcon(VaadinIcon.TRASH.create());
        return button;
    }

    protected void addButtonBar(String addText, String searchText, Grid<CarEntity> grid) {
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

    private void loadData(Grid<CarEntity> grid) {
        if (!grid.asSingleSelect().isEmpty()) {
            model.setValue(grid.asSingleSelect().getValue().getModel());
            releaseDate.setValue(grid.asSingleSelect().getValue().getReleaseDate());
            doorNumber.setValue(grid.asSingleSelect().getValue().getDoorNumber());
            manufacture.setValue(grid.asSingleSelect().getValue().getManufacturer());
        }
    }

    private void validation() {
        binder.forField(manufacture).asRequired("The manufacturer field is required");

        binder.forField(doorNumber)
                .asRequired("The door number field is required")
                .withValidator(name -> name > 0, "The door number is too low")
                .withValidator(name -> name < 20, "The door number is too high")
                .bind(CarEntity::getDoorNumber, CarEntity::setDoorNumber);

        binder.forField(releaseDate)
                .asRequired("The door number field is required")
                .withValidator(name -> name > 1800, "Invalid release date")
                .withValidator(name -> name < Calendar.getInstance().get(Calendar.YEAR) + 1, "Invalid release date")
                .bind(CarEntity::getReleaseDate, CarEntity::setReleaseDate);

        binder.forField(model)
                .asRequired("The model field is required")
                .withValidator(name -> name.length() > 4, "The model name is too short")
                .withValidator(name -> name.length() < 200, "The model name is too long")
                .bind(CarEntity::getModel, CarEntity::setModel);
    }

    private void clearInputs() {
        manufacture.setValue(null);
        doorNumber.setValue(null);
        releaseDate.setValue(null);
        model.setValue("");
    }
}
