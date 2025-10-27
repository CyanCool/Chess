package handler;

import com.google.gson.Gson;
import exception.AlreadyTakenException; //need to implement
import model.Request;
import io.javalin.Javalin;
import io.javalin.http.Context;


public class RegisterHandler
{
    public RegisterHandler()
    {

    }

    public void register(Context ctx) throws AlreadyTakenException
    {
        registerRequest = new Gson().fromJson(ctx.body(), Request.class);
    }
}
