package com.cars.management.user.view;

import com.cars.management.core.view.CoreView;
import com.cars.management.user.entity.RoleEntity;
import com.cars.management.user.entity.UserEntity;
import com.cars.management.user.service.RoleService;
import com.cars.management.user.service.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.PostConstruct;
import java.util.Collections;

@Route
public class UserView extends CoreView {

    private FormLayout form;
    private FormLayout formSearch;
    private UserEntity selectedUser;
    private Binder<UserEntity> binder;
    private TextField username;
    private TextField usernameSearch;
    private String password;
    private TextField lastName;
    private TextField lastNameSearch;
    private TextField firstName;
    private TextField firstNameSearch;
    private ComboBox<RoleEntity> comboBox;

    @Autowired
    private UserService service;

    @Autowired
    private RoleService roleService;

    @PostConstruct
    public void init() {
        add(navbar());
        Grid<UserEntity> grid = addGrid();
        addButtonBar(grid);
        addForm(grid);
        addSearchForm(grid);
        selectedUser = new UserEntity();

        //Add grid to bottom
        setSizeFull();
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(grid);
        verticalLayout.setSizeFull();
        add(verticalLayout);
    }

    private void addForm(Grid<UserEntity> grid) {
        form = new FormLayout();
        binder = new Binder<>(UserEntity.class);

        username = new TextField();
        username.setLabel("Username");
        username.setPlaceholder("Please enter the username");
        username.setMaxLength(60);
        username.setMinLength(4);
        form.add(username);

        firstName = new TextField();
        firstName.setLabel("First name");
        firstName.setPlaceholder("Please enter the First Name");
        firstName.setMaxLength(60);
        firstName.setMinLength(4);
        form.add(firstName);

        lastName = new TextField();
        lastName.setLabel("Last name");
        lastName.setPlaceholder("Please enter the Last Name");
        lastName.setMaxLength(60);
        lastName.setMinLength(4);
        form.add(lastName);

        comboBox = new ComboBox<>();
        comboBox.setItems(roleService.getAll());
        comboBox.setItemLabelGenerator(authorEntity -> authorEntity.getAuthority());
        comboBox.setLabel("Authorities");
        comboBox.setPlaceholder("Please choose a authority");
        form.add(comboBox);

        Button saveBtn = addSaveBtn(grid);

        form.add(saveBtn);
        form.setColspan(saveBtn, 2);
        form.setWidth("1200px");
        validation();

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(form);
        verticalLayout.setHorizontalComponentAlignment(Alignment.CENTER, form);

        add(verticalLayout);
        form.setVisible(false);
        binder.bindInstanceFields(this);
    }

    private void addSearchForm(Grid<UserEntity> grid) {
        formSearch = new FormLayout();

        usernameSearch = new TextField();
        usernameSearch.setLabel("Username");
        usernameSearch.setPlaceholder("Please enter the username");
        usernameSearch.setMaxLength(60);
        formSearch.add(usernameSearch);

        firstNameSearch = new TextField();
        firstNameSearch.setLabel("First name");
        firstNameSearch.setPlaceholder("Please enter the First Name");
        firstNameSearch.setMaxLength(60);
        formSearch.add(firstNameSearch);

        lastNameSearch = new TextField();
        lastNameSearch.setLabel("Last name");
        lastNameSearch.setPlaceholder("Please enter the Last Name");
        lastNameSearch.setMaxLength(60);
        formSearch.add(lastNameSearch);
        formSearch.setColspan(lastNameSearch, 2);

        Button saveBtn = addSearchBtn(grid);

        formSearch.add(saveBtn);
        formSearch.setColspan(saveBtn, 2);
        formSearch.setWidth("1200px");

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(formSearch);
        verticalLayout.setHorizontalComponentAlignment(Alignment.CENTER, formSearch);

        add(verticalLayout);
        formSearch.setVisible(false);
    }

    private Button addSaveBtn(Grid<UserEntity> grid) {
        Button saveBtn = new Button("Save");
        saveBtn.addClickListener(buttonClickEvent -> {
            try{
                binder.validate();
                if (grid.asSingleSelect().isEmpty() && binder.isValid()) {
                    UserEntity userEntity = new UserEntity();
                    userEntity.setUsername(username.getValue());
                    userEntity.setLastName(lastName.getValue());
                    userEntity.setFirstName(firstName.getValue());
                    userEntity.setAuthorities(Collections.singletonList(comboBox.getValue()));
                    password = service.generatePassayPassword();
                    userEntity.setPassword(new BCryptPasswordEncoder().encode(password));
                    service.add(userEntity);
                    grid.setItems(service.getAll());
                    passwordDialog();
                    clearInputs();
                    form.setVisible(false);
                    Notification.show("Successful save");
                } else if (binder.isValid()) {
                    selectedUser.setUsername(username.getValue());
                    selectedUser.setLastName(lastName.getValue());
                    selectedUser.setFirstName(firstName.getValue());
                    service.update(selectedUser);
                    grid.setItems(service.getAll());
                    clearInputs();
                    Notification.show("Successful update");
                }
            } catch (Exception e) {
                System.out.println(e);
                Notification.show("Something went wrong");
            }
        });
        return saveBtn;
    }

    private Button addSearchBtn(Grid<UserEntity> grid) {
        Button saveBtn = new Button("Search", VaadinIcon.SEARCH.create());
        saveBtn.addClickListener(buttonClickEvent -> {
            try{
                grid.setItems(service.filteredSearch(usernameSearch.getValue(), firstNameSearch.getValue(), lastNameSearch.getValue()));
                Notification.show("Successful Search");
            } catch (Exception e) {
                System.out.println(e);
                Notification.show("Something went wrong");
            }
        });
        return saveBtn;
    }

    private void addButtonBar(Grid<UserEntity> grid) {
        Button addBtn = new Button("Add user", VaadinIcon.PLUS.create());
        addBtn.addClickListener(buttonClickEvent -> {
            form.setVisible(!form.isVisible());
            if (form.isVisible()) {
                addBtn.setText("Close add user");
            } else {
                addBtn.setText("Add user");
            }
            formSearch.setVisible(false);
        });


        Button searchBtn = new Button("Search user", VaadinIcon.SEARCH.create());
        searchBtn.addClickListener(buttonClickEvent -> {
            formSearch.setVisible(!formSearch.isVisible());
            if (formSearch.isVisible()) {
                searchBtn.setText("Close search user");
            } else {
                searchBtn.setText("Search user");
            }
            form.setVisible(false);
        });

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(addBtn, searchBtn);
        verticalLayout.setHorizontalComponentAlignment(Alignment.CENTER, addBtn);
        verticalLayout.setHorizontalComponentAlignment(Alignment.CENTER, searchBtn);

        add(verticalLayout);
    }

    private Button removeBtn(Grid<UserEntity> grid, UserEntity item) {
        Button deleteBtn = new Button("Delete", VaadinIcon.TRASH.create());
        deleteBtn.addClickListener(buttonClickEvent -> {
            service.remove(item);
            Notification.show("Successful delete");
            grid.setItems(service.getAll());
        });

        return deleteBtn;
    }

    private Grid<UserEntity> addGrid() {
        Grid<UserEntity> grid = new Grid<>();
        grid.setItems(service.getAll());
        grid.addColumn(UserEntity::getId).setHeader("Id").setFlexGrow(0).setAutoWidth(true).setSortable(true);
        grid.addColumn(UserEntity::getUsername).setHeader("Username").setFlexGrow(0).setAutoWidth(true).setSortable(true);
        grid.addColumn(UserEntity::getFirstName).setHeader("First name").setFlexGrow(0).setAutoWidth(true).setSortable(true);
        grid.addColumn(UserEntity::getLastName).setHeader("Last name").setFlexGrow(0).setAutoWidth(true).setSortable(true);
        grid.addColumn(userEntity -> {
                    if (userEntity.getAuthorities() != null) {
                        StringBuilder builder = new StringBuilder();
                        userEntity.getAuthorities().forEach(roleEntity -> {
                            builder.append(roleEntity.getAuthority()).append(",");
                        });
                        return builder.toString();
                    }
                    return "";
                }
        ).setHeader("Authorities").setSortable(true);
        grid.addComponentColumn(item -> removeBtn(grid, item)).setHeader("Actions");
        grid.asSingleSelect().addValueChangeListener(event -> {
            selectedUser = event.getValue();
            loadData(grid);
            formSearch.setVisible(false);
            form.setVisible(!grid.asSingleSelect().isEmpty());
        });
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.setMultiSort(true);
        grid.setMinHeight("350px");

        return grid;
    }

    private void validation() {
        binder.forField(username)
                .asRequired("The username field is required")
                .withValidator(name -> name.length() >= 6, "The username is too short")
                .withValidator(name -> name.length() <= 40, "The username is too long")
                .bind(UserEntity::getUsername, UserEntity::setUsername);

        binder.forField(firstName)
                .asRequired("The first name field is required")
                .withValidator(name -> name.length() >= 3, "The first name is too short")
                .withValidator(name -> name.length() <= 40, "The first name is too long")
                .bind(UserEntity::getFirstName, UserEntity::setFirstName);

        binder.forField(lastName)
                .asRequired("The last name field is required")
                .withValidator(name -> name.length() >= 3, "The last name is too short")
                .withValidator(name -> name.length() <= 40, "The last name is too long")
                .bind(UserEntity::getLastName, UserEntity::setLastName);

        binder.forField(comboBox).asRequired("Please choose a authority");
    }

    private void loadData(Grid<UserEntity> grid) {
        if (!grid.asSingleSelect().isEmpty()) {
            password = grid.asSingleSelect().getValue().getPassword();
            username.setValue(grid.asSingleSelect().getValue().getUsername());
            firstName.setValue(grid.asSingleSelect().getValue().getFirstName());
            lastName.setValue(grid.asSingleSelect().getValue().getLastName());
        }
    }

    private void passwordDialog() {
        Dialog dialog = new Dialog();
        dialog.add(readOnlyField(username.getValue() + " password", password));
        dialog.open();
        password = "";
    }

    public static TextField readOnlyField(String label, String text) {
        TextField field = new TextField();
        field.setLabel(label);
        field.setValue(text);
        field.setReadOnly(true);
        field.setWidthFull();
        return field;
    }

    private void clearInputs() {
        username.setValue("");
        lastName.setValue("");
        firstName.setValue("");
        comboBox.setValue(null);
    }
}
