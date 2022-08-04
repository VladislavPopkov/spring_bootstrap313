package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.model.Role;

import java.util.List;

public interface RolDaoInterf {
    Role findByIdRole(Long id);
    List<Role> listRoles();
    Role findByName(String name);
    List<Role> listByName(List<String> name);
}