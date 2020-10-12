package org.example.fileshibernate.dao;

import org.example.fileshibernate.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends CrudRepository<User, Long> {
    User findByUsername(String username);
    User findBySecret(String secret);
}
