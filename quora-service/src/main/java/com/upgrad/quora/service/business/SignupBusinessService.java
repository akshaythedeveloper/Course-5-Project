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

        String[] encryptedText = cryptographyProvider.encrypt(usersEntity.getPassword());
        usersEntity.setSalt(encryptedText[0]);
        usersEntity.setPassword(encryptedText[1]);

        /**UsersEntity user1 = usersDao.getUserByUsername(usersEntity.getUsername());
        if(user1 != null) {
            throw new SignUpRestrictedException("SGR-001", "Try any other Username, this Username has already been taken");
        }**/



        UsersEntity user1 = usersDao.getUserByEmail(usersEntity.getEmail());
        if(user1 != null) {
            throw new SignUpRestrictedException("SGR-001", "This user has already been registered, try with any other emailId");
        }
        usersDao.createUser(usersEntity);
        return usersEntity;

    }
}
