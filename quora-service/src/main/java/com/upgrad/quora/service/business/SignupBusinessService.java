package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UsersDao;
import com.upgrad.quora.service.entity.UsersEntity;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service
public class SignupBusinessService {

    @Autowired
    private UsersDao usersDao;

    @Autowired
    private PasswordCryptographyProvider cryptographyProvider;

    @Transactional(propagation = Propagation.REQUIRED)
    public UsersEntity signup(UsersEntity usersEntity) throws SignUpRestrictedException {

        /* Here we encrypt the password.
         * When user enters the password it gets encrypted and then saves in the database
         * */
        String[] encryptedText = cryptographyProvider.encrypt(usersEntity.getPassword());
        usersEntity.setSalt(encryptedText[0]);
        usersEntity.setPassword(encryptedText[1]);




        //Object user1 saves the result of query which checks user is already registered or not with current username.
        UsersEntity user1 = usersDao.getUserByUsername(usersEntity.getUsername());
        if(user1 == null) {
            //Do nothing
        } else {
            throw new SignUpRestrictedException("SGR-001", "Try any other Username, this Username has already been taken");
        }

        //Object user2 saves the result of query which checks user is already registered or not with current email.
        UsersEntity user2 = usersDao.getUserByEmail(usersEntity.getEmail());
        if(user2 == null) {
           usersDao.createUser(usersEntity);
           return usersEntity;
        } else {
            throw new SignUpRestrictedException("SGR-002", "This user has already been registered, try with any other emailId");
        }




    }
}
