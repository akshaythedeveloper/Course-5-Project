package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UsersDao;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UsersEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminBusinessService {

    @Autowired
    private UsersDao usersDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public UsersEntity userDelete(final String uuid  , final String authorizationToken) throws AuthorizationFailedException , UserNotFoundException {
        UserAuthEntity userAuthEntity = usersDao.getUserAuthToken(authorizationToken);
        if(userAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001" , "User has not signed in");
        }

        UsersEntity usersEntity = new UsersEntity();
        if(usersEntity.getRole().equals("nonadmin")) {
            throw new AuthorizationFailedException("ATHR-003", "Unauthorized Access,Entered user is not an admin");
        }

        UsersEntity usersEntity1 = usersDao.getUserByUuid(uuid);
        if(usersEntity1 == null) {
            throw new UserNotFoundException("USR-001" , "User with entered uuid to be deleted does not exist");
        }

        usersDao.deleteUser(usersEntity);

        return usersEntity;


    }
}

