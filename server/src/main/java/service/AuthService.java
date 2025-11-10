package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import exception.*;
import io.javalin.http.Context;
import model.*;

public class AuthService
{
    private MemoryUserDAO myData;
    private MemoryAuthDAO myAuth;
    private MemoryGameDAO myGame;

    public AuthService(MemoryUserDAO myData, MemoryAuthDAO myAuth, MemoryGameDAO myGame)
    {
        this.myData = myData;
        this.myAuth = myAuth;
        this.myGame = myGame;
    }

    public boolean authorized(Context ctx)throws InvalidAuthDataException
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
        if(!check)
        {
            throw new InvalidAuthDataException("InvalidAuthData");
        }
        return check;
    }
}
