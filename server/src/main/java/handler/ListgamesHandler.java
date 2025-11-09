package handler;

import com.google.gson.Gson;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import io.javalin.http.Context;
import model.*;
import service.GameService;
import exception.*;

public class ListgamesHandler
{
    private final GameService gameService;

    public ListgamesHandler(MemoryUserDAO myData, MemoryAuthDAO myAuth)
    {
        gameService = new GameService(myData, myAuth);
    }

    public void listgames(Context ctx) throws InvalidAuthDataException
    {
        ListgamesRequest listgamesRequest = new ListgamesRequest(ctx.header("authorization"));
        try
        {
            ListgamesResponse listgamesResponse = gameService.list(listgamesRequest);
            gameService.authorized(ctx); //take out if broken
            ctx.result(new Gson().toJson(listgamesResponse));
            ctx.status(200);
        }
        catch(InvalidAuthDataException i)
        {
            ErrorResponse authWrong = new ErrorResponse("Error: unauthorized");
            ctx.result(new Gson().toJson(authWrong));
            ctx.status(401);
        }
    }
}
