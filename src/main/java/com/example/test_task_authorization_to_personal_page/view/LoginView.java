package com.example.test_task_authorization_to_personal_page.view;

import com.example.test_task_authorization_to_personal_page.entity.Person;
import com.example.test_task_authorization_to_personal_page.entity.Role;
import com.example.test_task_authorization_to_personal_page.entity.UserEntity;
import com.example.test_task_authorization_to_personal_page.repositories.PersonRepository;
import com.example.test_task_authorization_to_personal_page.security.SecurityService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.internal.RouteUtil;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.LocalDate;
@AnonymousAllowed//Любой пользователь может посетить на эту страницу
@PageTitle("Login")//название страницы
@Route(value = "login")//адрес страницы
public class LoginView extends LoginOverlay implements BeforeEnterObserver {

    private final SecurityService securityService;

    public LoginView(SecurityService securityService, PersonRepository personRepository, PasswordEncoder passwordEncoder) {
        this.securityService = securityService;

        //Создание админа если его нет в базе данных
        if (personRepository.findAll().stream().noneMatch(e -> e.getRoles().equals(Role.ADMIN))){
            personRepository.save(new Person("admin",passwordEncoder.encode("admin"),Role.ADMIN,new UserEntity("","","", LocalDate.of(2000,1,1),"","")));
        }

        setAction(RouteUtil.getRoutePath(VaadinService.getCurrent().getContext(), getClass()));
        //Созданее дефолтного логин меню
        LoginI18n i18n = LoginI18n.createDefault();
        i18n.setHeader(new LoginI18n.Header());
        i18n.getHeader().setTitle("TestVaadin");
        i18n.getHeader().setDescription("Login  admin/admin");
        i18n.setAdditionalInformation(null);
        i18n.getForm().setSubmit("Вход");

        //задал новую логику ForgotPassword - переход на другую страницу
        addForgotPasswordListener(forgotPasswordEvent -> UI.getCurrent().getPage().setLocation("/registration"));
        i18n.getForm().setForgotPassword("Регистрация");
        setForgotPasswordButtonVisible(true);

        setI18n(i18n);

        setOpened(true);
    }

    //Если пользователь уже прошел проверку его перенаправляют "" , форма входа скрыта
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (securityService.get().isPresent()) {
            setOpened(false);
            event.forwardTo("");
        }
        setError(event.getLocation().getQueryParameters().getParameters().containsKey("error"));
    }
}



