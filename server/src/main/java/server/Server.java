package server;

import handler.LoginHandler;
import handler.RegisterHandler;
import io.javalin.*;
import com.google.gson.Gson;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;

import java.util.Map;

public class Server
{

    private static final Logger log = LoggerFactory.getLogger(Server.class);
    private final Javalin server;
    private final RegisterHandler registerHandler;
    private final LoginHandler loginHandler;
    private final UserService userService;

    public Server()
    {
        server = Javalin.create(config -> config.staticFiles.add("web"));
        server.delete("db", ctx -> ctx.result("{}"));

        userService = new UserService();

        registerHandler = new RegisterHandler(userService);
        loginHandler = new LoginHandler(userService);

        server.post("user", registerHandler::register);
        server.post("session", loginHandler::login);



        //{\"username\":\joe\", \"authToken\":\"xyz\"}"

        // Register your endpoints and exception handlers here.

    }

    public int run(int desiredPort)
    {
        server.start(desiredPort);
        return server.port();
    }

    public void stop() {
        server.stop();
    }


}


