package com.example.test_task_authorization_to_personal_page.components;

import com.example.test_task_authorization_to_personal_page.entity.UserEntity;
import com.example.test_task_authorization_to_personal_page.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class UserInfoTest {
    @Mock
    private UserInfo.ChangeHandler changeHandler;
    @Mock
    UserRepository userRepository;


    UserEntity user;
    UserInfo userInfo;
    @BeforeEach
    void setUp(){


        user = new UserEntity("John", "Doe","Joch" ,
                LocalDate.of(1990,2,2),"johndoe@example.com",
                "895556665552");
        userInfo = new UserInfo(user, userRepository);
        userInfo.setChangeHandler(changeHandler);
    }

    @Test
    public void updateUserInfo_shouldUpdateParagraphs() {
        // Создаем пользователя с новыми данными
        UserEntity updatedUser = new UserEntity("Jane", "Smith","Joch" ,
                LocalDate.of(1990,2,2),"johndoe@example.com",
                "895556665552");

        // Обновляем информацию пользователя
        userInfo.updateUserInfo(updatedUser);

        // Проверка обновления текста параграфов
        assertEquals("Имя:            " + updatedUser.getFirstName(), userInfo.getFirstName().getText());
        assertEquals("Фамилия:        " + updatedUser.getLastName(), userInfo.getLastName().getText());
        assertEquals("Отчество:       " + updatedUser.getMiddleName() , userInfo.getMiddleName().getText());
        assertEquals("День рождения:  " + updatedUser.getBirthday().toString(), userInfo.getBirthday().getText());
        assertEquals("Email:          " + updatedUser.getEmail(), userInfo.getEmail().getText());
        assertEquals("Номер телефона: " + updatedUser.getPhoneNumber(), userInfo.getPhoneNumber().getText());
    }

    @Test
    public void addNewButton_shouldCallEditUserInUserEditor() {
        // Нажатие на кнопку "Редактировать"
        userInfo.getEditButton().click();

   }
}