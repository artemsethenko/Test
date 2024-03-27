package com.example.test_task_authorization_to_personal_page.components;

import com.example.test_task_authorization_to_personal_page.entity.UserEntity;
import com.example.test_task_authorization_to_personal_page.repositories.UserRepository;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
@SpringComponent
@UIScope
public class UserEditor extends VerticalLayout implements KeyNotifier {
    private  final UserRepository userRepository;
    private UserEntity userEntity;
    private final TextField firstName = new TextField("First name");
    private final TextField lastName = new TextField("Last name");
    private final TextField middleName = new TextField("Middle name");
    private final EmailField email = new EmailField("email");
    private final DatePicker birthdayCom = new DatePicker("Select a date:");
    private final DatePicker.DatePickerI18n birthday = new DatePicker.DatePickerI18n();
    private final TextField phoneNumber = new TextField("phone Number");
   private Button save = new Button("Save", VaadinIcon.CHECK.create());
   private Button cancel = new Button("Cancel");
   private Button delete = new Button("Delete", VaadinIcon.TRASH.create());
   private HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);
   @Getter
   private Binder<UserEntity> binder = new Binder<>(UserEntity.class);
   @Setter
   private ChangeHandler changeHandler;
   public interface ChangeHandler{
       void onChang();
   }
@Autowired
    public UserEditor(UserRepository userRepository) {
        this.userRepository = userRepository;
    birthday.setDateFormat("yyyy-MM-dd");
    birthdayCom.setI18n(birthday);

        add(new HorizontalLayout(firstName,lastName,middleName),new HorizontalLayout( email,birthdayCom,phoneNumber), actions);

        binder.bindInstanceFields(this);

    setSpacing(true);

    save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

    addKeyPressListener(Key.ENTER, e -> save());
    birthdayCom.addValueChangeListener(changeEvent ->
        userEntity.setBirthday(changeEvent.getValue()));
    // wire action buttons to save, delete and reset
    save.addClickListener(e -> save());
    delete.addClickListener(e -> delete());
    cancel.addClickListener(e -> editUser(userEntity));
    cancel.addClickListener(e -> setVisible(false));
    setVisible(false);
    editUser(null);
}

    public void editUser(UserEntity newUserEntity) {
       if (newUserEntity == null){
           setVisible(false);
           return;
       }
       if (newUserEntity.getId() != null){
           userEntity = userRepository.findById(newUserEntity.getId()).orElse(newUserEntity);
       }else {
           userEntity = newUserEntity;
       }
       binder.setBean(userEntity);
       setVisible(true);
       lastName.focus();
    }

    private void delete() {
       userRepository.delete(userEntity);
       changeHandler.onChang();
    }
    private void save() {
       userRepository.save(userEntity);
       setVisible(false);
       changeHandler.onChang();


    }
}

