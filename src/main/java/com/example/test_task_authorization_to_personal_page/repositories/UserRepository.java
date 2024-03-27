package com.example.test_task_authorization_to_personal_page.repositories;

import com.example.test_task_authorization_to_personal_page.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    @Query("from UserEntity u  " +
            "where concat(u.lastName, ' ' , u.firstName, ' ' , u.middleName)" +
            "like concat ('%', :name, '%') ")
    List<UserEntity> findByName(@Param("name")String name);

}
