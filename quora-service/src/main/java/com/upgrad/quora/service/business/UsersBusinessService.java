package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UsersDao;
import com.upgrad.quora.service.entity.UsersEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsersBusinessService {
    @Autowired
    private UsersDao usersDao;

    public UsersEntity userProfile(final String userUuid) {
        return usersDao.userProfile(userUuid);

    }
}
