package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import exception.AlreadyTakenException;
import exception.BadRequestException;
import exception.BlankFieldException;
import exception.DoesNotExistException;
import model.CreateRequest;
import model.CreateResponse;

public class CreateGameService
{
    private MemoryGameDAO myGame;
    private MemoryAuthDAO myAuth;

    public CreateGameService(MemoryGameDAO myGame, MemoryAuthDAO myAuth)
    {
        this.myGame = myGame;
        this.myAuth = myAuth;
    }

    public CreateResponse createGame(String authToken, CreateRequest createRequest)throws BadRequestException, AlreadyTakenException, DoesNotExistException
    {
        if(authToken == null)
        {
            throw new BadRequestException("Bad Request");
        }
        else if(myAuth.getAuth(authToken) == null)
        {
            throw new DoesNotExistException("This session doesn't exist");
        }
        else if(createRequest.gameName() == null)
        {
            throw new BlankFieldException("The game name is blank");
        }
        else if(myGame.getGame(createRequest.gameName()) != null)
        {
            throw new AlreadyTakenException("This game name is already taken");
        }
        else
        {
            myGame.createGame(createRequest.gameName());
            return new CreateResponse(myGame.getGame(createRequest.gameName()).gameID());
        }
        //if the gamename already exists, return an alreadytaken exception
        //otherwise, create the game, call the method from the database, then return a create response
    }
}