package service;

import dataaccess.*;
import exception.*;
import request.LoginRequest;
import response.LoginResponse;

public class LoginService
{
    private UserDAO myData;
    private AuthDAO myAuth;

    public LoginService(UserDAO myData, AuthDAO myAuth)
    {
        this.myData = myData;
        this.myAuth = myAuth;
    }

    public LoginResponse login(LoginRequest loginRequest) throws ResponseException, DataAccessException
    {
        //success case for login
        //the username and password are both strings and none of them are null
        //exceptions that I will throw
        //user with the username does not exist
        //password is incorrect
        //one or more of the something are null
        if(loginRequest.username() == null || loginRequest.password() == null)
        {
            throw new BlankFieldException("One of the fields are missing");
        }
        else if(myData.getUser(loginRequest.username()) == null)
        {
            throw new UnauthorizedException("This user does not exist");
        }
        else if(!myData.getUser(loginRequest.username()).password().equals(loginRequest.password()))
        {
            throw new PasswordIncorrectException("This password is incorrect");
        }
        else
        {
            String token = myAuth.createAuth(loginRequest.username());
            LoginResponse loginResponse = new LoginResponse(loginRequest.username(), token);
            return loginResponse;
        }
    }
}
