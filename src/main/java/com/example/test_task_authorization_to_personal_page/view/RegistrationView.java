package com.example.test_task_authorization_to_personal_page.view;

import com.example.test_task_authorization_to_personal_page.entity.Person;
import com.example.test_task_authorization_to_personal_page.entity.Role;
import com.example.test_task_authorization_to_personal_page.entity.UserEntity;
import com.example.test_task_authorization_to_personal_page.repositories.PersonRepository;
import com.example.test_task_authorization_to_personal_page.security.SecurityConfig;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.component.html.H2;

import java.time.LocalDate;

@Route("/registration")
@AnonymousAllowed
public class RegistrationView extends Composite {
    private final PersonRepository personRepository;

    public RegistrationView(PersonRepository personRepository) {
        this.personRepository = personRepository;

    }

    @Override
    protected Component initContent() {
        TextField username = new TextField("Username");
        PasswordField password1 = new PasswordField("Password");
        PasswordField password2 = new PasswordField("Confirm password");
        //кнопка перехода на страницу для входа
        Button logInButton = new Button("Войти");
        logInButton.addClickListener(e -> UI.getCurrent().getPage().setLocation("/login"));
        //кнопка для отправки данных на регистрацию
        Button sendButton = new Button("Зарегистрироваться");
        sendButton.addClickListener(e -> register(username.getValue(),password1.getValue(),password2.getValue()));

        //Собираем все на странице
        return new VerticalLayout(
                new H2("Регистрация"),username,password1,password2,
                new HorizontalLayout(sendButton,logInButton)
        );
    }
    //Поведение при разных сценариях заполнения
    private void register(String username, String password1, String password2) {
        if (username.trim().isEmpty()) {
            Notification.show("Введите имя");
        } else if (password1.isEmpty()) {
            Notification.show("Введите пароль");
        } else if (!password1.equals(password2)) {
            Notification.show("Пороли не совпадают");
        } else {
            //кодирую пароль
            String pass = new SecurityConfig().passwordEncoder().encode(password1);
            //сохраняю пользователя
            personRepository.save(new Person(username,
                    pass,
                    Role.USER,
                    new UserEntity("", "", "",
                            LocalDate.of(2000, 1, 1),
                            "", "")));

            Notification.show("Вы зарегестрированны");
        }
    }
}

