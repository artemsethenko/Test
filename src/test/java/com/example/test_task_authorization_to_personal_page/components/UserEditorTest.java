package com.example.test_task_authorization_to_personal_page.components;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.test_task_authorization_to_personal_page.entity.UserEntity;
import com.example.test_task_authorization_to_personal_page.repositories.UserRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserEditorTest {
    @Mock
    private UserEditor.ChangeHandler changeHandler;

    @Mock
    private UserRepository userRepository;
    UserEntity existingUser;
    UserEditor editor;
    @BeforeEach
    void setUp(){
        editor = new UserEditor(userRepository);
         existingUser = new UserEntity("John", "Doe","Joch" ,
                LocalDate.of(1990,2,2),"johndoe@example.com",
                "895556665552");
        editor.setChangeHandler(changeHandler);
    }
    @Test
    public void editingExistingUserAndSaving() {

        existingUser.setId(1L);

        editor.editUser(existingUser);

        // Изменить данные пользователя
        editor.getFirstName().setValue("Jane");
        editor.getLastName().setValue("Smith");

        // Сохранить изменения
        editor.getSave().click();

        verify(userRepository, times(1)).save(existingUser);
        assertEquals("Jane", existingUser.getFirstName());
        assertEquals("Smith", existingUser.getLastName());
    }


    @Test
    public void deletingUser() {

        existingUser.setId(1L);

        editor.editUser(existingUser);

        // Удалить пользователя
        editor.getDelete().click();

        verify(userRepository, times(1)).delete(existingUser);
    }

    @Test
    public void cancellingEditing() {

        existingUser.setId(1L);

        editor.editUser(existingUser);

        // Внести изменения в данные
        editor.getFirstName().setValue("Jane");

        // Нажать кнопку отмены
        editor.getCancel().click();

        // Пользователь не должен быть сохранен
        verify(userRepository, never()).save(any(UserEntity.class));

    }
}