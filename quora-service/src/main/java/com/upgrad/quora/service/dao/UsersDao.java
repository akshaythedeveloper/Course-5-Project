package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UsersEntity;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.LinkedList;

@Repository
public class UsersDao {

    private static final String checkAuthToken = "checkAuthToken";

    @PersistenceContext
    private EntityManager entityManager;

    public UsersEntity createUser(UsersEntity usersEntity) {
                entityManager.persist(usersEntity);
                return usersEntity;
    }

    public UsersEntity userProfile(final String userUuid) {
        try {
            return entityManager.createNamedQuery("userByUuid", UsersEntity.class).setParameter("uuid" , userUuid).getSingleResult();
        } catch (NoResultException nre) {
            return null;

        }

    }

    public UsersEntity getUserByUserName(final String email) {
        try {
            return entityManager.createNamedQuery("userByUserName", UsersEntity.class).setParameter("email", email).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public UserAuthEntity createAuthToken(final UserAuthEntity userAuthTokenEntity) {
        entityManager.persist(userAuthTokenEntity);
        return userAuthTokenEntity;
    }



    public void updateUser(final UsersEntity updatedUserEntity) {
        entityManager.merge(updatedUserEntity);
    }

    /** this method used to check the validity of the authorization token.
     */
    public UserAuthEntity checkAuthToken(final String authorizationToken) {
        try {

            return entityManager.createNamedQuery(checkAuthToken,UserAuthEntity.class).setParameter("accessToken", authorizationToken)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

}
