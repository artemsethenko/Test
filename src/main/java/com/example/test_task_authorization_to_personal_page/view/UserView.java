package com.example.test_task_authorization_to_personal_page.view;

import com.example.test_task_authorization_to_personal_page.components.UserEditor;
import com.example.test_task_authorization_to_personal_page.components.UserInfo;
import com.example.test_task_authorization_to_personal_page.entity.UserEntity;
import com.example.test_task_authorization_to_personal_page.repositories.PersonRepository;
import com.example.test_task_authorization_to_personal_page.repositories.UserRepository;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@PageTitle("UserPage")
@Route(value = "user", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@PermitAll
@Uses(Icon.class)


public class UserView extends VerticalLayout {
    private final UserRepository userRepository;
    private  UserInfo userInfo ;

    private final  MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
    private VerticalLayout forImage = new VerticalLayout();

    @Autowired

    public UserView(UserRepository userRepository, PersonRepository personRepository, AuthenticationContext authenticationContext) {
        this.userRepository = userRepository;
        UserEditor userEditor =new UserEditor(userRepository);
        //Получаю доступ к текушего пользователя
        UserDetails userDetails = authenticationContext.getAuthenticatedUser(UserDetails.class).get();
        String username = userDetails.getUsername();
        UserEntity user = personRepository.findByLogin(username).getUserEntity();

        //Создаем поля с информацией о пользователе
        userInfo = new UserInfo(user, userRepository);

        //Место под фото

        forImage.setHeight("400px");
        forImage.setWidth("350px");

//Проверяю если фото у пользователя и встовляю если есть
        updateImage(user);
 //Создание кнопки для загрузки фото и сохранение в базу
        Upload upload = addEvent(user);
     //   forImage.add(new VerticalLayout(upload) );



      //  Добавляем все на страницу
        add(new HorizontalLayout(forImage,userInfo),upload);
        userInfo.setChangeHandler(() ->{
            UserEntity updatedUser = userEditor.getBinder().getBean();
            userInfo.updateUserInfo(updatedUser);});
        userEditor.setChangeHandler(() -> {
            userEditor.setVisible(false);
            // Получить обновленные данные пользователя из userEditor
           // UserEntity updatedUser = userEditor.getBinder().getBean();  // Используем binder для получения обновленного объекта

            // Обновить поля UserInfo с данными updatedUser
           // userInfo.updateUserInfo(updatedUser);

        });


    }
    //Логика сохранения картинки
    public Upload addEvent (UserEntity user){
        Upload upload = new Upload(buffer);
        upload.addSucceededListener(event -> {
            String fileName = event.getFileName();
            InputStream inputStream = buffer.getInputStream(fileName);
            try {
                BufferedImage image = ImageIO.read(inputStream);
                ImageIO.write(image,"jpg",new File("src/main/webapp/images/"+ fileName));
                user.setPhotoUrl("images/"+ fileName);
                userRepository.save(user);
                updateImage(user);
            } catch (IOException e) {
                throw new RuntimeException("Ошибка загрузки изображения: " + e.getMessage());
            }
        });
        return upload;
    }
    public void updateImage(UserEntity user) {
        if(user.getPhotoUrl()!=null) {
            Image image1 = new Image(user.getPhotoUrl(), "An example");
            image1.setWidth("300px");
            forImage.removeAll();
            forImage.add(image1);
        }
    }
}


