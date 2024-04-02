package com.example.test_task_authorization_to_personal_page.security;

import com.example.test_task_authorization_to_personal_page.view.LoginView;
import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
@Configuration
@EnableWebSecurity
public class SecurityConfig extends VaadinWebSecurity {
    @Bean
    //Создаем шифровщик поролей
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //Настройка доступа всем можно выполнять Get запросы по адресу "/images/*.png"
        http.authorizeHttpRequests(auth ->
                auth.requestMatchers(
                        AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/images/*.png")).permitAll());
        //вызываю configure из радительского класса, который выполняет дополнительную настройку параметров безопасности.
        super.configure(http);
//Устанавливаю LoginView как представление для входа в систему.
        setLoginView(http, LoginView.class);
    }
}

