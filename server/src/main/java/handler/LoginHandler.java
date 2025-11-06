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
    private final UserService userService;

    public LoginHandler(UserService userService)
    {
        this.userService = userService;
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
        catch(BadRequestException b) //not really sure what error code to put in, matches the register exception but ended up being different
        {
            //make a new ErrorResponse
            ErrorResponse badRequest = new ErrorResponse("Error: one of the fields are blank");
            //update the context status and result
            ctx.result(new Gson().toJson(badRequest));
            ctx.status(500);
        }

    }
}
