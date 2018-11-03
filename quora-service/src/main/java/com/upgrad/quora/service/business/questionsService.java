package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.questionsDao;
import com.upgrad.quora.service.dao.UsersDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


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
}