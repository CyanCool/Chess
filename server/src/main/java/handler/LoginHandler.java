package handler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import exception.*;
import io.javalin.http.Context;
import response.ErrorResponse;
import request.LoginRequest;
import response.LoginResponse;
import service.LoginService;

public class LoginHandler
{
    private final LoginService loginService;

    public LoginHandler(UserDAO myData, AuthDAO myAuth)
    {
        loginService = new LoginService(myData, myAuth);
    }

    public void login(Context ctx) throws UnauthorizedException, PasswordIncorrectException
    {
        LoginRequest loginRequest = new Gson().fromJson(ctx.body(), LoginRequest.class);

        try
        {
            //make a new loginResponse
            LoginResponse loginResponse = loginService.login(loginRequest);
            //update the context status and result
            ctx.result(new Gson().toJson(loginResponse));
            ctx.status(200);
        }
        catch(BlankFieldException | ResponseException | DataAccessException b)
        {
            ErrorResponse notExist = new ErrorResponse("Error: bad request");
            ctx.result(new Gson().toJson(notExist));
            ctx.status(400);
            System.out.println(ctx.status());
        }
        catch(PasswordIncorrectException | UnauthorizedException p)
        {
            //make a new ErrorResponse
            ErrorResponse passwordWrong = new ErrorResponse("Error: unauthorized");
            //update the context status and result
            ctx.result(new Gson().toJson(passwordWrong));
            ctx.status(401);
        }
    }
}
