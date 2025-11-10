package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import exception.*;
import io.javalin.http.Context;
import model.*;

public class GameService
{
    private MemoryUserDAO myData;
    private MemoryAuthDAO myAuth;

    public GameService(MemoryUserDAO myData, MemoryAuthDAO myAuth)
    {
        this.myData = myData;
        this.myAuth = myAuth;
    }

    public ListgamesResponse list(ListgamesRequest listgamesRequest)
    {
        //if there is no authtoken or it doesnt work, throw exception
        String listOfGames = "";
        if(listgamesRequest.authToken() == null)
        {
            throw new InvalidAuthDataException("User cannot be verified");
        }
        else
        {
            listOfGames = myData.listGames();
            ListgamesResponse listgamesResponse = new ListgamesResponse(listgamesRequest.authToken(),listOfGames);
            myAuth.remove(myAuth.getAuth(listgamesRequest.authToken()));
            return listgamesResponse;
        }
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
    } //keep here, if works, make more efficient
}
