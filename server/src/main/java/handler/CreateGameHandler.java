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
        createGame = new CreateGameService(myAuth, myGame);
    }

    public void create(Context ctx)
    {
        String authToken = ctx.header("authorization");
        System.out.println(ctx.body());
        HashMap<String, String> getJSONBody = new Gson().fromJson(ctx.body(), HashMap.class);
        String body = getJSONBody.toString();
        if(body.equals("{}"))
        {
            body = null;
        }
        CreateRequest createRequest = new CreateRequest(authToken, body);
        try
        {
            createGame.verifyAuth(authToken);
            CreateResponse createResponse = createGame.createGame(createRequest);
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
