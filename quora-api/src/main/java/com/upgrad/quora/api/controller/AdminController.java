package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.UserDeleteResponse;
import com.upgrad.quora.api.model.UserDetailsResponse;
import com.upgrad.quora.service.business.AdminBusinessService;
import com.upgrad.quora.service.entity.UsersEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;

@RestController
@RequestMapping("/")
public class AdminController {

    /* This is a Admin controller and only admin has the authority to delete user from the database.
    userDelete method takes two arguments uuid of the user to be deleted and access token to check weather user is
    signed in or not. */


    @Autowired
    private AdminBusinessService adminBusinessService;

    @RequestMapping(method = RequestMethod.DELETE , path = "/admin/user/{userId}" , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UserDeleteResponse> userDelete(@PathVariable("userId") final String userUuid ,
                                                         @RequestHeader("authorization") final String authorization)
            throws AuthorizationFailedException , UserNotFoundException {

        final UsersEntity usersEntity = adminBusinessService.userDelete(userUuid , authorization);

        UserDeleteResponse userDeleteResponse = new UserDeleteResponse().id(usersEntity.getUuid()).status("USER SUCCESSFULLY DELETED");

        return new ResponseEntity<UserDeleteResponse>(userDeleteResponse , HttpStatus.OK);




    }
}


