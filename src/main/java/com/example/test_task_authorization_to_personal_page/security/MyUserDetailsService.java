package com.example.test_task_authorization_to_personal_page.security;

import com.example.test_task_authorization_to_personal_page.entity.Person;
import com.example.test_task_authorization_to_personal_page.repositories.PersonRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
@Service
public class MyUserDetailsService implements UserDetailsService {

    PersonRepository personRepository;

    public MyUserDetailsService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }
    @Override
    @Transactional
    //Логика как сохранять данные в UserDetails
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Person person = personRepository.findByLogin(login);
        if (person == null) {
            throw new UsernameNotFoundException("No person present with username: " + login);
        } else {
            return new User(person.getLogin(), person.getPassword(),
                    getAuthorities(person));
        }
    }

    private static List<GrantedAuthority> getAuthorities(Person person) {

       List<GrantedAuthority> authorities = new ArrayList<>();
       authorities.add( new SimpleGrantedAuthority("ROLE_"+person.getRoles()));
        return authorities ;
    }
}
