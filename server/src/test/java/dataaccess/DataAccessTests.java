package dataaccess;

import chess.ChessGame;
import exception.BadRequestException;
import exception.ResponseException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import passoff.model.*;
import passoff.server.TestServerFacade;
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

    private TestUser TEST_USER;
    private SQLUserDAO myUser;

    public DataAccessTests() throws ResponseException, DataAccessException
    {
        TEST_USER = new TestUser("Jenneth", "ILikeToMakeJam", "jen@mail.com");
        myUser = new SQLUserDAO();
    }

    @Test
    @DisplayName("UserDAO - Create User and Get User Test Successful")
    @Order(1)
    public void testUserDAOSuccess() throws DataAccessException, SQLException
    {
        myUser.clearTableData();
        RegisterRequest myRequest = new RegisterRequest("Homer", "doh", "marge@simpson.com");
        Assertions.assertDoesNotThrow(() -> myUser.createUser(myRequest));
        try
        {
            //We know that the username is equal if getUser retrieves it successfully, so we just have to test if the password and email are equal
            Assertions.assertEquals(myUser.getUser(myRequest.username()).password(), myRequest.password());
            Assertions.assertEquals(myUser.getUser(myRequest.username()).email(), myRequest.email());
        }
        catch(ResponseException e)
        {
            Assertions.fail();
        }
    }

    @Test
    @DisplayName("UserDAO - Create User and Get User Test Failed")
    @Order(2)
    public void testUserDAOFailure()
    {
        RegisterRequest myRequest = new RegisterRequest(null,"existingUserPassword", "eu@mail.com");
        Assertions.assertThrowsExactly(ResponseException.class, () -> {myUser.createUser(myRequest);});
    }


    @FunctionalInterface
    private interface TableAction
    {
        void execute(String tableName, Connection connection) throws SQLException;
    }

}
