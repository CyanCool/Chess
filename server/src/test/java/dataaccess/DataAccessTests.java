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

    private SQLUserDAO myUser;
    private SQLAuthDAO myAuth;
    private SQLGameDAO myGame;

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
    @DisplayName("AuthDAO - Create and Get Auth Success")
    @Order(4)
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
    @Order(5)
    public void testauthDAOFailure() throws SQLException, DataAccessException
    {
        LoginRequest myRequest = new LoginRequest(null, "doh");
        Assertions.assertThrowsExactly(ResponseException.class, () -> {myAuth.createAuth(myRequest.username());});
        Assertions.assertDoesNotThrow(() -> {myAuth.getClassInfo("supercalafregaliciousexplialadocious");});
    }

    @Test
    @DisplayName("AuthDAO - Get ArrayList Auth Success")
    @Order(6)
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
    @Order(7)
    public void testAuthDAOClearSuccess() throws SQLException, DataAccessException
    {
        Assertions.assertDoesNotThrow(() -> {myAuth.clearData();});
    }

    @Test
    @DisplayName("AuthDAO - ClearTable Success")
    @Order(8)
    public void testGameDAOClearSuccess() throws SQLException, DataAccessException
    {
        Assertions.assertDoesNotThrow(() -> {myGame.clearData();});
    }

    @Test
    @DisplayName("GameDAO - Create and Get Game Success")
    @Order(9)
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
            Assertions.assertNotNull(myGame.getGame(storeID).gameName());
        }
        catch(ResponseException e)
        {
            Assertions.fail();
        }
    }

    @Test
    @DisplayName("GameDAO- Create and Get Game Failure")
    @Order(10)
    public void testGameDAOFailure() throws SQLException, DataAccessException, ResponseException
    {
        Assertions.assertThrowsExactly(ResponseException.class, () -> {myGame.createGame(null);});
        Assertions.assertNull(myGame.getGame(5757));
    }

    @Test
    @DisplayName("GameDAO - Get ArrayList Game Success")
    @Order(11)
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
    @Order(12)
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
    @Order(13)
    public void testGameUpdateDAOSuccess() throws SQLException, DataAccessException, ResponseException
    {
        int storeID;
        storeID = Assertions.assertDoesNotThrow(() ->
                myGame.createGame("Life")
        );
        Assertions.assertDoesNotThrow(() -> {myGame.updateGame(storeID,"WHITE","julianna");});
        Assertions.assertNotNull(myGame.getGame(storeID).whiteUsername());
        Assertions.assertNull(myGame.getGame(storeID).blackUsername());
    }

    @Test
    @DisplayName("GameDAO - test UpdateGameFailure")
    @Order(14)
    public void testGameUpdateDAOFailure() throws SQLException, DataAccessException, ResponseException
    {
        int storeID;
        storeID = Assertions.assertDoesNotThrow(() ->
                myGame.createGame("Life")
        );
        Assertions.assertThrowsExactly(ResponseException.class, () -> {myGame.updateGame(999999,"WHITE","julianna");});
        Assertions.assertThrowsExactly(BadRequestException.class, () -> {myGame.updateGame(storeID,"PURPLE","julianna");});
    }

    @FunctionalInterface
    private interface TableAction
    {
        void execute(String tableName, Connection connection) throws SQLException;
    }

}
