package handler;

import com.google.gson.Gson;
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
    private UserService userService;

    public LoginHandler()
    {
        userService = new UserService();
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
        catch(DoesNotExistException d)
        {
            //make a new ErrorResponse
            ErrorResponse notExist = new ErrorResponse("Error: bad request");
            //update the context status and result
            ctx.result(new Gson().toJson(notExist));
            ctx.status(400);
        }
        catch(PasswordIncorrectException p)
        {
            //make a new ErrorResponse
            ErrorResponse passwordWrong = new ErrorResponse("Error: unauthorized");
            //update the context status and result
            ctx.result(new Gson().toJson(passwordWrong));
            ctx.status(401);
        }

    }
}
