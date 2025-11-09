package server;

import dataaccess.MemoryAuthDAO;
import dataaccess.UserMemoryDAO;
import handler.LoginHandler;
import handler.LogoutHandler;
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
    private final LogoutHandler logoutHandler;
    private final UserMemoryDAO userMemory;
    private final MemoryAuthDAO authMemory;

    public Server()
    {
        server = Javalin.create(config -> config.staticFiles.add("web"));
        server.delete("db", ctx -> ctx.result("{}"));

        userMemory = new UserMemoryDAO();
        authMemory = new MemoryAuthDAO(); //makes the memory global

        registerHandler = new RegisterHandler(userMemory, authMemory);
        loginHandler = new LoginHandler(userMemory, authMemory);
        logoutHandler = new LogoutHandler(userMemory, authMemory);

        server.post("user", registerHandler::register);
        server.post("session", loginHandler::login);
        server.delete("session", logoutHandler::logout);
        server.get("game", listgamesHandler::listgames);



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


