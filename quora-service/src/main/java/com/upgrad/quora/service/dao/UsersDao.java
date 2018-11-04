package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UsersEntity;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import java.time.ZonedDateTime;

@Repository
public class UsersDao {

    @PersistenceContext
    private EntityManager entityManager;

    public UsersEntity createUser(UsersEntity usersEntity) {
        try {
            entityManager.persist(usersEntity);
            return usersEntity;
        } catch (ConstraintViolationException cve) {
            return null;
        }
    }

    public UsersEntity getUserByEmail(final String email) {
        try {
            return entityManager.createNamedQuery("userByEmail", UsersEntity.class).setParameter("email", email).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public UsersEntity getUserByUsername(final String username) {
        try {
            return entityManager.createNamedQuery("userByUsername", UsersEntity.class).setParameter("username", username).getSingleResult();
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

    public void updateUserAuth(final UserAuthEntity updatedUserAuthEntity) {
        entityManager.merge(updatedUserAuthEntity);
    }

    public UserAuthEntity getUserAuthToken(final String accessToken) {
        try {
            return entityManager.createNamedQuery("userAuthTokenByAccessToken", UserAuthEntity.class).setParameter("accessToken", accessToken).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }

    }

    public UsersEntity getUserByUuid(final String uuid) {
        try {
            return entityManager.createNamedQuery("userByUuid" , UsersEntity.class).setParameter("uuid" , uuid).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public UserAuthEntity getuserLogoutAtTime(final String accessToken) {
        try {
            return entityManager.createNamedQuery("userLogoutAtTime" , UserAuthEntity.class).setParameter("accessToken" , accessToken).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public UsersEntity getUserRole(final String uuid) {
        try {
            return entityManager.createNamedQuery("userRole" , UsersEntity.class).setParameter("uuid" , uuid).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public void deleteUser(final UsersEntity usersEntity) {
        entityManager.remove(usersEntity);
    }

}
