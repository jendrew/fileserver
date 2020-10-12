package org.example.fileshibernate.dao;

import org.example.fileshibernate.model.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleDao extends CrudRepository<Role, Long> {
}
