package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.UserMemoryDAO;
import exception.BadRequestException;
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

        //exceptions that I will throw
    }

}
