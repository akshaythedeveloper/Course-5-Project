package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.UserDetailsResponse;
import com.upgrad.quora.service.business.UsersBusinessService;
import com.upgrad.quora.service.entity.UsersEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class CommonController {

    @Autowired
    private UsersBusinessService usersBusinessService;

    @RequestMapping(method = RequestMethod.GET , path = "/userprofile/{userId}" ,  produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UserDetailsResponse> userProfile(@PathVariable("userId") final String userUuid, @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException , UserNotFoundException{

        final UsersEntity usersEntity = usersBusinessService.userProfile(userUuid, authorization);

        UserDetailsResponse userDetailsResponse = new UserDetailsResponse().firstName(usersEntity.getFirstname()).lastName(usersEntity.getLastname()).aboutMe(usersEntity.getAboutme())
                .country(usersEntity.getCountry()).dob(usersEntity.getDob()).contactNumber(usersEntity.getContactnumber())
                .userName(usersEntity.getUsername()).emailAddress(usersEntity.getEmail());

        return new ResponseEntity<UserDetailsResponse>(userDetailsResponse , HttpStatus.OK);


    }


}
