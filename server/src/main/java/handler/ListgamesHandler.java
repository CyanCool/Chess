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

public class ListgamesHandler
{
    private final UserService userService;

    public ListgamesHandler(UserMemoryDAO myData, MemoryAuthDAO myAuth)
    {
        userService = new UserService(myData, myAuth);
    }

    public void listgames(Context ctx) throws InvalidAuthDataException
    {
        ListgamesRequest listgamesRequest = new ListgamesRequest(ctx.header("authorization"));
        try
        {

        }
        catch(InvalidAuthDataException i)
        {
            ErrorResponse authWrong = new ErrorResponse("Error: unauthorized");
            ctx.result(new Gson().toJson(authWrong));
            ctx.status(401);
        }
    }
}
