package org.example.fileshibernate.service;

import org.example.fileshibernate.model.Role;

public interface RoleService {
    Iterable<Role> findAll();
}
