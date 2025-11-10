package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import exception.BadRequestException;
import exception.DoesNotExistException;
import io.javalin.http.Context;
import model.JoinGameRequest;
import model.JoinGameResponse;

public class JoinGameService extends ParentService
{
    private MemoryAuthDAO myAuth;
    private MemoryGameDAO myGame;
    private MemoryUserDAO myData;

    public JoinGameService(MemoryAuthDAO myAuth, MemoryGameDAO myGame, MemoryUserDAO myData)
    {
        super(myAuth);
        this.myGame = myGame;
        this.myData = myData;
    }

    public JoinGameResponse updateGame(JoinGameRequest joinRequest)
    {
        //verify the auth token
        //verify that the game exists based on the name
        //verify that none of the fields are blank
        if(joinRequest.gameID() == null || joinRequest.playerColor() == null)
        {
            throw new BadRequestException("One of the required fields are blank");
        }
        else if(myGame.getGame(joinRequest.gameID()) == null)
        {
            throw new DoesNotExistException("This game does not exist");
        }
        else
        {
            myGame.updateGame(joinRequest.gameID(), joinRequest.playerColor());
            return new JoinGameResponse();
        }
    }
}
