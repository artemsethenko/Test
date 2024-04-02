package com.example.test_task_authorization_to_personal_page.view;
import com.example.test_task_authorization_to_personal_page.components.PersonEditor;
import com.example.test_task_authorization_to_personal_page.components.UserEditor;
import com.example.test_task_authorization_to_personal_page.entity.Person;
import com.example.test_task_authorization_to_personal_page.entity.Role;
import com.example.test_task_authorization_to_personal_page.entity.UserEntity;
import com.example.test_task_authorization_to_personal_page.repositories.PersonRepository;
import com.example.test_task_authorization_to_personal_page.repositories.UserRepository;
import com.vaadin.flow.component.html.Image;
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

import static com.github.mvysny.kaributesting.v10.LocatorJ._get;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
public class UserViewTest {
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

    private UserView userView;
    private Person person;
    private UserEntity userOne;
    private UserEntity userTwo;


    @BeforeEach
    public void setup() {
        passwordEncoder = passwordEncoder();
        LocalDate lD = LocalDate.of(1990, 2, 2);


        userOne = new UserEntity("John", "Doe", "Joch", lD, "johndoe@example.com", "895556665552");
        userTwo = new UserEntity("Abram", "Mix", "Joch", lD, "johndoe@example.com", "895556665552");

        person = new Person("user", "user", Role.ADMIN, userOne);

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + person.getRoles()));
        Optional<UserDetails> auth = Optional.of(new User(person.getLogin(), person.getPassword(), authorities));

        List<UserEntity> userList = new ArrayList<>();
        List<UserEntity> userListOnlyOne = new ArrayList<>();
        userList.add(userOne);
        userList.add(userTwo);
        userListOnlyOne.add(userOne);


//        when(userRepository.findAll()).thenReturn(Arrays.asList(userOne, userTwo));
//
//        when(userRepository.findByName("John")).thenReturn(Collections.singletonList(userOne));
//        when(userRepository.findByName("Abram")).thenReturn(Collections.singletonList(userOne));


        when(authenticationContext.getAuthenticatedUser(UserDetails.class)).thenReturn(auth);

        when(personRepository.findByLogin(person.getLogin())).thenReturn(person);

        userView = new UserView(userRepository, personRepository, authenticationContext);
    }

    @Test
    public void testElementUserView() {
        assertNotNull(userView.getUpload());
        assertNotNull(userView.getUserInfo());
        assertNotNull(userView.getUserEditor());

        assertTrue(userView.getUpload().isVisible());
        assertTrue(userView.getUserInfo().isVisible());
        assertFalse(userView.getUserEditor().isVisible());
    }

    @Test
    public void testUpdateImage() {


        userOne.setPhotoUrl("src/main/webapp/images/1One.jpg");
        userView.updateImage(userOne);

        Image expectedImage = (Image) userView.getForImage().getChildren().findFirst().get();
        assertEquals(expectedImage.getSrc(), userOne.getPhotoUrl());


        userView.updateImage(userTwo);
        Image expectedImageTwo;
        try {
            expectedImageTwo = (Image) userView.getForImage().getChildren().findFirst().get();
            assertEquals(expectedImageTwo.getSrc(), userTwo.getPhotoUrl());
        } catch (Exception e) {
            expectedImageTwo = null;
            assertEquals(expectedImageTwo, userTwo.getPhotoUrl());
        }
    }
}