package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UsersDao;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UsersEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
public class UsersBusinessService {

    @Autowired
    private UsersDao usersDao;

    public UsersEntity userProfile(final String userUuid , final String authorizationToken) throws AuthorizationFailedException , UserNotFoundException {
        UserAuthEntity userAuthEntity = usersDao.getUserAuthToken(authorizationToken);
        if(userAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001" , "User has not signed in");
        }


      /**  UserAuthEntity userAuthEntity1 = usersDao.getuserLogoutAtTime(authorizationToken);
        if(userAuthEntity1 == null) {
            //Do Nothing
        } else {
            throw new AuthorizationFailedException("ATHR-002" , "User is signed out.Sign in first to get user details");
        } **/

        UsersEntity usersEntity = usersDao.getUserByUuid(userUuid);
        if(usersEntity == null) {
            throw new UserNotFoundException("USR-001" , "User with entered uuid does not exist");
        }

        return usersEntity;

    }
}
