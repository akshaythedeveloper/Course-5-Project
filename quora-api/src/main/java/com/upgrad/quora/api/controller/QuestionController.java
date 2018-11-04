package com.upgrad.quora.api.controller;


import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.business.questionsService;
import com.upgrad.quora.service.business.UsersBusinessService;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UsersEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.QuestionNotFoundException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import com.upgrad.quora.service.type.ActionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.ZonedDateTime;
import java.util.List;
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
     *
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

    /**
     * The lines below implement the rest endpoint method for getting all questions
     * The questions can be viewed only by the users who are logged in
     */

    @RequestMapping(method = RequestMethod.GET, path = "/question/all/{userId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionDetailsResponse> getAllQuestionsByUser(@PathVariable("userId") final String uuId, @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, QuestionNotFoundException, UserNotFoundException {
        UserAuthEntity authorizedUser = UsersBusinessService.getUserByAccessToken(authorization, ActionType.ALL_QUESTION_FOR_USER);
        List<QuestionEntity> questionList = questionsService.getQuestionsForUser(uuId);
        StringBuilder contentBuilder = new StringBuilder();
        StringBuilder uuIdBuilder = new StringBuilder();
        getContentsString(questionList, contentBuilder);
        getUuIdString(questionList, uuIdBuilder);
        QuestionDetailsResponse questionResponse = new QuestionDetailsResponse()
                .id(uuIdBuilder.toString())
                .content(contentBuilder.toString());
        return new ResponseEntity<QuestionDetailsResponse>(questionResponse, HttpStatus.OK);
    }

    public static final StringBuilder getContentsString(List<QuestionEntity> questionList, StringBuilder builder) {
        for (QuestionEntity questionObject : questionList) {
            builder.append(questionObject.getContent()).append(",");
        }
        return builder;
    }
    public static final StringBuilder getUuIdString(List<QuestionEntity> questionList, StringBuilder uuIdBuilder) {

        for (QuestionEntity questionObject : questionList) {
            uuIdBuilder.append(questionObject.getUuid()).append(",");
        }
        return uuIdBuilder;
    }

    /**
     * The lines below implement the rest endpoint method for getting all questions
     * The questions can be viewed only by the users who are logged in
     */
    @RequestMapping(method = RequestMethod.PUT, path = "/question/edit/{questionId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionEditResponse> editQuestionContent(QuestionEditRequest questionEditRequest, @PathVariable("questionId") final String questionId, @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, InvalidQuestionException {
        UserAuthEntity authorizedUser = UsersBusinessService.getUserByAccessToken(authorization, ActionType.EDIT_QUESTION);
        QuestionEntity question = questionsService.isUserQuestionOwner(questionId, authorizedUser, ActionType.EDIT_QUESTION);
        question.setContent(questionEditRequest.getContent());
        questionsService.editQuestion(question);
        QuestionEditResponse questionEditResponse = new QuestionEditResponse().id(question.getUuid()).status("QUESTION EDITED");
        return new ResponseEntity<QuestionEditResponse>(questionEditResponse, HttpStatus.OK);
    }


}