package handler;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.MemoryAuthDAO;
import dataaccess.UserMemoryDAO;
import io.javalin.http.Context;
import model.*;
import org.junit.jupiter.api.*;
import passoff.model.TestAuthResult;
import service.UserService;
import java.net.HttpURLConnection;
import java.util.*;
import exception.*;

public class LogoutHandler
{
    private final UserService userService;
    private final ArrayList<AuthData> validTokens;

    public LogoutHandler(UserMemoryDAO myData, MemoryAuthDAO myAuth)
    {
        userService = new UserService(myData, myAuth);
        validTokens = myAuth.getAllAuthData(); //does this make a copy or reference the original?
    }

    public void logout(Context ctx) throws InvalidAuthDataException, BadRequestException
    {
        //System.out.println(ctx.header("authorization"));
        if(userService.)
        LogoutRequest logoutRequest = new LogoutRequest(ctx.header("authorization"));
        try
        {
            LogoutResponse logoutResponse = userService.logout(logoutRequest);
            ctx.result(new Gson().toJson(logoutResponse));
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
            ErrorResponse authWrong = new ErrorResponse("Error: unauthorized");
            ctx.result(new Gson().toJson(authWrong));
            ctx.status(401);
        }
    }
    //System.out.println(ctx.header("authorization"));
    //authorization stores the authtoken
    //use it to get the authtoken
    //use the authtoken to search through the usernames and see if one matches
    //if it matches, then clear the auth data for them
    //if none of them do, throw an invalid authorization exception

}
