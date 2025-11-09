package service;

import com.google.gson.Gson;
import dataaccess.MemoryAuthDAO;
import dataaccess.UserMemoryDAO;
import exception.*;
import io.javalin.http.Context;
import model.*;

import java.util.Map;

public class UserService
{
    private UserMemoryDAO myData;
    private MemoryAuthDAO myAuth;

    public UserService(UserMemoryDAO myData, MemoryAuthDAO myAuth)
    {
        this.myData = myData;
        this.myAuth = myAuth;
    }

    public RegisterResponse register(RegisterRequest registerRequest)
    {
        String token;
        if(myData.getUser(registerRequest.username()) == null)
        {
            myData.createUser(registerRequest);
            token = myAuth.createAuth(registerRequest.username());

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

    public LogoutResponse logout(LogoutRequest logoutRequest) throws BadRequestException, InvalidAuthDataException
    {
        if(logoutRequest == null)
        {
            throw new BadRequestException("The server cannot authenticate properly");
        }
        else if(myAuth.getAuth(logoutRequest.authToken()) == null)
        {
            throw new InvalidAuthDataException("Your session is unauthorized");
        }
        else
        {
            LogoutResponse logoutResponse = new LogoutResponse(logoutRequest.authToken());
            myAuth.remove(myAuth.getAuth(logoutRequest.authToken()));
            return logoutResponse;
        }

    }
    public void removeAuthData(AuthData authData)
    {
        myAuth.remove(authData);
    }

    private boolean authorized(Context ctx)
    {
        boolean check = false;
        String authTokenHeader = ctx.header("authorization");

        for(AuthData authData : myAuth.getAllAuthData())
        {
            if(authData.authToken().equals(authTokenHeader))
            {
                check = true;
                myAuth.remove(authData);
            }
        }
        return check;
    }

}
