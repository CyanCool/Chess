package service;

import dataaccess.*;
import exception.AlreadyTakenException;
import exception.BadRequestException;
import exception.ResponseException;
import request.RegisterRequest;
import response.RegisterResponse;

public class RegisterService
{
    private UserDAO myData;
    private AuthDAO myAuth;

    public RegisterService(MemoryUserDAO myData, MemoryAuthDAO myAuth)
    {
        this.myData = myData;
        this.myAuth = myAuth;
    }

    public RegisterResponse register(RegisterRequest registerRequest) throws ResponseException, DataAccessException
    {
        String token;
        if(registerRequest.username() == null || registerRequest.password() == null || registerRequest.email() == null)
        {
            throw new BadRequestException("One of the required fields (username, password, or email) are missing");
        }
        else if(myData.getUser(registerRequest.username()) != null)
        {
            throw new AlreadyTakenException("This Username is already taken");
        }
        else
        {
            myData.createUser(registerRequest);
            token = myAuth.createAuth(registerRequest.username());

            RegisterResponse registerResponse = new RegisterResponse(registerRequest.username(), token);
            return registerResponse;
        }
    }
}
