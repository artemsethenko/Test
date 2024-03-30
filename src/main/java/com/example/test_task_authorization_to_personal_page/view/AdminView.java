package com.example.test_task_authorization_to_personal_page.view;

import com.example.test_task_authorization_to_personal_page.components.PersonEditor;
import com.example.test_task_authorization_to_personal_page.components.UserEditor;
import com.example.test_task_authorization_to_personal_page.entity.Person;
import com.example.test_task_authorization_to_personal_page.entity.UserEntity;
import com.example.test_task_authorization_to_personal_page.repositories.PersonRepository;
import com.example.test_task_authorization_to_personal_page.repositories.UserRepository;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import com.vaadin.flow.spring.security.AuthenticationContext;
import jakarta.annotation.security.RolesAllowed;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

@PageTitle("AdminPage")//Название страницы
@Route(value = "admin", layout = MainLayout.class)//адрес страницы и связь с MainLayout
@RolesAllowed("ADMIN")// Уровень доступа
@Uses(Icon.class)//иконка рядом с названием
@Data
public class AdminView extends VerticalLayout {
    private  final PasswordEncoder passwordEncoder;
    private final PersonRepository personRepository;
    private final AuthenticationContext authenticationContext;
    private final UserRepository userRepository;
    private final PersonEditor personEditor;

    private final UserEditor userEditor;

    private Grid<UserEntity> employeeGrid= new Grid<>(UserEntity.class);
    private final TextField filter = new TextField();
    private final Button addNewPersonButton = new Button("Новый пользователь");
    private Span name = new Span();
    private Span email = new Span();
    private Span phone = new Span();
    private Span birthday = new Span();
    UserEntity user;
    Person person;



    @Autowired
    public AdminView(PasswordEncoder passwordEncoder,
                     PersonRepository personRepository,
                     AuthenticationContext authenticationContext,
                     UserRepository userRepository) {

        this.passwordEncoder = passwordEncoder;
        this.personRepository = personRepository;
        this.authenticationContext = authenticationContext;
        this.userRepository = userRepository;


        userEditor = new UserEditor(userRepository);
        personEditor = new PersonEditor(passwordEncoder,personRepository,userRepository);
        authenticationContext.getAuthenticatedUser(UserDetails.class).ifPresent(userDetails -> {
                    String username = userDetails.getUsername();
                    person = personRepository.findByLogin(username);
                });
        user = person.getUserEntity();


        userEditor.setVisible(false);

        updateUserInfoAdmin(user);

        VerticalLayout content = new VerticalLayout(name, email, phone , birthday);
        content.setSpacing(false);
        content.setPadding(false);


        Details details = new Details("Личная информация", content);
        details.setOpened(true);


        filter.setPlaceholder("Поиск");
        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(field -> fillList(field.getValue()));
        HorizontalLayout toolbar = new HorizontalLayout(details,filter, addNewPersonButton);
       // add(toolbar,userEditor,personEditor, employeeGrid );
        add(toolbar);
        add(userEditor );
        add(personEditor);
        add( employeeGrid );

        //редоктирование  при выборе одного из списка
        employeeGrid
                .asSingleSelect()
                .addValueChangeListener(e -> userEditor.editUser(e.getValue()));


//Кнопка создания нового пользователя
        addNewPersonButton.addClickListener(e -> personEditor.editPerson(new Person()));


//Обновление полей после изменений в userEditor
        userEditor.setChangeHandler(() -> {
            userEditor.setVisible(false);
            fillList(filter.getValue());
            UserEntity updatedUser = userEditor.getBinder().getBean();
            updateUserInfoAdmin(updatedUser);
        });
        personEditor.setChangeHandler(()-> {
            fillList(filter.getValue());
        });

        fillList("");
    }
    protected void updateUserInfoAdmin(UserEntity updatedUser){
        name.setText(updatedUser.getFirstName() +" "+ updatedUser.getLastName() + " " + updatedUser.getMiddleName());
        email.setText(updatedUser.getEmail());
        phone.setText(updatedUser.getPhoneNumber());
        birthday.setText(updatedUser.getBirthday().toString());
    }
    //поиск по имени
    protected void fillList(String name) {
        if (name.isEmpty()) {
            employeeGrid.setItems(this.userRepository.findAll());
        } else {
            employeeGrid.setItems(this.userRepository.findByName(name));
        }
    }
}

