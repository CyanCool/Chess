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

    public LogoutHandler(UserMemoryDAO myData, MemoryAuthDAO myAuth)
    {
        userService = new UserService(myData, myAuth);
    }

    public void logout(Context ctx)throws InvalidAuthDataException
    {
        LogoutRequest logoutRequest = new Gson().fromJson(ctx.body(), LogoutRequest.class);
        try
        {
            LogoutResponse logoutResponse = userService.logout(logoutRequest);
            ctx.result(new Gson().toJson(logoutResponse));
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
