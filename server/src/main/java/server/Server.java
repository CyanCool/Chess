package server;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import handler.*;
import io.javalin.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Server
{

    private final Javalin server;
    private final RegisterHandler registerHandler;
    private final LoginHandler loginHandler;
    private final LogoutHandler logoutHandler;
    private final ListgamesHandler listgamesHandler;
    private final CreateGameHandler createGameHandler;
    private final ClearHandler clearHandler;
    private final JoinGameHandler joinGameHandler;

    private final MemoryUserDAO userMemory;
    private final MemoryAuthDAO authMemory;
    private final MemoryGameDAO gameMemory;

    public Server()
    {
        userMemory = new MemoryUserDAO();
        authMemory = new MemoryAuthDAO(); //makes the memory global
        gameMemory = new MemoryGameDAO();

        registerHandler = new RegisterHandler(userMemory, authMemory);
        loginHandler = new LoginHandler(userMemory, authMemory);
        logoutHandler = new LogoutHandler(userMemory, authMemory);
        createGameHandler = new CreateGameHandler(authMemory, gameMemory);
        clearHandler = new ClearHandler(userMemory, authMemory, gameMemory);
        joinGameHandler = new JoinGameHandler(authMemory, gameMemory);
        listgamesHandler = new ListgamesHandler(authMemory, gameMemory);

        //server.delete("db", ctx -> ctx.result("{}")); //old clear method
        server = Javalin.create(config -> config.staticFiles.add("web"));
        server.delete("db", clearHandler::clear);


        server.post("user", registerHandler::register);
        server.post("session", loginHandler::login);
        server.delete("session", logoutHandler::logout);
        server.post("game", createGameHandler::create);
        server.put("game", joinGameHandler::join);
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


