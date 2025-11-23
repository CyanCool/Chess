package service;

import dataaccess.*;
import exception.*;
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
}
