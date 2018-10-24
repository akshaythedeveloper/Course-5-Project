package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UsersDao;
import com.upgrad.quora.service.entity.UsersEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class SignupBusinessService {

    @Autowired
    private UsersDao usersDao;

    @Transactional
    public UsersEntity signup(UsersEntity usersEntity) {
        return usersDao.createUser(usersEntity);

    }
}
