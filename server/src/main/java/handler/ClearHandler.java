package handler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import exception.InaccessibleConnection;
import exception.ResponseException;
import request.DeleteRequest;
import response.DeleteResponse;
import response.ErrorResponse;
import service.ClearService;
import io.javalin.http.Context;

import java.sql.SQLException;

public class ClearHandler
{
    private final ClearService clearService;

    public ClearHandler(UserDAO myData, AuthDAO myAuth, GameDAO myGame)
    {
        clearService = new ClearService(myData, myAuth, myGame);
    }

    public void clear(Context ctx) throws SQLException, DataAccessException
    {
        DeleteRequest deleteRequest = new DeleteRequest();
        try
        {
            DeleteResponse deleteResponse = clearService.clear(deleteRequest);
            ctx.result(new Gson().toJson(deleteResponse));
            ctx.status(200);
        }
        catch(ResponseException e)
        {
            ErrorResponse notAccessible = new ErrorResponse("Error: Could not create connection to database server");
            ctx.result(new Gson().toJson(notAccessible));
            ctx.status(500);
        }
    }
}
