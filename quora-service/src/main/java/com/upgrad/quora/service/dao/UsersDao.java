package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UsersEntity;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;


@Repository
public class UsersDao {

    private static final String checkAuthToken = "checkAuthToken";
    private static final String USER_BY_UUID = "userByUuid";

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
