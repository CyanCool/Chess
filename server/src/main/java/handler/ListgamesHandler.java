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
    //private final ListGamesService listGamesService;

    public ListgamesHandler(MemoryUserDAO myData, MemoryAuthDAO myAuth, MemoryGameDAO myGame)
    {
       // listGamesService = new ListGamesService(myData, myAuth, myGame);
    }

    public void listgames(Context ctx) throws InvalidAuthDataException
    {
        ListgamesRequest listgamesRequest = new ListgamesRequest(ctx.header("authorization"));
        try
        {
            //ListgamesResponse listgamesResponse = listGamesService.list(listgamesRequest);
            //listGamesService.authorized(ctx); //figure out where to put this method
          //  ctx.result(new Gson().toJson(listgamesResponse));
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
