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
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
@Getter
public class UserInfo extends HorizontalLayout implements KeyNotifier {

    private final  UserEntity user;
    private final Button editButton = new Button("Редактировать");
    private final Paragraph lastName = new Paragraph();
    private final Paragraph firstName = new Paragraph();
    private final Paragraph middleName = new Paragraph();
    private final Paragraph birthday = new Paragraph();
    private final Paragraph email = new Paragraph();
    private final Paragraph phoneNumber = new Paragraph();
    private final UserEditor userEditor;
    private final UserRepository userRepository;


@Autowired
    public UserInfo(UserEntity userEntity, UserRepository userRepository) {
        this.userRepository = userRepository;
        userEditor = new UserEditor(userRepository);
        user = userRepository.findById(userEntity.getId()).orElse(userEntity);

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
    add(new VerticalLayout(firstName,lastName,middleName,birthday,email,phoneNumber, editButton),userEditor);


    editButton.addClickListener(e -> {
        if (userEditor.isVisible()){
            userEditor.setVisible(false);
        }else {
        userEditor.editUser(user);
        }
    });
}
    public void updateUserInfo(UserEntity updatedUser) {
        firstName.setText(  "Имя:            " + updatedUser.getFirstName());
        lastName.setText(   "Фамилия:        " + updatedUser.getLastName());
        middleName.setText( "Отчество:       " + updatedUser.getMiddleName());
        birthday.setText(   "День рождения:  " + updatedUser.getBirthday().toString());
        email.setText(      "Email:          " + updatedUser.getEmail());
        phoneNumber.setText("Номер телефона: " + updatedUser.getPhoneNumber());
    }

    @Setter
    public ChangeHandler changeHandler;
    public interface ChangeHandler{
        void onChang();
    }

}
