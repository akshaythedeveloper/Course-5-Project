package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UsersDao;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UsersEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import org.omg.CORBA.Current;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.UUID;

@Service
public class AuthenticationService {

    @Autowired
    private UsersDao usersDao;

    @Autowired
    private PasswordCryptographyProvider cryptographyProvider;

    @Transactional(propagation = Propagation.REQUIRED)
    public UserAuthEntity authenticate(final String username, final String password) throws AuthenticationFailedException {
        UsersEntity usersEntity = usersDao.getUserByUsername(username);
        if (usersEntity == null) {
            throw new AuthenticationFailedException("ATH-001", "This username does not exist");
        }

        final String encryptedPassword = cryptographyProvider.encrypt(password, usersEntity.getSalt());
        if (encryptedPassword.equals(usersEntity.getPassword())) {
            JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(encryptedPassword);
            UserAuthEntity userAuthTokenEntity = new UserAuthEntity();
            userAuthTokenEntity.setUser(usersEntity);
            userAuthTokenEntity.setUuid(UUID.randomUUID().toString());

            final ZonedDateTime now = ZonedDateTime.now();
            final ZonedDateTime expiresAt = now.plusHours(8);

            userAuthTokenEntity.setAccessToken(jwtTokenProvider.generateToken(usersEntity.getUuid(), now, expiresAt));
            userAuthTokenEntity.setLoginAt(now);
            userAuthTokenEntity.setExpiresAt(expiresAt);

            usersDao.createAuthToken(userAuthTokenEntity);
            usersDao.updateUser(usersEntity);

            return userAuthTokenEntity;
        }
        else {
            throw new AuthenticationFailedException("ATH-002", "Password failed");
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public UserAuthEntity signout(final String accessToken) throws SignOutRestrictedException {
        UserAuthEntity userAuthEntity = usersDao.getUserAuthToken(accessToken);
        if (userAuthEntity == null) {
            throw new SignOutRestrictedException("SGR-001", "User is not Signed in");
        }
        final ZonedDateTime now = ZonedDateTime.now();
        userAuthEntity.setLogoutAt(now);
        usersDao.updateUserAuth(userAuthEntity);

        return userAuthEntity;

    }


}
