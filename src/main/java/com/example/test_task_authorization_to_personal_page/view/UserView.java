package com.example.test_task_authorization_to_personal_page.view;

import com.example.test_task_authorization_to_personal_page.components.UserEditor;
import com.example.test_task_authorization_to_personal_page.components.UserInfo;
import com.example.test_task_authorization_to_personal_page.entity.UserEntity;
import com.example.test_task_authorization_to_personal_page.repositories.PersonRepository;
import com.example.test_task_authorization_to_personal_page.repositories.UserRepository;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.spring.security.AuthenticationContext;
import jakarta.annotation.security.PermitAll;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@PageTitle("UserPage")
@Route(value = "user", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@PermitAll
@Uses(Icon.class)
@Getter
public class UserView extends VerticalLayout {
    private final UserRepository userRepository;
    private final UserInfo userInfo;
    private UserEntity user;
    private Upload upload;
    private final MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
    private final VerticalLayout forImage = new VerticalLayout();
    private UserEditor userEditor;

    @Autowired

    public UserView(UserRepository userRepository, PersonRepository personRepository, AuthenticationContext authenticationContext) {
        this.userRepository = userRepository;

        //Получаю доступ к текушему пользователю
        Optional<UserDetails> optionalUD = authenticationContext.getAuthenticatedUser(UserDetails.class);
        try {
            String username = optionalUD.get().getUsername();
            user = personRepository.findByLogin(username).getUserEntity();
        } catch (Exception e) {
            //перенаправляю если user == null
            authenticationContext.logout();
            UI.getCurrent().getPage().setLocation("/login");
        }

        //Создаем поля с информацией о пользователе
        userInfo = new UserInfo(user, userRepository);
        userEditor = userInfo.getUserEditor();

        //Место под фото
        forImage.setHeight("400px");
        forImage.setWidth("350px");

        //Проверяю если фото у пользователя и встовляю если есть
        updateImage(user);
        //Создание кнопки для загрузки фото и сохранение в базу
        upload = addEvent(user);
        //  Добавляем все на страницу
        add(new HorizontalLayout(forImage, userInfo), upload);
        //Сработает когда будет вызван changeHandler.onChang() в userEditor
        userEditor.setChangeHandler(() -> {
            // Используем binder для получения обновленного объекта
            UserEntity updatedUser = userEditor.getBinder().getBean();
            //  Обновить поля UserInfo с данными updatedUser
            userInfo.updateUserInfo(updatedUser);
            updateImage(updatedUser);
        });
    }

    //Логика сохранения картинки
    public Upload addEvent(UserEntity user) {
        Upload upload = new Upload(buffer);
        upload.addSucceededListener(event -> {
            String fileName = event.getFileName();
            InputStream inputStream = buffer.getInputStream(fileName);
            try {
                BufferedImage image = ImageIO.read(inputStream);
                ImageIO.write(image, "jpg", new File("src/main/webapp/images/" + user.getId()+ fileName ));
                //удаляем фото если оно есть
                if (user.getPhotoUrl() != null){
                     new File(user.getPhotoUrl()).delete();
                }
                //сохраняем новое
                user.setPhotoUrl("images/" + user.getId() + fileName);
                userRepository.save(user);
                updateImage(user);
            } catch (IOException e) {
                throw new RuntimeException("Ошибка загрузки изображения: " + e.getMessage());
            }
        });
        return upload;
    }
    public void updateImage(UserEntity user) {
        userInfo.getEditButton().click();
        userInfo.getEditButton().click();
        if (user.getPhotoUrl() != null) {
            Image image1 = new Image(user.getPhotoUrl(), "An example");
            image1.setWidth("300px");
            forImage.removeAll();
            forImage.add(image1);
        }else {
            forImage.removeAll();
        }
    }
}


