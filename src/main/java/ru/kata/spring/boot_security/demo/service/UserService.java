package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService extends UserDetailsService {
    boolean addRole(Role role);
    Role findByNameRole(String name);
    List<Role> listRoles();
    Role findByIdRole(Long id);
    List<Role> listByRole(List<String> name);
    boolean add(User user);
    List<User> listUsers();
    void delete(Long id);
    void update(User us);
    User findById(Long id);
    User findByUsername(String userName);
}
