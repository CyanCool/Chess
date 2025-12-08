package client;

import exception.BadRequestException;
import exception.InvalidEmailException;
import exception.ResponseException;
import exception.WrongNumberOfArgumentsException;
import org.junit.jupiter.api.*;
import server.Server;
import ui.ServerFacade;


public class ServerFacadeTests
{
    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init()
    {
        server = new Server();
        facade = new ServerFacade("http://localhost:7777");

        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    @Order(1)
    @DisplayName("Register - Successful User Registration")
    public void registerSuccess() throws ResponseException
    {
        String[] userInfo = {"Phineas", "Where'sPerry", "Phineas@gmail.com"};
        Assertions.assertDoesNotThrow(() -> {facade.register(userInfo);});
    }

    @Test
    @Order(2)
    @DisplayName("Register - Unsuccessful User Registration")
    public void registerFailure() throws ResponseException
    {
        //Wrong Number of Arguments
        String[] userInfo0 = {"Phineas", "Where'sPerry", "Phineasgmail.com","Candace"};
        Assertions.assertThrowsExactly(WrongNumberOfArgumentsException.class , () -> {facade.register(userInfo0);});

        //Bad Email
        String[] userInfo1 = {"Phineas", "Where'sPerry", "Phineasgmail.com"};
        Assertions.assertThrowsExactly(InvalidEmailException.class , () -> {facade.register(userInfo1);});

        //Null Username
        String[] userInfo2 = {null, "Where'sPerry", "Phineas@gmail.com"};
        Assertions.assertThrowsExactly(NullPointerException.class, () -> {facade.register(userInfo2);});

        //Blank Username
        String[] userInfo3 = {"      ", "Where'sPerry", "Phineas@gmail.com"};
        Assertions.assertThrowsExactly(NullPointerException.class, () -> {facade.register(userInfo3);}); //Maybe make a seperate exception for blank
    }

    @Test
    @Order(3)
    @DisplayName("Log-in - Successful User Log-in")
    public void loginSuccess() throws ResponseException
    {
        String[] userInfo = {"Phineas", "Where'sPerry"};
        Assertions.assertDoesNotThrow(() -> {facade.login(userInfo);});
    }

    @Test
    @Order(4)
    @DisplayName("Log-in - Unsuccessful User Log-in")
    public void loginFailure() throws ResponseException
    {
        //Wrong number of arguments
        String[] userInfo1 = {"Phineas", "Where'sPerry", "Phineasgmail.com"};
        Assertions.assertThrowsExactly(WrongNumberOfArgumentsException.class , () -> {facade.login(userInfo1);});

        //Null Username
        String[] userInfo2 = {null, "Where'sPerry"};
        Assertions.assertThrowsExactly(NullPointerException.class, () -> {facade.login(userInfo2);});

        //Blank Username
        String[] userInfo3 = {"      ", "Where'sPerry"};
        Assertions.assertThrowsExactly(NullPointerException.class, () -> {facade.login(userInfo3);}); //Maybe make a seperate exception for blank
    }

}
