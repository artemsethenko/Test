package com.example.test_task_authorization_to_personal_page.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Objects;

@Entity
@Data
@Table(name = "person")
public class Person {
    public Person() {}
    public Person(String login, String password, Role roles, UserEntity userEntity) {
        this.login = login;
        this.password = password;
        this.roles = roles;
        this.userEntity = userEntity;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login", unique = true , nullable = false)
    private String login;

    @Column(name = "password")
    private String password;
    @Enumerated(EnumType.STRING)
    @Column(name = "roles", nullable = false)
    private Role roles;
    @JoinColumn(name = "user_id")
    @OneToOne(cascade = CascadeType.ALL)
    private UserEntity userEntity;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return id.equals(person.id) && login.equals(person.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login);
    }
    @Override
    public String toString() {
        return login + " " + roles;
    }


}

