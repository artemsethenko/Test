package com.example.test_task_authorization_to_personal_page.repositories;

import com.example.test_task_authorization_to_personal_page.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
public interface PersonRepository extends JpaRepository<Person,Long> {
    Person findByLogin(String login);

}
