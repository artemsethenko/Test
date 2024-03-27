package com.example.test_task_authorization_to_personal_page.components;

import com.example.test_task_authorization_to_personal_page.entity.Person;
import com.example.test_task_authorization_to_personal_page.entity.Role;
import com.example.test_task_authorization_to_personal_page.repositories.PersonRepository;
import com.example.test_task_authorization_to_personal_page.repositories.UserRepository;
import com.vaadin.flow.component.button.Button;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonEditorTest {

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private PersonRepository personRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PersonEditor.ChangeHandler changeHandler;

    @InjectMocks
    private PersonEditor personEditor;

//    @Test
//    void editPerson_withExistingPerson_setsBeanAndFields() {
//        Person existingPerson = new Person("testLogin", "testPassword", Role.USER, new UserEntity());
//
//        personEditor.editPerson(existingPerson);
//        Button saveButton = personEditor.save;
//        saveButton.click();
//
//        Person person = personRepository.findByLogin(existingPerson.getLogin());
//        assertEquals(false, personEditor.isVisible());
//        assertEquals("testLogin",person.getLogin());
//        assertEquals(passwordEncoder.encode("testPassword"),person.getPassword());
//        assertEquals((Role.USER), person.getRoles());
//    }

    // ... tests for saving, deleting, canceling, and key press events

    @Test
    void save_newPerson_encodesPasswordAndSavesToRepositories() {
        Person newPerson = new Person("testLogin", "testPassword", Role.ADMIN, null);
        personEditor.editPerson(newPerson);

        personEditor.editPerson(newPerson);
        Button saveButton = personEditor.save;
        saveButton.click();

        verify(passwordEncoder).encode("testPassword");
        verify(personRepository).save(personEditor.person);
        verify(userRepository).save(personEditor.person.getUserEntity());
        assertFalse(personEditor.isVisible());
        verify(changeHandler).onChang();
    }
}
