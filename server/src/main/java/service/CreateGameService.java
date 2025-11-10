package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import exception.AlreadyTakenException;
import exception.BadRequestException;
import model.CreateRequest;
import model.CreateResponse;

public class CreateGameService extends ParentService
{
    private MemoryGameDAO myGame;

    public CreateGameService(MemoryAuthDAO myAuth, MemoryGameDAO myGame)
    {
        super(myAuth);
        this.myGame = myGame;
    }

    public CreateResponse createGame(CreateRequest createRequest)throws BadRequestException, AlreadyTakenException
    {
        //check authdata first with other method
        //create needs an authtoken and a gamename
        //if the authtoken is invalid, return exception, use verify auth

        if(createRequest.gameName() == null)
        {
            throw new BadRequestException("Bad Request");
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