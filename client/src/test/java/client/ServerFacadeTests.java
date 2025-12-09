package client;

import dataaccess.DataAccessException;
import exception.*;
import org.junit.jupiter.api.*;
import server.Server;
import ui.ServerFacade;

import java.sql.SQLException;


public class ServerFacadeTests
{
    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init()
    {
        server = new Server();
        var port = server.run(7777);
        facade = new ServerFacade("http://localhost:"+port);
        System.out.println("Started test HTTP server on " + port);
    }

    @BeforeEach
    public void clear() throws SQLException, DataAccessException
    {
        server.clear();
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

        //Invalid Characters
        String[] userInfo4 = {"%***<<<<", "Where'sPerry", "Phineas@gmail.com"};
        Assertions.assertThrowsExactly(InvalidCharacterException.class, () -> {facade.register(userInfo4);});
    }

    @Test
    @Order(3)
    @DisplayName("Log-in - Successful User Log-in")
    public void loginSuccess() throws ResponseException
    {
        String[] userInfo = {"Phineas", "Where'sPerry", "Phineas@gmail.com"};
        facade.register(userInfo);

        String[] userInfo2 = {"Phineas", "Where'sPerry"};
        Assertions.assertDoesNotThrow(() -> {facade.login(userInfo2);});
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

        //Invalid Characters
        String[] userInfo4 = {"%***<<<<", "Where'sPerry"};
        Assertions.assertThrowsExactly(InvalidCharacterException.class, () -> {facade.login(userInfo4);});
    }

    @Test
    @Order(5)
    @DisplayName("Log-out - Successful User Log-out")
    public void logoutSuccess() throws ResponseException
    {
        String[] userInfo = {"Phineas", "Where'sPerry", "Phineas@gmail.com"};
        facade.register(userInfo);

        String[] userInfo2 = {"Phineas", "Where'sPerry"};
        Assertions.assertDoesNotThrow(() -> {facade.login(userInfo2);});

        Assertions.assertDoesNotThrow(() -> {facade.logout();});
    }

    @Test
    @Order(6)
    @DisplayName("Log-out - Unsuccessful User Log-out")
    public void logoutFailure() throws ResponseException
    {
        Assertions.assertThrowsExactly(NullPointerException.class , () -> {facade.logout();});
    }

    @Test
    @Order(7)
    @DisplayName("Create Game - Successful")
    public void createSuccess() throws ResponseException
    {
        String[] userInfo = {"Phineas", "Where'sPerry", "Phineas@gmail.com"};
        facade.register(userInfo);

        String[] userInfo2 = {"Phineas", "Where'sPerry"};
        Assertions.assertDoesNotThrow(() -> {facade.login(userInfo2);});

        Assertions.assertDoesNotThrow(() -> {facade.createGame(new String[]{"Steven"});});
    }

    @Test
    @Order(8)
    @DisplayName("Create Game - Unsuccessful")
    public void createFailure() throws ResponseException
    {
        //Wrong number of arguments
        String[] userInfo1 = {"myGame", "Steven"};
        Assertions.assertThrowsExactly(WrongNumberOfArgumentsException.class , () -> {facade.createGame(userInfo1);});

        //Null Game Name
        String[] userInfo2 = {null};
        Assertions.assertThrowsExactly(NullPointerException.class, () -> {facade.createGame(userInfo2);});

        //Blank Game Name
        String[] userInfo3 = {"      "};
        Assertions.assertThrowsExactly(NullPointerException.class, () -> {facade.createGame(userInfo3);}); //Maybe make a seperate exception for blank

        //Invalid Characters
        String[] userInfo4 = {"%***<<<<"};
        Assertions.assertThrowsExactly(InvalidCharacterException.class, () -> {facade.createGame(userInfo4);});
    }

    @Test
    @Order(9)
    @DisplayName("List Games - Successful")
    public void listSuccess() throws ResponseException
    {
        loginSuccess();

        Assertions.assertDoesNotThrow(() -> {facade.createGame(new String[]{"Steven"});});

        Assertions.assertDoesNotThrow(() -> {facade.listGames();});
        int a = 1;
    }

    @Test
    @Order(9)
    @DisplayName("List Games - Unsuccessful")
    public void listFailure() throws ResponseException
    {
        Assertions.assertThrowsExactly(NullPointerException.class , () -> {facade.listGames();});
    }
}
