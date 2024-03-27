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
            Button logInButton = new Button("Log in");
        logInButton.addClickListener(e -> UI.getCurrent().getPage().setLocation("/login"));
            return new VerticalLayout(
                    new H2("Register"),
                    username,
                    password1,
                    password2,
                    new Button("Send", event -> register(
                            username.getValue(),
                            password1.getValue(),
                            password2.getValue()
                    )),logInButton

            );
        }

        private void register(String username, String password1, String password2) {
            if (username.trim().isEmpty()) {
                Notification.show("Enter a username");
            } else if (password1.isEmpty()) {
                Notification.show("Enter a password");
            } else if (!password1.equals(password2)) {
                Notification.show("Passwords don't match");
            } else {

                String pass = new SecurityConfig().passwordEncoder().encode(password1);

                personRepository.save(new Person(username,
                        pass,
                        Role.USER,
                        new UserEntity("","","", LocalDate.of(2000,1,1),"","")));

                Notification.show("Check your email.");
            }
        }
    }

