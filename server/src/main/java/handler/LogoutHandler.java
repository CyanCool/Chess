package handler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import io.javalin.http.Context;
import exception.*;
import request.LogoutRequest;
import response.ErrorResponse;
import response.LogoutResponse;
import service.LogoutService;

public class LogoutHandler
{
    private final LogoutService logoutService;

    public LogoutHandler(UserDAO myData, AuthDAO myAuth)
    {
        logoutService = new LogoutService(myData, myAuth);
    }

    public void logout(Context ctx) throws InvalidAuthDataException, BadRequestException
    {
        LogoutRequest logoutRequest = new LogoutRequest(ctx.header("authorization"));
        try
        {
            LogoutResponse logoutResponse = logoutService.logout(logoutRequest);
            //userService.authorized(ctx); //figure out where to put this method
            ctx.result(new Gson().toJson(logoutResponse));
            ctx.status(200);
        }
        catch(BadRequestException | DataAccessException b)
        {
            ErrorResponse badReq = new ErrorResponse("Error: bad request");
            ctx.result(new Gson().toJson(badReq));
            ctx.status(400);
        }
        catch(InvalidAuthDataException i)
        {
            ErrorResponse authWrong = new ErrorResponse("Error: unauthorized");
            ctx.result(new Gson().toJson(authWrong));
            ctx.status(401);
        }
        catch(ResponseException e)
        {
            ErrorResponse serverIssue = new ErrorResponse("Error: Server Issue");
            ctx.result(new Gson().toJson(serverIssue));
            System.err.println("Error: The server is having an issue logging out.");
            ctx.status(e.toHttpStatusCode());
        }
    }

}
