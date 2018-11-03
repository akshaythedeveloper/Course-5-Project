package com.upgrad.quora.api.controller;


import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.business.questionsService;
import com.upgrad.quora.service.business.UsersBusinessService;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UsersEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.type.ActionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * Controller class for defining question related operations.
 */
@RestController
@RequestMapping("/")
public class QuestionController {

    @Autowired
    UsersBusinessService UsersBusinessService;

    @Autowired
   questionsService questionsService;

    /**
     * The lines below implement the rest endpoint method for creating question for authorized user.
     * <p>
     * The questions can be created only the user who is logged in
     * An exception is thrown if the user is not logged in
     */
    @RequestMapping(method = RequestMethod.POST, path = "/question/create", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionResponse> createQuestion(final QuestionRequest questionRequest, @RequestHeader final String authorization) throws AuthorizationFailedException {
        UserAuthEntity authorizedUser = UsersBusinessService.getUserByAccessToken(authorization, ActionType.CREATE_QUESTION);
        UsersEntity user = authorizedUser.getUser();
        QuestionEntity question = new QuestionEntity();
        question.setUser(authorizedUser.getUser());
        question.setUuid(UUID.randomUUID().toString());
        question.setContent(questionRequest.getContent());
        final ZonedDateTime now = ZonedDateTime.now();
        question.setDate(now);
        QuestionEntity createdQuestion = questionsService.createQuestion(question);
        QuestionResponse questionResponse = new QuestionResponse().id(createdQuestion.getUuid()).status("QUESTION CREATED");
        return new ResponseEntity<QuestionResponse>(questionResponse, HttpStatus.CREATED);
    }
}