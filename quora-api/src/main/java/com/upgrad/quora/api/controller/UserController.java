package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.*;
//import com.upgrad.quora.service.business.AuthenticationService;
import com.upgrad.quora.service.business.AuthenticationService;
import com.upgrad.quora.service.business.SignupBusinessService;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UsersEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class UserController {

    @Autowired
    private SignupBusinessService signupBusinessService;

    @RequestMapping(method = RequestMethod.POST , path = "/user/signup"  , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SignupUserResponse> signup(final SignupUserRequest signupUserRequest) throws SignUpRestrictedException {

        final UsersEntity usersEntity = new UsersEntity();
        usersEntity.setUuid(UUID.randomUUID().toString());
        usersEntity.setFirstname(signupUserRequest.getFirstName());
        usersEntity.setLastname(signupUserRequest.getLastName());
        usersEntity.setUsername(signupUserRequest.getUserName());
        usersEntity.setEmail(signupUserRequest.getEmailAddress());
        usersEntity.setPassword(signupUserRequest.getPassword());
        usersEntity.setCountry(signupUserRequest.getCountry());
        usersEntity.setAboutme(signupUserRequest.getAboutMe());
        usersEntity.setDob(signupUserRequest.getDob());
        usersEntity.setContactnumber(signupUserRequest.getContactNumber());
        usersEntity.setRole("nonadmin");
        usersEntity.setSalt("abc@123");


        final UsersEntity createdUsersEntity = signupBusinessService.signup(usersEntity);
        SignupUserResponse userResponse = new SignupUserResponse().id(createdUsersEntity.getUuid()).status("USER SUCCESSFULLY REGISTERED");

        return new ResponseEntity<SignupUserResponse>(userResponse, HttpStatus.CREATED);
    }

    @Autowired
    private AuthenticationService authenticationService;

    @RequestMapping(method = RequestMethod.POST , path = "/user/signin"  ,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SigninResponse> signin(@RequestHeader("authorization") final String authorization) throws AuthenticationFailedException {
        byte[]  decode = Base64.getDecoder().decode(authorization.split("Basic ")[1]);
        String decodedText = new String(decode);
        String[] decodedArray = decodedText.split(":");

        UserAuthEntity userAuthEntity = authenticationService.authenticate(decodedArray[0] , decodedArray[1]);
        UsersEntity user = userAuthEntity.getUser();

        SigninResponse signinResponse = new SigninResponse().id(user.getUuid()).message("SIGNED IN SUCCESSFULLY");


        HttpHeaders headers = new HttpHeaders();
        headers.add("access-token", userAuthEntity.getAccessToken());
        return new ResponseEntity<SigninResponse>(signinResponse, headers, HttpStatus.OK);

    }

    @RequestMapping(method = RequestMethod.POST , path = "/user/signout" , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SignoutResponse> signout(@RequestHeader("authorization") final String accessToken) throws SignOutRestrictedException {


        UserAuthEntity userAuthEntity = authenticationService.signout(accessToken);
        UsersEntity usersEntity = userAuthEntity.getUser();

        SignoutResponse signoutResponse = new SignoutResponse().id(usersEntity.getUuid()).message("SIGNED OUT SUCCESSFULLY");

        return new ResponseEntity<SignoutResponse>(signoutResponse , HttpStatus.OK);


    }
}
