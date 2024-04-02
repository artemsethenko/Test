package com.example.test_task_authorization_to_personal_page.view;

import com.example.test_task_authorization_to_personal_page.components.PersonEditor;
import com.example.test_task_authorization_to_personal_page.components.UserEditor;
import com.example.test_task_authorization_to_personal_page.entity.Person;
import com.example.test_task_authorization_to_personal_page.entity.UserEntity;
import com.example.test_task_authorization_to_personal_page.repositories.PersonRepository;
import com.example.test_task_authorization_to_personal_page.repositories.UserRepository;

import com.vaadin.flow.component.UI;
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
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@PageTitle("AdminPage")//Название страницы
@Route(value = "admin", layout = MainLayout.class)//адрес страницы и связь с MainLayout
@RolesAllowed("ADMIN")// Уровень доступа
@Uses(Icon.class)//иконка рядом с названием
@Getter
public class AdminView extends VerticalLayout {
    private final PasswordEncoder passwordEncoder;
    private final PersonRepository personRepository;
    private final AuthenticationContext authenticationContext;
    private final UserRepository userRepository;
    private final PersonEditor personEditor;

    private final UserEditor userEditor;

    private final Grid<UserEntity> employeeGrid = new Grid<>(UserEntity.class);
    private final TextField filter = new TextField();
    private final Button addNewPersonButton = new Button("Новый пользователь");
    private final Span name = new Span();
    private final Span email = new Span();
    private final Span phone = new Span();
    private final Span birthday = new Span();
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
        personEditor = new PersonEditor(passwordEncoder, personRepository, userRepository);

        //Получаю доступ к текушему пользователю
        Optional<UserDetails> optionalUD = authenticationContext.getAuthenticatedUser(UserDetails.class);
        try {
            String username = optionalUD.get().getUsername();
            user = personRepository.findByLogin(username).getUserEntity();
        }catch (Exception e){
            //перенаправляю если user == null
            authenticationContext.logout();
            UI.getCurrent().getPage().setLocation("/login");
        }


        userEditor.setVisible(false);

        //Обновляем личную инфу в content
        updateUserInfoAdmin(user);
        VerticalLayout content = new VerticalLayout(name, email, phone, birthday);
        content.setSpacing(false);
        content.setPadding(false);


        Details details = new Details("Личная информация", content);
        details.setOpened(true);

        //устанавливаем название
        filter.setPlaceholder("Поиск");
        //устанавливаем режим изменения значения "сразу позле ввода"
        filter.setValueChangeMode(ValueChangeMode.EAGER);
        //каждый раз когда пользователь меняет текст вызываем fillList
        filter.addValueChangeListener(field -> fillList(field.getValue()));

        HorizontalLayout toolbar = new HorizontalLayout(details, filter, addNewPersonButton);

        //добавляем все на страницу
        add(toolbar,userEditor,personEditor, employeeGrid );


        //редоктирование  при выборе одного из списка
        employeeGrid
                .asSingleSelect()
                .addValueChangeListener(e -> {
                    personEditor.setVisible(false);
                    userEditor.editUser(e.getValue());
                });
        //Кнопка создания нового пользователя
        addNewPersonButton.addClickListener(e -> {
            userEditor.setVisible(false);
            if (personEditor.isVisible()) {
                personEditor.getCancel().click();
            } else {
                personEditor.editPerson(new Person());
            }
        });
        //Обновление полей после изменений в userEditor
        userEditor.setChangeHandler(() -> {
            userEditor.setVisible(false);
            fillList(filter.getValue());
            UserEntity updatedUser = userEditor.getBinder().getBean();
            updateUserInfoAdmin(updatedUser);
        });
        //обнуляем фильтр
        fillList("");
    }

    protected void updateUserInfoAdmin(UserEntity updatedUser) {
        name.setText(updatedUser.getFirstName() + " " + updatedUser.getLastName() + " " + updatedUser.getMiddleName());
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

