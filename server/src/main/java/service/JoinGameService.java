package service;

import dataaccess.*;
import exception.AlreadyTakenException;
import exception.BadRequestException;
import exception.ResponseException;
import exception.UnauthorizedException;
import request.JoinGameRequest;
import response.JoinGameResponse;

public class JoinGameService
{
    private AuthDAO myAuth;
    private GameDAO myGame;

    public JoinGameService(AuthDAO myAuth, GameDAO myGame)
    {
        this.myAuth = myAuth;
        this.myGame = myGame;
    }

    public JoinGameResponse updateGame(String authToken, JoinGameRequest joinRequest) throws ResponseException
    {
        if(authToken == null)
        {
            throw new BadRequestException("Bad Request");
        }
        else if(myAuth.getClassInfo(authToken) == null)
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
            String username = myAuth.getClassInfo(authToken).username(); //gets the username from the AuthData class using one of its methods
            try
            {
                myGame.updateGame(joinRequest.gameID(), joinRequest.playerColor(), username);
            }
            catch(ResponseException | DataAccessException e)
            {
                System.out.println("Update Game not functional");
            }
            return new JoinGameResponse();
        }
    }
}
