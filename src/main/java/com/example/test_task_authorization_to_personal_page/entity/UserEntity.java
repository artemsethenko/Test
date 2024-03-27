package com.example.test_task_authorization_to_personal_page.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "users")
@Data
public class UserEntity {
    public UserEntity() {

    }

    public UserEntity(String lastName, String firstName, String middleName, LocalDate birthday, String email, String phoneNumber) {

        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.birthday = birthday;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "birthday")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "UTC")
    private LocalDate birthday;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;


    @Column(name = "photo")
    private String photoUrl;
    @JoinColumn(name = "person_id")
     @OneToOne(mappedBy = "userEntity", cascade = CascadeType.ALL)
     private Person person;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                '}';
    }
}