package handler;

import com.google.gson.Gson;
import dataaccess.MemoryAuthDAO;
import dataaccess.UserMemoryDAO;
import exception.*;
import io.javalin.Javalin;
import io.javalin.http.Context;
import model.ErrorResponse;
import model.LoginRequest;
import model.LoginResponse;
import model.RegisterRequest;
import service.UserService;

public class LoginHandler
{
    private final UserService userService;

    public LoginHandler(UserMemoryDAO myData, MemoryAuthDAO myAuth)
    {
        userService = new UserService(myData, myAuth);
    }

    public void login(Context ctx) throws DoesNotExistException, PasswordIncorrectException
    {
        LoginRequest loginRequest = new Gson().fromJson(ctx.body(), LoginRequest.class);

        try
        {
            //make a new loginResponse
            LoginResponse loginResponse = userService.login(loginRequest);
            //update the context status and result
            ctx.result(new Gson().toJson(loginResponse));
            ctx.status(200);
        }
        catch(BlankFieldException b)
        {
            ErrorResponse notExist = new ErrorResponse("Error: bad request");
            ctx.result(new Gson().toJson(notExist));
            ctx.status(400);
            System.out.println(ctx.status());
        }
        catch(PasswordIncorrectException|DoesNotExistException p)
        {
            //make a new ErrorResponse
            ErrorResponse passwordWrong = new ErrorResponse("Error: unauthorized");
            //update the context status and result
            ctx.result(new Gson().toJson(passwordWrong));
            ctx.status(401);
        }
    }
}
