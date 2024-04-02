package com.example.test_task_authorization_to_personal_page.security;

import com.example.test_task_authorization_to_personal_page.entity.Person;
import com.example.test_task_authorization_to_personal_page.repositories.PersonRepository;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.spring.security.AuthenticationContext;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SecurityService {
    private final PersonRepository personRepository;
    private final AuthenticationContext authenticationContext;



    public SecurityService(AuthenticationContext authenticationContext, PersonRepository personRepository) {
        this.personRepository = personRepository;
        this.authenticationContext = authenticationContext;
    }

    @Transactional
    //Получение информации о person из базы данных для проверки пользователей при входе
    public Optional<Person> get() {
        return authenticationContext.getAuthenticatedUser(UserDetails.class)
                .map(userDetails -> personRepository.findByLogin(userDetails.getUsername()));
    }

//выход из системы
    public void logout() {
        UI.getCurrent().getPage().setLocation("/login");
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(
                VaadinServletRequest.getCurrent().getHttpServletRequest(), null,
                null);
    }
}

