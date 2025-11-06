package server;

import handler.RegisterHandler;
import io.javalin.*;
import com.google.gson.Gson;
import io.javalin.http.Context;

import java.util.Map;

public class Server
{

    private final Javalin server;
    private final RegisterHandler registerHandler;

    public Server()
    {
        server = Javalin.create(config -> config.staticFiles.add("web"));

        server.delete("db", ctx -> ctx.result("{}"));

        registerHandler = new RegisterHandler();

        server.post("user", registerHandler::register);

        server.post("session", loginHandler::login);



        //{\"username\":\joe\", \"authToken\":\"xyz\"}"

        // Register your endpoints and exception handlers here.

    }

    public int run(int desiredPort) {
        server.start(desiredPort);
        return server.port();
    }

    public void stop() {
        server.stop();
    }


}


