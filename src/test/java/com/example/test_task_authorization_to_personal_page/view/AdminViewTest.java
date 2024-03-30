package com.example.test_task_authorization_to_personal_page.view;
import com.example.test_task_authorization_to_personal_page.entity.Person;
import com.example.test_task_authorization_to_personal_page.entity.Role;
import com.example.test_task_authorization_to_personal_page.entity.UserEntity;
import com.example.test_task_authorization_to_personal_page.repositories.PersonRepository;
import com.example.test_task_authorization_to_personal_page.repositories.UserRepository;
import com.example.test_task_authorization_to_personal_page.components.PersonEditor;
import com.example.test_task_authorization_to_personal_page.components.UserEditor;
import com.vaadin.flow.spring.security.AuthenticationContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminViewTest {
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Mock
    private PersonRepository personRepository;
    @Mock
    private AuthenticationContext authenticationContext;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PersonEditor personEditor;
    @Mock
    private UserEditor userEditor;
    PasswordEncoder passwordEncoder;

    private AdminView adminView;
    private  Person person;
    private UserEntity userOne;
    private UserEntity userTwo;

    @BeforeEach
    void setUp() {
        passwordEncoder = passwordEncoder();
        LocalDate lD = LocalDate.of(1990, 2, 2);


        userOne = new UserEntity("John", "Doe", "Joch",lD, "johndoe@example.com", "895556665552");
        userTwo = new UserEntity("Abram", "Mix", "Joch",lD, "johndoe@example.com", "895556665552");

        person = new Person("user","user", Role.ADMIN, userOne);

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add( new SimpleGrantedAuthority("ROLE_"+person.getRoles()));
        Optional<UserDetails> auth = Optional.of(new User(person.getLogin(), person.getPassword(), authorities));

        List<UserEntity> userList = new ArrayList<>( );
        List<UserEntity> userListOnlyOne = new ArrayList<>( );
        userList.add(userOne);
        userList.add(userTwo);
        userListOnlyOne.add(userOne);


        when(userRepository.findAll()).thenReturn(userList);

        when(userRepository.findByName("John")).thenReturn(userListOnlyOne);




        when(authenticationContext.getAuthenticatedUser(UserDetails.class)).thenReturn(auth);

        when(personRepository.findByLogin(person.getLogin())).thenReturn(person);

        adminView = new AdminView(passwordEncoder, personRepository, authenticationContext, userRepository);
    }

    @Test
    public void initialView() {
        // Проверяем, что грида пользователей отображается, а редакторы пользователей скрыты
        assertTrue(adminView.getEmployeeGrid().isVisible());
        assertFalse(adminView.getUserEditor().isVisible());
        assertFalse(adminView.getPersonEditor().isVisible());

        // Проверяем, что личная информация отображается корректно
        adminView.updateUserInfoAdmin(userOne);
        assertEquals("Doe John Joch", adminView.getName().getText());
        assertEquals("johndoe@example.com", adminView.getEmail().getText());
        assertEquals("895556665552", adminView.getPhone().getText());
        assertEquals("1990-02-02", adminView.getBirthday().getText());
    }

//    @Test
//    public void searchByUsername() {
//        UserEntity user1 = new UserEntity("John", "Doe", "Joch", LocalDate.of(1990, 2, 2), "johndoe@example.com", "895556665552");
//        UserEntity user2 = new UserEntity("Jane", "Smith", "Joch", LocalDate.of(1991, 3, 3), "janesmith@example.com", "894445554442");
//        when(userRepository.findAll()).thenReturn(Collections.emptyList());
//        when(userRepository.findByName("John")).thenReturn(Collections.singletonList(user1));
//        when(userRepository.findByName("Jane")).thenReturn(Collections.singletonList(user2));
//
//        adminView.fillList(""); // Поиск без имени
//        //assertEquals(2, adminView.getEmployeeGrid().si);
//
//        adminView.fillList("John"); // Поиск по имени "John"
//        assertEquals(1, adminView.getEmployeeGrid().getPageSize());
//        assertEquals(user1, adminView.getEmployeeGrid().getGenericDataView().getItems());
//
//        adminView.fillList("Jane"); // Поиск по имени "Jane"
//        assertEquals(1, adminView.getEmployeeGrid().getPageSize());
//        assertEquals(user2, adminView.getEmployeeGrid().getGenericDataView().getItems());
//    }

    // ... добавьте тесты для редактирования пользователей, создания новых пользователей и других функций
}