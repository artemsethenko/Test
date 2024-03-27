package com.example.test_task_authorization_to_personal_page.components;

import com.example.test_task_authorization_to_personal_page.entity.Person;
import com.example.test_task_authorization_to_personal_page.entity.Role;
import com.example.test_task_authorization_to_personal_page.entity.UserEntity;
import com.example.test_task_authorization_to_personal_page.repositories.PersonRepository;
import com.example.test_task_authorization_to_personal_page.repositories.UserRepository;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@SpringComponent
@UIScope
public class PersonEditor  extends VerticalLayout implements KeyNotifier {
    private final PasswordEncoder passwordEncoder;
        private  final PersonRepository personRepository;
        private final UserRepository userRepository;
        protected Person person;


        private TextField login = new TextField("Логин");
        private TextField password = new TextField("Пароль");
        private ComboBox<Role> roles = new ComboBox<>("Выберите роль");

        protected Button save = new Button("Save", VaadinIcon.CHECK.create());
        private Button cancel = new Button("Cancel");
        private Button delete = new Button("Delete", VaadinIcon.TRASH.create());
        private HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);
        private Binder<Person> personBinder = new Binder<>(Person.class);
        @Setter
        private ChangeHandler changeHandler;
        public interface ChangeHandler{
            void onChang();
        }
        @Autowired
        public PersonEditor(PasswordEncoder passwordEncoder, PersonRepository personRepository, UserRepository userRepository) {
            this.passwordEncoder = passwordEncoder;
            this.personRepository = personRepository;
            this.userRepository = userRepository;


            personBinder.bind(login, "login");
            personBinder.bind(password, "password");
            personBinder.bind(roles, "roles");
            //Выбор роли
            roles.setItems(Role.ADMIN,Role.USER);

            add(new HorizontalLayout(login, password,roles), actions);

            setSpacing(true);

            save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

            addKeyPressListener(Key.ENTER, e -> save());

            // wire action buttons to save, delete and reset
            save.addClickListener(e -> save());
            delete.addClickListener(e -> delete());
            cancel.addClickListener(e -> editPerson(person));
            cancel.addClickListener(e -> setVisible(false));
            setVisible(false);
            editPerson(null);
        }

        public void editPerson(Person newPerson) {
            if (newPerson == null){
                setVisible(false);
                return;
            }
            if (newPerson.getId() != null){
                person = personRepository.findById(newPerson.getId()).orElse(newPerson);
            }else {
                person = newPerson;
            }
            personBinder.setBean(person);
            setVisible(true);
            login.focus();
        }

        private void delete() {
            personRepository.delete(person);
            changeHandler.onChang();
        }
        private void save() {
            UserEntity user = new UserEntity("","","", LocalDate.of(2000,1,1),"","");
            person.setUserEntity(user);
            person.setPassword(passwordEncoder.encode(person.getPassword()));
            if (person.getRoles()==(null)){
                person.setRoles(Role.USER);
            }
            personRepository.save(person);
            userRepository.save(user);
            setVisible(false);
            changeHandler.onChang();


        }
    }


