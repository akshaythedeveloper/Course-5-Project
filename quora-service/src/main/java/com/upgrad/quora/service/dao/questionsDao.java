package com.upgrad.quora.service.dao;


import com.upgrad.quora.service.entity.QuestionEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

import javax.persistence.PersistenceContext;
import java.util.List;
import javax.persistence.NoResultException;


/**DAO class for EndPoint 2 - operations related to questions*/
@Repository
public class questionsDao {


    @PersistenceContext
    private EntityManager entityManager;
    private static final String GET_ALL_QUESTIONS = "getAllQuestions";
    private static final String GET_QUESTIONS = "getQuestions";
    private static final String GET_ALL_QUESTIONS_FOR_USER = "getAllQuestionsForUser";


    /*** this method is used for creating questions*/
    public QuestionEntity createQuestion(QuestionEntity questions) {
        entityManager.persist(questions);
        return questions;
    }

    /*** this method is used for getting all questions.*/

    public List<QuestionEntity> getAllQuestions() {
        try {
            return entityManager
                    .createNamedQuery(GET_ALL_QUESTIONS)
                    .getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    /*** this method is used for getting questions asked by the same owner*/
    public QuestionEntity getQuestion(String questionUuId) {
        try {
            return entityManager
                    .createNamedQuery(GET_QUESTIONS, QuestionEntity.class)
                    .setParameter("uuid", questionUuId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /*** this method used for getting all questions for a specific uuid*/
    public List<QuestionEntity> getAllQuestionsForUser(String uuId) {
        try {
            return entityManager
                    .createNamedQuery(GET_ALL_QUESTIONS_FOR_USER)
                    .setParameter("uuid", uuId)
                    .getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    /*** This method is used to edit the question*/

    public QuestionEntity editQuestion(QuestionEntity question) {
        entityManager.persist(question);
        return question;
    }


}
