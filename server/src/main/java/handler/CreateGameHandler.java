package handler;


import com.google.gson.Gson;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import exception.AlreadyTakenException;
import exception.BadRequestException;
import io.javalin.http.Context;
import model.*;
import service.CreateGameService;

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
        String gameName = ctx.body();

        CreateRequest createRequest = new CreateRequest(authToken, gameName); //this code may suck
        try
        {
            CreateResponse createResponse = createGame.createGame(createRequest);
            ctx.result(new Gson().toJson(createResponse));
            ctx.status(200);
        }
        catch(BadRequestException b)
        {
            ErrorResponse badReq = new ErrorResponse("Error: bad request");
            ctx.result(new Gson().toJson(badReq));
            ctx.status(400);
        }
        catch(AlreadyTakenException a)
        {
            ErrorResponse taken = new ErrorResponse("Error: unauthorized");
            ctx.result(new Gson().toJson(taken));
            ctx.status(401);
        }

    }
}
