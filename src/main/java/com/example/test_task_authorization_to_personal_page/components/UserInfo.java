package com.example.test_task_authorization_to_personal_page.components;

import com.example.test_task_authorization_to_personal_page.entity.UserEntity;
import com.example.test_task_authorization_to_personal_page.repositories.UserRepository;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.Data;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
@Data
public class UserInfo extends HorizontalLayout implements KeyNotifier {

    private final  UserEntity user;
    private final Button addNewButton = new Button("Редактировать");
    private Paragraph lastName = new Paragraph();
    private Paragraph firstName = new Paragraph();
    private Paragraph middleName = new Paragraph();
    private Paragraph birthday = new Paragraph();
    private Paragraph email = new Paragraph();
    private Paragraph phoneNumber = new Paragraph();
    private UserEditor userEditor;
    private final UserRepository userRepository;

    @Setter
    public ChangeHandler changeHandler;
    public interface ChangeHandler{
        void onChang();
    }
@Autowired
    public UserInfo(UserEntity userEntity, UserRepository userRepository) {
        this.user = userEntity;
        this.userRepository = userRepository;

        UserEditor userEditor = new UserEditor(userRepository);

        lastName.setWidth("230px");
        lastName.setHeight("15px");
        lastName.getStyle().set("font-size", "var(--lumo-font-size-m)");


        firstName.setWidth("230px");
        firstName.setHeight("15px");
        firstName.getStyle().set("font-size", "var(--lumo-font-size-m)");


        middleName.setWidth("230px");
        middleName.setHeight("15px");
        middleName.getStyle().set("font-size", "var(--lumo-font-size-m)");


        birthday.setWidth("230px");
        birthday.setHeight("15px");
        birthday.getStyle().set("font-size", "var(--lumo-font-size-m)");


        email.setWidth("230px");
        email.setHeight("15px");
        email.getStyle().set("font-size", "var(--lumo-font-size-m)");


        phoneNumber.setWidth("230px");
        phoneNumber.setHeight("15px");
        phoneNumber.getStyle().set("font-size", "var(--lumo-font-size-m)");

    updateUserInfo(user);


        add(new VerticalLayout(firstName,lastName,middleName,birthday,email,phoneNumber,addNewButton),userEditor);

    addNewButton.addClickListener(e -> userEditor.editUser(user));
    userEditor.editUser(null);


    }
    public void updateUserInfo(UserEntity updatedUser) {
        firstName.setText(  "Имя:            " + updatedUser.getFirstName());
        lastName.setText(   "Фамилия:        " + updatedUser.getLastName());
        middleName.setText( "Отчество:       " + updatedUser.getMiddleName());
        birthday.setText(   "День рождения:  " + updatedUser.getBirthday().toString());
        email.setText(      "Email:          " + updatedUser.getEmail());
        phoneNumber.setText("Номер телефона: " + updatedUser.getPhoneNumber());
    }

}
