package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import exception.BadRequestException;
import exception.DoesNotExistException;
import model.ListgamesRequest;
import model.ListgamesResponse;

public class ListGamesService
{
    private MemoryAuthDAO myAuth;
    private MemoryGameDAO myGame;

    public ListGamesService(MemoryAuthDAO myAuth, MemoryGameDAO myGame)
    {
        this.myAuth = myAuth;
        this.myGame = myGame;
    }

    public ListgamesResponse listGames(ListgamesRequest myRequest)
    {
        if(myRequest.authToken() == null)
        {
            throw new BadRequestException("Bad Request");
        }
        else if(myAuth.getAuth(myRequest.authToken()) == null)
        {
            throw new DoesNotExistException("This session doesn't exist");
        }
        else
        {
            ListgamesResponse listgamesResponse = new ListgamesResponse(myGame.listGames());
            return listgamesResponse;
        }
    }

}
