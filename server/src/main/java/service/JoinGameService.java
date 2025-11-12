package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import exception.AlreadyTakenException;
import exception.BadRequestException;
import exception.UnauthorizedException;
import request.JoinGameRequest;
import response.JoinGameResponse;

public class JoinGameService
{
    private MemoryAuthDAO myAuth;
    private MemoryGameDAO myGame;

    public JoinGameService(MemoryAuthDAO myAuth, MemoryGameDAO myGame)
    {
        this.myAuth = myAuth;
        this.myGame = myGame;
    }

    public JoinGameResponse updateGame(String authToken, JoinGameRequest joinRequest)
    {
        if(authToken == null)
        {
            throw new BadRequestException("Bad Request");
        }
        else if(myAuth.getAuth(authToken) == null)
        {
            throw new UnauthorizedException("This session doesn't exist");
        }
        else if(joinRequest.gameID() == 0 || joinRequest.playerColor() == null)
        {
            throw new BadRequestException("One of the required fields are blank");
        }
        else if(myGame.getGame(joinRequest.gameID()) == null)
        {
            throw new UnauthorizedException("This game does not exist");
        }
        else if((myGame.getGame(joinRequest.gameID()).blackUsername() != null && joinRequest.playerColor().equals("BLACK"))
                || (myGame.getGame(joinRequest.gameID()).whiteUsername() != null && joinRequest.playerColor().equals("WHITE")))
        {
            throw new AlreadyTakenException("This spot is already taken");
        }
        else
        {
            String username = myAuth.getAuth(authToken).username(); //gets the username from the AuthData class using one of its methods
            myGame.updateGame(joinRequest.gameID(), joinRequest.playerColor(), username);
            return new JoinGameResponse();
        }
    }
}
