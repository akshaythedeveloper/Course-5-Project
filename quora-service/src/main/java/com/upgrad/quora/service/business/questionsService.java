package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.questionsDao;
import com.upgrad.quora.service.dao.UsersDao;
import com.upgrad.quora.service.entity.QuestionEntity;

import com.upgrad.quora.service.entity.UsersEntity;

import com.upgrad.quora.service.exception.UserNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import com.upgrad.quora.service.exception.QuestionNotFoundException;


/**This is the service class for all the operations mentioned in Endpoint 2 about questions*/
@Service
public class questionsService {

    @Autowired
    questionsDao questionsDao;

    @Autowired
    UsersDao userDao;

    /**
     * The method below is used to create question
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity createQuestion(QuestionEntity question) {
        return questionsDao.createQuestion(question);

    }

    /**
     * The method below is used to get all questions

     */
    @Transactional(propagation = Propagation.REQUIRED)
    public List<QuestionEntity> getAllQuestions() throws QuestionNotFoundException {
        List<QuestionEntity> questionList = questionsDao.getAllQuestions();
        if (questionList == null) {
            throw new QuestionNotFoundException("QUER-002", "No questions found for any user");
        } else {
            return questionList;
        }
    }

    /**
     * The method below is used to get questions for a specific user

     */
    @Transactional(propagation = Propagation.REQUIRED)
    public List<QuestionEntity> getQuestionsForUser(final String uuId) throws QuestionNotFoundException, UserNotFoundException {
        UsersEntity user = UsersDao.userProfile(uuId);
        if (user == null) {
            throw new UserNotFoundException("USR-001", "User with entered uuid whose question details are to be seen does not exist");
        }
        List<QuestionEntity> questionList = questionsDao.getAllQuestionsForUser(user.getUuid());
        if (questionList == null) {
            throw new QuestionNotFoundException("QUER-001", "No questions found for user");
        } else {
            return questionList;
        }
    }


}