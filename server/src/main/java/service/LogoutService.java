package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import exception.*;
import io.javalin.http.Context;
import model.*;

public class LogoutService
{
    private MemoryUserDAO myData;
    private MemoryAuthDAO myAuth;

    public LogoutService(MemoryUserDAO myData, MemoryAuthDAO myAuth)
    {
        this.myData = myData;
        this.myAuth = myAuth;
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
}
