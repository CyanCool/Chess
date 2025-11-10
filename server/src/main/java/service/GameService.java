package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import exception.*;
import io.javalin.http.Context;
import model.*;

public class GameService
{
    private MemoryUserDAO myData;
    private MemoryAuthDAO myAuth;
    private MemoryGameDAO myGame;

    public GameService(MemoryUserDAO myData, MemoryAuthDAO myAuth, MemoryGameDAO myGame)
    {
        this.myData = myData;
        this.myAuth = myAuth;
        this.myGame = myGame;
    }

    public ListgamesResponse list(ListgamesRequest listgamesRequest) //this is going to list all the games that exist given the authToken
    {
        //if there is no authtoken or it doesnt work, throw exception
        String listOfGames = "";
        if(listgamesRequest.authToken() == null)
        {
            throw new InvalidAuthDataException("User cannot be verified"); //make message better later
        }
        else
        {
            listOfGames = myData.listGames();
            ListgamesResponse listgamesResponse = new ListgamesResponse(listgamesRequest.authToken(),listOfGames);
            myAuth.remove(myAuth.getAuth(listgamesRequest.authToken()));
            return listgamesResponse;
        }
    }
}
