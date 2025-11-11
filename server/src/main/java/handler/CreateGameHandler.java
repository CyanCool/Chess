package handler;


import com.google.gson.Gson;
import com.google.gson.JsonParser;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import exception.AlreadyTakenException;
import exception.BadRequestException;
import exception.BlankFieldException;
import exception.DoesNotExistException;
import io.javalin.http.Context;
import model.*;
import service.CreateGameService;

import java.util.HashMap;

public class CreateGameHandler
{
    private final CreateGameService createGame;

    public CreateGameHandler(MemoryAuthDAO myAuth, MemoryGameDAO myGame)
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
        catch(BadRequestException|BlankFieldException b)
        {
            ErrorResponse badReq = new ErrorResponse("Error: bad request");
            ctx.result(new Gson().toJson(badReq));
            ctx.status(400);
        }
        catch(AlreadyTakenException | DoesNotExistException a)
        {
            ErrorResponse taken = new ErrorResponse("Error: unauthorized");
            ctx.result(new Gson().toJson(taken));
            ctx.status(401);
        }

    }
}
