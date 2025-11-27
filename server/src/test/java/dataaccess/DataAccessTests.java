package dataaccess;

import chess.ChessGame;
import exception.BadRequestException;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import passoff.model.*;
import passoff.server.TestServerFacade;
import request.LoginRequest;
import request.RegisterRequest;
import server.Server;

import java.lang.reflect.Method;
import java.sql.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DataAccessTests
{

    private UserDAO myUser;
    private AuthDAO myAuth;
    private GameDAO myGame;

    public DataAccessTests() throws ResponseException, DataAccessException
    {
        myUser = new SQLUserDAO();
        myAuth = new SQLAuthDAO();
        myGame = new SQLGameDAO();
    }

    @Test
    @DisplayName("UserDAO - Create User and Get User Test Failed")
    @Order(1)
    public void testUserDAOFailure() throws SQLException, DataAccessException
    {
        myUser.clearData();
        RegisterRequest myRequest = new RegisterRequest(null,"existingUserPassword", "eu@mail.com");
        Assertions.assertThrowsExactly(ResponseException.class, () -> {myUser.createUser(myRequest);});
    }

    @Test
    @DisplayName("UserDAO - Create User and Get User Test Successful")
    @Order(2)
    public void testUserDAOSuccess() throws DataAccessException, SQLException
    {
        myUser.clearData();
        RegisterRequest myRequest = new RegisterRequest("Homer", "doh", "marge@simpson.com");
        Assertions.assertDoesNotThrow(() -> myUser.createUser(myRequest));
        try
        {
            //We know that the username is equal if getUser retrieves it successfully, so we just have to test if the password and email are equal
            Assertions.assertTrue( myUser.verifyUser(myRequest.username(), myRequest.password()));
            Assertions.assertEquals(myUser.getClassInfo(myRequest.username()).email(), myRequest.email());
        }
        catch(ResponseException e)
        {
            Assertions.fail();
        }
    }

    @Test
    @DisplayName("UserDAO - ClearTable Success")
    @Order(3)
    public void testUserDAOClearSuccess() throws SQLException, DataAccessException
    {
        Assertions.assertDoesNotThrow(() -> {myUser.clearData();});
    }

    @Test
    @DisplayName("UserDAO - Get User Failure")
    @Order(4)
    public void testUserDAOGetUserFailure() throws DataAccessException, SQLException
    {
        myUser.clearData();
        try
        {
            Assertions.assertNull(myUser.getClassInfo("YourMom"));
        } catch (ResponseException e) {
            Assertions.fail();
        }
    }

    @Test
    @DisplayName("UserDAO - Wrong Password")
    @Order(5)
    public void testUserDAOVerifyWrongPassword() throws DataAccessException, SQLException, ResponseException
    {
        myUser.clearData();
        RegisterRequest req = new RegisterRequest("Bart", "eatmyshorts", "bart@simpson.com");
        myUser.createUser(req);

        Assertions.assertFalse(myUser.verifyUser("Bart", "wrongpassword"));
    }

    @Test
    @DisplayName("UserDAO - ReadHashedPassword Success")
    @Order(6)
    public void testUserDAOReadPasswordSuccess() throws DataAccessException, SQLException, ResponseException
    {
        myUser.clearData();
        RegisterRequest req = new RegisterRequest("Lisa", "saxophone", "lisa@simpson.com");
        myUser.createUser(req);

        Assertions.assertNotNull(myUser.readHashedPasswordFromDatabase("Lisa"));
    }

    @Test
    @DisplayName("UserDAO - ReadHashedPassword Failure")
    @Order(7)
    public void testUserDAOReadPasswordFailure()
    {
        Assertions.assertDoesNotThrow(() ->
        {
            String password = myUser.readHashedPasswordFromDatabase("GhostUser123");
            Assertions.assertNull(password);
        });
    }


    @Test
    @DisplayName("AuthDAO - Create and Get Auth Success")
    @Order(8)
    public void testauthDAOSuccess() throws SQLException, DataAccessException
    {
        LoginRequest myRequest = new LoginRequest("Homer", "doh");
        String storeToken = "";
        storeToken = Assertions.assertDoesNotThrow(() ->
                myAuth.createAuth(myRequest.username())
        );
        try
        {
            Assertions.assertNotNull(myAuth.getClassInfo(storeToken));
            Assertions.assertEquals(myAuth.getClassInfo(storeToken).username(), myRequest.username());
        }
        catch(ResponseException e)
        {
            Assertions.fail();
        }
    }

    @Test
    @DisplayName("AuthDAO - Create and Get Auth Failure")
    @Order(9)
    public void testauthDAOFailure() throws SQLException, DataAccessException
    {
        LoginRequest myRequest = new LoginRequest(null, "doh");
        Assertions.assertThrowsExactly(ResponseException.class, () -> {myAuth.createAuth(myRequest.username());});
        Assertions.assertDoesNotThrow(() -> {myAuth.getClassInfo("supercalafregaliciousexplialadocious");});
    }

    @Test
    @DisplayName("AuthDAO - Get ArrayList Auth Success")
    @Order(10)
    public void testAuthArrayListDAOSuccess() throws SQLException, DataAccessException
    {
        Assertions.assertDoesNotThrow(() -> {myAuth.getAllAuthData();});
        ArrayList<AuthData> myData = new ArrayList<>();
        myData = Assertions.assertDoesNotThrow(() ->
                myAuth.getAllAuthData()
        );
        System.out.println(myData.toString()); //check if the Auth Data is stored correctly
    }

    @Test
    @DisplayName("AuthDAO - ClearTable Success")
    @Order(11)
    public void testAuthDAOClearSuccess() throws SQLException, DataAccessException
    {
        Assertions.assertDoesNotThrow(() -> {myAuth.clearData();});
    }

    @Test
    @DisplayName("AuthDAO - Token Not Found")
    @Order(12)
    public void testAuthDAOGetFailure()
    {
        Assertions.assertDoesNotThrow(() ->
        {
            Assertions.assertNull(myAuth.getClassInfo("notARealToken"));
        });
    }

    @Test
    @DisplayName("AuthDAO - GetAllAuth Empty Table Success")
    @Order(13)
    public void testAuthDAOEmptyList() throws DataAccessException, SQLException
    {
        myAuth.clearData();
        ArrayList<AuthData> list = Assertions.assertDoesNotThrow(() -> myAuth.getAllAuthData());
        Assertions.assertEquals(0, list.size());
    }

    @Test
    @DisplayName("AuthDAO - Remove Auth Success")
    @Order(14)
    public void testAuthDAORemoveSuccess() throws DataAccessException, ResponseException, SQLException
    {
        myAuth.clearData();
        String token = myAuth.createAuth("Marge");
        AuthData myData = new AuthData(token, "Marge");

        Assertions.assertDoesNotThrow(() -> myAuth.remove(myData));
        Assertions.assertNull(myAuth.getClassInfo(token));
    }

    @Test
    @DisplayName("AuthDAO - Token not found")
    @Order(15)
    public void testAuthDAORemoveFailure()
    {
        AuthData fake = new AuthData("69678", "Nobody");
        Assertions.assertThrowsExactly(ResponseException.class, () ->
        {
            myAuth.remove(fake);
        });
    }


    @Test
    @DisplayName("AuthDAO - ClearTable Success")
    @Order(16)
    public void testGameDAOClearSuccess() throws SQLException, DataAccessException
    {
        Assertions.assertDoesNotThrow(() -> {myGame.clearData();});
    }

    @Test
    @DisplayName("GameDAO - Create and Get Game Success")
    @Order(17)
    public void testGameDAOSuccess() throws SQLException, DataAccessException
    {
        myGame.clearData();
        int storeID;
        storeID = Assertions.assertDoesNotThrow(() ->
                myGame.createGame("Bodalicious")
        );
        try
        {
            Assertions.assertNotNull(myGame.getClassInfo("Bodalicious").gameName());
            Assertions.assertNotNull(myGame.getClassInfo(storeID).gameName());
        }
        catch(ResponseException e)
        {
            Assertions.fail();
        }
    }

    @Test
    @DisplayName("GameDAO- Create and Get Game Failure")
    @Order(18)
    public void testGameDAOFailure() throws SQLException, DataAccessException, ResponseException
    {
        Assertions.assertThrowsExactly(ResponseException.class, () -> {myGame.createGame(null);});
        Assertions.assertNull(myGame.getClassInfo(5757));
    }

    @Test
    @DisplayName("GameDAO - Get ArrayList Game Success")
    @Order(19)
    public void testGameArrayListDAOSuccess() throws SQLException, DataAccessException
    {
        Assertions.assertDoesNotThrow(() -> {myGame.getList();});
        ArrayList<GameData> myData = new ArrayList<>();
        myData = Assertions.assertDoesNotThrow(() ->
                myGame.getList()
        );
        System.out.println(myData.toString()); //check if the Game Data is stored correctly
    }

    @Test
    @DisplayName("GameDAO - Get ArrayList Game Failure")
    @Order(20)
    public void testGameArrayListDAOFailure() throws SQLException, DataAccessException, ResponseException
    {
        myGame.clearData();
        try
        {
            ArrayList<GameData> myData = myGame.getList();
            Assertions.assertEquals(myData.size(),0);
        }
        catch(ResponseException e)
        {
            Assertions.fail();
        }
    }

    @Test
    @DisplayName("GameDAO - test UpdateGameSuccess")
    @Order(21)
    public void testGameUpdateDAOSuccess() throws SQLException, DataAccessException, ResponseException
    {
        int storeID;
        storeID = Assertions.assertDoesNotThrow(() ->
                myGame.createGame("Life")
        );
        Assertions.assertDoesNotThrow(() -> {myGame.updateGame(storeID,"WHITE","julianna");});
        Assertions.assertNotNull(myGame.getClassInfo(storeID).whiteUsername());
        Assertions.assertNull(myGame.getClassInfo(storeID).blackUsername());
    }

    @Test
    @DisplayName("GameDAO - test UpdateGameFailure")
    @Order(22)
    public void testGameUpdateDAOFailure() throws SQLException, DataAccessException, ResponseException
    {
        int storeID;
        storeID = Assertions.assertDoesNotThrow(() ->
                myGame.createGame("Life")
        );
        Assertions.assertThrowsExactly(ResponseException.class, () -> {myGame.updateGame(999999,"WHITE","julianna");});
        Assertions.assertThrowsExactly(BadRequestException.class, () -> {myGame.updateGame(storeID,"PURPLE","julianna");});
    }

    @Test
    @DisplayName("GameDAO - Get Game By Name Failure")
    @Order(23)
    public void testGameDAOGetByNameFailure()
    {
        Assertions.assertDoesNotThrow(() -> {Assertions.assertNull(myGame.getClassInfo("ThisGameDoesNotExist"));});
    }

    @Test
    @DisplayName("GameDAO - Get Game By ID Failure")
    @Order(24)
    public void testGameDAOGetByIDFailure()
    {
        Assertions.assertDoesNotThrow(() -> {Assertions.assertNull(myGame.getClassInfo(123456789));});
    }


    @FunctionalInterface
    private interface TableAction
    {
        void execute(String tableName, Connection connection) throws SQLException;
    }

}
