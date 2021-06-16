package com.cars.management.user.view;

import com.cars.management.core.view.CoreView;
import com.cars.management.user.entity.RoleEntity;
import com.cars.management.user.entity.UserEntity;
import com.cars.management.user.service.RoleService;
import com.cars.management.user.service.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
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
    private UserEntity selectedUser;
    private Binder<UserEntity> binder;
    private TextField username;
    private PasswordField password;
    private TextField lastName;
    private TextField firstName;
    private ComboBox<RoleEntity> comboBox;
    private Button deleteBtn = new Button("Delete", VaadinIcon.TRASH.create());

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
        add(grid);
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

        password = new PasswordField();
        password.setLabel("Password");
        password.setPlaceholder("Please enter the password");
        password.setMaxLength(80);
        password.setMinLength(6);
        form.add(password);

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

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(form);
        verticalLayout.setHorizontalComponentAlignment(Alignment.CENTER, form);

        add(verticalLayout);
        form.setVisible(false);
        binder.bindInstanceFields(this);
    }

    private Button addSaveBtn(Grid<UserEntity> grid) {
        Button saveBtn = new Button("Save");
        saveBtn.addClickListener(buttonClickEvent -> {
            if (selectedUser.getId() == null) {
                UserEntity userEntity = new UserEntity();
                userEntity.setUsername(selectedUser.getUsername());
                userEntity.setLastName(selectedUser.getLastName());
                userEntity.setFirstName(selectedUser.getFirstName());
                userEntity.setAuthorities(Collections.singletonList(comboBox.getValue()));
                userEntity.setPassword(new BCryptPasswordEncoder().encode(selectedUser.getPassword()));
                service.add(userEntity);
                grid.setItems(service.getAll());
                selectedUser = null;
                Notification.show("Successful save");
            } else {
                service.update(selectedUser);
                grid.setItems(service.getAll());
                Notification.show("Successful update");
            }
            form.setVisible(false);
        });
        return saveBtn;
    }

    private void addButtonBar(Grid<UserEntity> grid) {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        deleteBtn.addClickListener(buttonClickEvent -> {
            service.remove(selectedUser);
            Notification.show("Successful delete");
            selectedUser = null;
            grid.setItems(service.getAll());
            form.setVisible(false);
        });
        deleteBtn.setEnabled(false);

        Button addBtn = new Button("Add", VaadinIcon.PLUS.create());
        addBtn.addClickListener(buttonClickEvent -> {
            selectedUser = new UserEntity();
            binder.setBean(selectedUser);
            form.setVisible(true);
            password.setVisible(true);
        });
        horizontalLayout.add(deleteBtn);
        horizontalLayout.add(addBtn);
        horizontalLayout.setWidth(null);

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(horizontalLayout);
        verticalLayout.setHorizontalComponentAlignment(Alignment.CENTER, horizontalLayout);

        add(verticalLayout);
    }

    private Grid<UserEntity> addGrid() {
        Grid<UserEntity> grid = new Grid<>();
        grid.setItems(service.getAll());
        grid.addColumn(UserEntity::getId).setHeader("Id");
        grid.addColumn(UserEntity::getUsername).setHeader("Username");
        grid.addColumn(UserEntity::getFirstName).setHeader("First name");
        grid.addColumn(UserEntity::getLastName).setHeader("Last name");
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
        ).setHeader("Authorities");
        grid.asSingleSelect().addValueChangeListener(event -> {
            selectedUser = event.getValue();
            binder.setBean(selectedUser);
            form.setVisible(selectedUser != null);
            deleteBtn.setEnabled(selectedUser != null);
            password.setVisible(false);
        });
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);

        return grid;
    }
}
