package com.example.examplehotelreservations.service;

import com.example.examplehotelreservations.web.model.Role;
import com.example.examplehotelreservations.web.model.User;
import java.util.List;

public interface UserService {

    User create(User user, Role role);

    User update(Long id, User user);

    User findByUsername(String username);

    User findById(Long id);

    List<User> findAll();

    void delete(Long id);
}

