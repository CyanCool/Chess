package service;

import dataaccess.*;
import dataaccess.SQLGameDAO;
import exception.BadRequestException;
import exception.ResponseException;
import exception.UnauthorizedException;
import request.CreateRequest;
import response.CreateResponse;

public class CreateGameService
{
    private GameDAO myGame;
    private AuthDAO myAuth;

    public CreateGameService(GameDAO myGame, AuthDAO myAuth)
    {
        this.myGame = myGame;
        this.myAuth = myAuth;
    }

    public CreateResponse createGame(String authToken, CreateRequest createRequest) throws BadRequestException, UnauthorizedException,
            ResponseException, DataAccessException
    {
        if(authToken == null)
        {
            throw new BadRequestException("Bad Request");
        }
        else if(myAuth.getClassInfo(authToken) == null)
        {
            throw new UnauthorizedException("This session doesn't exist");
        }
        else if(createRequest.gameName() == null)
        {
            throw new BadRequestException("The game name is blank");
        }
        else if(myGame.getClassInfo(createRequest.gameName()) != null)
        {
            throw new UnauthorizedException("This game name is already taken");
        }
        else
        {
            myGame.createGame(createRequest.gameName());
            return new CreateResponse(myGame.getClassInfo(createRequest.gameName()).gameID());
        }
    }
}