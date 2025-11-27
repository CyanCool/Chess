package service;

import dataaccess.*;
import exception.*;
import model.AuthData;
import request.LogoutRequest;
import response.LogoutResponse;

public class LogoutService
{
    private UserDAO myData;
    private AuthDAO myAuth;

    public LogoutService(UserDAO myData, AuthDAO myAuth)
    {
        this.myData = myData;
        this.myAuth = myAuth;
    }

    public LogoutResponse logout(LogoutRequest logoutRequest) throws BadRequestException, InvalidAuthDataException,
            ResponseException, DataAccessException
    {
        if(logoutRequest == null)
        {
            throw new BadRequestException("The server cannot authenticate properly");
        }
        else if(myAuth.getClassInfo(logoutRequest.authToken()) == null)
        {
            throw new InvalidAuthDataException("Your session is unauthorized");
        }
        else
        {
            LogoutResponse logoutResponse = new LogoutResponse(logoutRequest.authToken());
            myAuth.remove((AuthData) myAuth.getClassInfo(logoutRequest.authToken()));
            return logoutResponse;
        }

    }
}
