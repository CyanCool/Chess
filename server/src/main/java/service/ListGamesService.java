package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import exception.BadRequestException;
import exception.ResponseException;
import exception.UnauthorizedException;
import model.GameData;
import request.ListgamesRequest;
import response.ListgamesResponse;

import java.util.ArrayList;

public class ListGamesService
{
    private AuthDAO myAuth;
    private GameDAO myGame;

    public ListGamesService(AuthDAO myAuth, GameDAO myGame)
    {
        this.myAuth = myAuth;
        this.myGame = myGame;
    }

    public ListgamesResponse listGames(ListgamesRequest myRequest) throws ResponseException
    {
        if(myRequest.authToken() == null)
        {
            throw new BadRequestException("Bad Request");
        }
        else if(myAuth.getClassInfo(myRequest.authToken()) == null)
        {
            throw new UnauthorizedException("This session doesn't exist");
        }
        else
        {
            ListgamesResponse listgamesResponse = new ListgamesResponse(myGame.getList());
            return listgamesResponse;
        }
    }

}
