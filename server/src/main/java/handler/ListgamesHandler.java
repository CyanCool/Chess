package handler;

import com.google.gson.Gson;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import io.javalin.http.Context;
import model.*;
import exception.*;
import service.ListGamesService;

public class ListgamesHandler
{
    private  ListGamesService listGamesService;

    public ListgamesHandler(MemoryAuthDAO myAuth, MemoryGameDAO myGame)
    {
       listGamesService = new ListGamesService(myAuth, myGame);
    }

    public void listgames(Context ctx) throws BadRequestException, DoesNotExistException
    {
        ListgamesRequest listgamesRequest = new ListgamesRequest(ctx.header("authorization"));
        try
        {
            ListgamesResponse listgamesResponse = listGamesService.listGames(listgamesRequest);
            ctx.result(new Gson().toJson(listgamesResponse));
            ctx.status(200);
        }
        catch(BadRequestException b)
        {
            ErrorResponse badReq = new ErrorResponse("Error: bad request");
            ctx.result(new Gson().toJson(badReq));
            ctx.status(400);
        }
        catch(DoesNotExistException e)
        {
            ErrorResponse authWrong = new ErrorResponse("Error: unauthorized");
            ctx.result(new Gson().toJson(authWrong));
            ctx.status(401);
        }
    }
}
