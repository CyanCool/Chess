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
        if(loginRequest.username() == null || loginRequest.password() == null)
        {
            throw new BlankFieldException("One of the fields are missing");
        }
        else if(myData.getUser(loginRequest.username()) == null)
        {
            throw new UnauthorizedException("This user does not exist");
        }
        else if(!myData.verifyUser(loginRequest.username(), loginRequest.password()))
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
