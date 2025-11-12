package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import exception.BadRequestException;
import exception.UnauthorizedException;
import request.CreateRequest;
import response.CreateResponse;

public class CreateGameService
{
    private MemoryGameDAO myGame;
    private MemoryAuthDAO myAuth;

    public CreateGameService(MemoryGameDAO myGame, MemoryAuthDAO myAuth)
    {
        this.myGame = myGame;
        this.myAuth = myAuth;
    }

    public CreateResponse createGame(String authToken, CreateRequest createRequest)throws BadRequestException, UnauthorizedException
    {
        if(authToken == null)
        {
            throw new BadRequestException("Bad Request");
        }
        else if(myAuth.getAuth(authToken) == null)
        {
            throw new UnauthorizedException("This session doesn't exist");
        }
        else if(createRequest.gameName() == null)
        {
            throw new BadRequestException("The game name is blank");
        }
        else if(myGame.getGame(createRequest.gameName()) != null)
        {
            throw new UnauthorizedException("This game name is already taken");
        }
        else
        {
            myGame.createGame(createRequest.gameName());
            return new CreateResponse(myGame.getGame(createRequest.gameName()).gameID());
        }
    }
}