package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UsersDao;
import com.upgrad.quora.service.entity.UsersEntity;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsersBusinessService {
    @Autowired
    private UsersDao usersDao;

    public UsersEntity userProfile(final String userUuid) throws UserNotFoundException {
        UsersEntity usersEntity =  usersDao.userProfile(userUuid);
        if(usersEntity == null) {
            throw new UserNotFoundException("USR-001" , "User with entered uuid does not exist");

        }
        return usersEntity;

    }
}
