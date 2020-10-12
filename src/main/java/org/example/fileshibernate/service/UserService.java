package org.example.fileshibernate.service;

import org.example.fileshibernate.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    Iterable<User> findAll();
    User findOne(Long id);
    User findByUsername(String username);
    void save(User task);
    User findBySecret(String secret);
    void delete(User user);


}
