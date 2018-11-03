package com.upgrad.quora.service.dao;


import com.upgrad.quora.service.entity.QuestionEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

import javax.persistence.PersistenceContext;


/**DAO class for EndPoint 2 - operations related to questions*/
@Repository
public class questionsDao {


    @PersistenceContext
    private EntityManager entityManager;


    /*** this method is used for creating questions*/
    public QuestionEntity createQuestion(QuestionEntity questions) {
        entityManager.persist(questions);
        return questions;
    }
}
