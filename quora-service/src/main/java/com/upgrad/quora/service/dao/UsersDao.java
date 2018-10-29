package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.UsersEntity;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class UsersDao {

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
}
