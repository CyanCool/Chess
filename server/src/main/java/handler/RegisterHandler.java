package handler;

import com.google.gson.Gson;
//import exception.AlreadyTakenException; //need to implement
import exception.*;
import model.ErrorResponse;
import model.RegisterRequest;
import model.RegisterResponse;
import io.javalin.Javalin;
import io.javalin.http.Context;
import service.UserService;


public class RegisterHandler
{
    private final UserService userService;
    public RegisterHandler()
    {
        userService = new UserService();
    }

    public void register(Context ctx) throws AlreadyTakenException, BadRequestException
    {
        RegisterRequest registerRequest = new Gson().fromJson(ctx.body(), RegisterRequest.class);
        try
        {
            RegisterResponse registerResponse = userService.register(registerRequest);
            ctx.result(new Gson().toJson(registerResponse));
            ctx.status(200);
        }
        catch(BadRequestException b)
        {
            ErrorResponse badRequest = new ErrorResponse("Error: bad request");
            ctx.result(new Gson().toJson(badRequest));
            System.err.println("Error: One of the required fields to register are blank.");
            ctx.status(400);
        }
        catch(AlreadyTakenException e)
        {
            ErrorResponse alreadyTaken = new ErrorResponse("Error: already taken");
            ctx.result(new Gson().toJson(alreadyTaken));
            //System.out.println(ctx.result());
            System.err.println("Error: This Username is Already Taken.");
            ctx.status(403);
        }
    }
}
