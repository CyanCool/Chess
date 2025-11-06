package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.UserMemoryDAO;
import exception.BadRequestException;
import exception.DoesNotExistException;
import exception.PasswordIncorrectException;
import model.LoginRequest;
import model.LoginResponse;
import model.RegisterResponse;
import model.RegisterRequest;
import exception.AlreadyTakenException;

public class UserService
{
    private UserMemoryDAO myData; //where do i put this idk
    private MemoryAuthDAO myAuth;

    public UserService()
    {
        myData = new UserMemoryDAO();
        myAuth = new MemoryAuthDAO();
    }

    public RegisterResponse register(RegisterRequest registerRequest)
    {
        String token;
        if(myData.getUser(registerRequest.username()) == null)
        {
            myData.createUser(registerRequest);
            token = myAuth.createAuth();

            RegisterResponse registerResponse = new RegisterResponse(registerRequest.username(), token);
            return registerResponse;
        }
        else if((registerRequest.username() != null && (registerRequest.password() == null || registerRequest.email() == null)) || (registerRequest.password() != null && (registerRequest.username() == null || registerRequest.email() == null)) || (registerRequest.email() != null && (registerRequest.username() == null || registerRequest.password() == null)))
        {
            throw new BadRequestException("One of the required fields (username, password, or email) are missing");
        }
        else
        {
            throw new AlreadyTakenException("This Username is already taken");
        }
    }

    public LoginResponse login(LoginRequest loginRequest)
    {
        //success case for login
        //the username and password are both strings and none of them are null
        //exceptions that I will throw
            //user with the username does not exist
            //password is incorrect
            //one or more of the something are null
        if(myData.getUser(loginRequest.username()) == null)
        {
            throw new DoesNotExistException("This user does not exist");
        }
        else if(!myData.getUser(loginRequest.username()).password().equals(loginRequest.password()))
        {
            throw new PasswordIncorrectException("This password is incorrect");
        }
        else if(loginRequest.username() == null || loginRequest.password() == null)
        {
            throw new BadRequestException("One of the fields are missing");
        }
        //maybe verify the fields are both strings before confirming true
        else
        {
            String token = myAuth.createAuth();
            LoginResponse loginResponse = new LoginResponse(loginRequest.username(), token);

            return loginResponse;
        }
    }

}
