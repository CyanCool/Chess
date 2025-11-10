package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import exception.*;
import io.javalin.http.Context;
import model.*;

public class LoginService
{
    private MemoryUserDAO myData;
    private MemoryAuthDAO myAuth;

    public LoginService(MemoryUserDAO myData, MemoryAuthDAO myAuth)
    {
        this.myData = myData;
        this.myAuth = myAuth;
    }

    public LoginResponse login(LoginRequest loginRequest)
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
            throw new DoesNotExistException("This user does not exist");
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
