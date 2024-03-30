package com.example.test_task_authorization_to_personal_page.components;

import com.example.test_task_authorization_to_personal_page.entity.Person;
import com.example.test_task_authorization_to_personal_page.entity.Role;
import com.example.test_task_authorization_to_personal_page.entity.UserEntity;
import com.example.test_task_authorization_to_personal_page.repositories.PersonRepository;
import com.example.test_task_authorization_to_personal_page.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PersonEditorTest {


    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Mock
    private PersonRepository personRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PersonEditor.ChangeHandler changeHandler;


    private UserEntity user;
    private UserEntity user2;
    private Person person;
    private Person person2;
    PasswordEncoder passwordEncoder;

    PersonEditor personEditor;


    @BeforeEach
    void setUp() {
        passwordEncoder  = passwordEncoder();
        user = new UserEntity(
                "Artem","Set","Alex",
                LocalDate.of(1992,1,7),"xxxx@mail.ru","89999999899");
        user2 = new UserEntity("Max","Pain","Painov",
                LocalDate.of(1999,2,10),"pain@gmail.com","89505555555");
        person = new Person("user","user", Role.USER,user);
        person2 = new Person("admin","admin", Role.ADMIN,user2);
        personEditor = new PersonEditor(passwordEncoder,personRepository,userRepository);
        personEditor.editPerson(person);
        personEditor.setChangeHandler(changeHandler);
    }



    @Test
    public void save() {

        personEditor.getSave().click();
        boolean isMatch = passwordEncoder.matches("user", person.getPassword());

        verify(personRepository).save(person);
        verify(userRepository).save(any(UserEntity.class));


        assertEquals("user",person.getLogin());
        assertTrue(isMatch);
        assertEquals(Role.USER,person.getRoles());
    }
    @Test
    public void cancel(){
        personEditor.getCancel().click();
        assertFalse(personEditor.isVisible());
    }
    @Test
    public void delete() {
        personEditor.getDelete().click();
        verify(personRepository).delete(person);
    }
        @Test
        public void save_emptyLogin_shouldShowError() {
        person.setLogin("");
        person.setPassword("password");
        personEditor.getSave().click();


            // Проверка отображения ошибки
            // ... (используйте методы проверки фреймворка)

            // Проверка отсутствия вызовов репозиториев
            verify(personRepository, never()).save(any(Person.class));
            verify(userRepository, never()).save(any(UserEntity.class));
        }


    }


