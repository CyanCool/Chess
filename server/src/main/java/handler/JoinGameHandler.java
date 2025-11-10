package handler;

import com.google.gson.Gson;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import exception.AlreadyTakenException;
import exception.BadRequestException;
import exception.InvalidAuthDataException;
import io.javalin.http.Context;
import model.*;
import service.JoinGameService;

import java.util.HashMap;

public class JoinGameHandler
{
    JoinGameService joinGame;
    public JoinGameHandler(MemoryAuthDAO myAuth, MemoryGameDAO myGame)
    {
        joinGame = new JoinGameService(myAuth, myGame);
    }

    public void join(Context ctx)
    {
        String authToken = ctx.header("authorization");
        HashMap<String, String> getJSONBody = new Gson().fromJson(ctx.body(), HashMap.class);
        String body = getJSONBody.toString();
        System.out.println(body);
        if(body.equals("{}"))
        {
            body = null;
        }
        JoinGameRequest joinGameRequest = new JoinGameRequest(authToken, body, body);//placeholder
        try
        {
            joinGame.verifyAuth(authToken);
            JoinGameResponse joinGameResponse = joinGame.join(joinGameRequest);
            ctx.result(new Gson().toJson(joinGameResponse));
            ctx.status(200);
        }
        catch(BadRequestException b)
        {
            ErrorResponse badReq = new ErrorResponse("Error: bad request");
            ctx.result(new Gson().toJson(badReq));
            ctx.status(400);
        }
        catch(InvalidAuthDataException i)
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
    }
}
