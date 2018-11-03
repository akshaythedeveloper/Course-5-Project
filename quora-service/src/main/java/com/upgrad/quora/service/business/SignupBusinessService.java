package com.upgrad.quora.service.business;

//import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIConversion;
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

        return usersDao.createUser(usersEntity);

    }
}
