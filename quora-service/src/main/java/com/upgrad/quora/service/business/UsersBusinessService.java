package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UsersDao;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UsersEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import com.upgrad.quora.service.type.ActionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UsersBusinessService {
    @Autowired
    private UsersDao usersDao;

    public UsersEntity userProfile(final String userUuid) throws UserNotFoundException {
        UsersEntity usersEntity =  usersDao.userProfile(userUuid);
        if(usersEntity == null) {
            throw new UserNotFoundException("USR-001" , "User with entered uuid does not exist");

        }
        return usersEntity;

    }

    /**
     * method used for get user auth details for the user with right access token.
     *
     * @param authorizationToken access token value
     * @param actionType         action type based on which we have to send various messages
     * @return UserAuth Entity object
     * @throws AuthorizationFailedException exception indicating user is not signed in or not signed out.
     */
    public UserAuthEntity getUserByAccessToken(String authorizationToken, ActionType actionType) throws AuthorizationFailedException {
        UserAuthEntity userAuthTokenEntity = usersDao.checkAuthToken(authorizationToken);
        if (userAuthTokenEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }
        if (userAuthTokenEntity.getLogoutAt() != null) {
            if (actionType.equals(actionType.CREATE_QUESTION)) {
                throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to post a question");
            } else if (actionType.equals(ActionType.ALL_QUESTION)) {
                throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to get all questions");
            } else if (actionType.equals(ActionType.EDIT_QUESTION)) {
                throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to edit the question");
            } else if (actionType.equals(ActionType.DELETE_QUESTION)) {
                throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to delete a question");
            } else if (actionType.equals(ActionType.ALL_QUESTION_FOR_USER)) {
                throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to get all questions posted by a specific user");
            } else if (actionType.equals(ActionType.CREATE_ANSWER)) {
                throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to post an answer");
            } else if (actionType.equals(ActionType.EDIT_ANSWER)) {
                throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to edit an answer");
            } else if (actionType.equals(ActionType.DELETE_ANSWER)) {
                throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to delete an answer");
            } else if (actionType.equals(ActionType.GET_ALL_ANSWER_TO_QUESTION)) {
                throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to get the answers");
            }
        }
        return userAuthTokenEntity;
    }
}
