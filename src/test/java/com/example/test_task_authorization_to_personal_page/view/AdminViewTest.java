package com.example.test_task_authorization_to_personal_page.view;
import com.example.test_task_authorization_to_personal_page.entity.Person;
import com.example.test_task_authorization_to_personal_page.entity.Role;
import com.example.test_task_authorization_to_personal_page.entity.UserEntity;
import com.example.test_task_authorization_to_personal_page.repositories.PersonRepository;
import com.example.test_task_authorization_to_personal_page.repositories.UserRepository;
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
import java.util.*;

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

    private AdminView adminView;
    private  Person person;
    private UserEntity userOne;
    private UserEntity userTwo;

    @BeforeEach
    void setUp() {
        PasswordEncoder passwordEncoder = passwordEncoder();
        LocalDate lD = LocalDate.of(1990, 2, 2);


        userOne = new UserEntity("John", "Doe", "Joch",lD, "johndoe@example.com", "895556665552");
        userTwo = new UserEntity("Abram", "Max", "Joch",lD, "johndoe@example.com", "895556665552");

        person = new Person("user","user", Role.ADMIN, userOne);

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add( new SimpleGrantedAuthority("ROLE_"+person.getRoles()));
        Optional<UserDetails> auth = Optional.of(new User(person.getLogin(), person.getPassword(), authorities));

        when(userRepository.findAll()).thenReturn(Arrays.asList(userOne,userTwo));


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

    @Test
    public void searchByUsername() {
        when(userRepository.findByName("John")).thenReturn(Collections.singletonList(userOne));
        when(userRepository.findByName("Abram")).thenReturn(Collections.singletonList(userTwo));

        adminView.fillList(""); // Поиск без имени
        assertEquals(2, adminView.getEmployeeGrid().getListDataView().getItemCount());

        adminView.fillList("John"); // Поиск по имени "John"
        assertEquals(1, adminView.getEmployeeGrid().getListDataView().getItemCount());
        assertEquals(userOne, adminView.getEmployeeGrid().getGenericDataView().getItems().findFirst().get());

        adminView.fillList("Abram"); // Поиск по имени "Jane"
        assertEquals(1, adminView.getEmployeeGrid().getListDataView().getItemCount());
        assertEquals(userTwo, adminView.getEmployeeGrid().getGenericDataView().getItems().findFirst().get());
    }

}