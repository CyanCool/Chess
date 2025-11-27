package handler;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import exception.AlreadyTakenException;
import exception.BadRequestException;
import exception.ResponseException;
import exception.UnauthorizedException;
import io.javalin.http.Context;
import request.JoinGameRequest;
import response.ErrorResponse;
import response.JoinGameResponse;
import service.JoinGameService;

public class JoinGameHandler
{
    private JoinGameService joinGame;
    public JoinGameHandler(AuthDAO myAuth, GameDAO myGame)
    {
        joinGame = new JoinGameService(myAuth, myGame);
    }

    public void join(Context ctx)throws BadRequestException, UnauthorizedException, AlreadyTakenException
    {
        String authToken = ctx.header("authorization");
        JoinGameRequest joinGameRequest = new Gson().fromJson(ctx.body(), JoinGameRequest.class);
        try
        {
            JoinGameResponse joinGameResponse = joinGame.updateGame(authToken, joinGameRequest);
            ctx.result(new Gson().toJson(joinGameResponse));
            ctx.status(200);
        }
        catch(BadRequestException b)
        {
            ErrorResponse badReq = new ErrorResponse("Error: bad request");
            ctx.result(new Gson().toJson(badReq));
            ctx.status(400);
        }
        catch(UnauthorizedException d)
        {
            ErrorResponse invalid = new ErrorResponse("Error: unauthorized");
            ctx.result(new Gson().toJson(invalid));
            ctx.status(401);
        }
        catch(AlreadyTakenException a)
        {
            ErrorResponse taken = new ErrorResponse("Error: already taken");
            ctx.result(new Gson().toJson(taken));
            ctx.status(403);
        }
        catch(ResponseException e)
        {
            ErrorResponse serverIssue = new ErrorResponse("Error: Server Issue");
            ctx.result(new Gson().toJson(serverIssue));
            System.err.println("Error: The server is having an issue.");
            ctx.status(500);
        }
    }
}
