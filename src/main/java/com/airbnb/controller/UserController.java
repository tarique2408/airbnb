package com.airbnb.controller;

import com.airbnb.dto.LoginDto;
import com.airbnb.dto.PropertyUserDto;
import com.airbnb.dto.TokenResponseDto;
import com.airbnb.entity.PropertyUser;
import com.airbnb.service.UserService;
import org.apache.coyote.Request;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }


    //add user
    //url-http://localhost:8080/api/v1/users/addUser
   // @RequestMapping(name = "/sign-up",method = RequestMethod.POST)
    @PostMapping("/addUser")
    public ResponseEntity<String> addUser(@RequestBody PropertyUserDto propertyUserDto)
    {
        PropertyUser user = userService.addUser(propertyUserDto);
        if(user!=null){
            return new ResponseEntity<>("user registered successful", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("somethind went wrong",HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //sign-in
    //url- http://localhost:8080/api/v1/users/login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto){
        String token = userService.verifyLogin(loginDto);
        if(token!=null){
            TokenResponseDto tokenResponseDto=new TokenResponseDto();
            tokenResponseDto.setToken(token);

            //ResponseEntity convert the tokenResponseDto to json automatically and send response to client as json
            //why send as json - so that our fronted dev take this json and give it to url
            return new ResponseEntity<>(tokenResponseDto,HttpStatus.OK);
        }
        return new ResponseEntity<>("incorrect credentials",HttpStatus.UNAUTHORIZED);
    }


    //get the details of current user
    //spring boot take the request and take the token from http request and verify the token internally
    //and then based on the session id it take the user details
    //url-http://localhost:8080/api/v1/users/profile
    @GetMapping("/profile")
    public PropertyUser getUserDetails(@AuthenticationPrincipal PropertyUser user)

    {
        return user;
    }
}
