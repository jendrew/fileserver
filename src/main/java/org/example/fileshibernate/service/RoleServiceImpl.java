package org.example.fileshibernate.service;

import org.example.fileshibernate.dao.RoleDao;
import org.example.fileshibernate.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleDao roleDao;


    @Override
    public Iterable<Role> findAll() {
        return roleDao.findAll();
    }
}
