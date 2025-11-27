package handler;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import io.javalin.http.Context;
import exception.*;
import request.ListgamesRequest;
import response.ErrorResponse;
import response.ListgamesResponse;
import service.ListGamesService;

public class ListgamesHandler
{
    private  ListGamesService listGamesService;

    public ListgamesHandler(AuthDAO myAuth, GameDAO myGame)
    {
       listGamesService = new ListGamesService(myAuth, myGame);
    }

    public void listgames(Context ctx) throws BadRequestException, UnauthorizedException
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
        catch(UnauthorizedException e)
        {
            ErrorResponse authWrong = new ErrorResponse("Error: unauthorized");
            ctx.result(new Gson().toJson(authWrong));
            ctx.status(401);
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
