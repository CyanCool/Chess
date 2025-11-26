package handler;


import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import exception.BadRequestException;
import exception.ResponseException;
import exception.UnauthorizedException;
import io.javalin.http.Context;
import request.CreateRequest;
import response.CreateResponse;
import response.ErrorResponse;
import service.CreateGameService;

public class CreateGameHandler
{
    private final CreateGameService createGame;

    public CreateGameHandler(AuthDAO myAuth, GameDAO myGame)
    {
        createGame = new CreateGameService(myGame, myAuth);
    }

    public void create(Context ctx)
    {
        String authToken = ctx.header("authorization");
        CreateRequest createRequest = new Gson().fromJson(ctx.body(), CreateRequest.class);
        try
        {
            CreateResponse createResponse = createGame.createGame(authToken, createRequest);
            ctx.result(new Gson().toJson(createResponse));
            ctx.status(200);
        }
        catch(BadRequestException | ResponseException | DataAccessException a)
        {
            ErrorResponse badReq = new ErrorResponse("Error: bad request");
            ctx.result(new Gson().toJson(badReq));
            ctx.status(400);
        }
        catch(UnauthorizedException e)
        {
            ErrorResponse taken = new ErrorResponse("Error: unauthorized");
            ctx.result(new Gson().toJson(taken));
            ctx.status(401);
        }

    }
}
