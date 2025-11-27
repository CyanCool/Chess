package dataaccess;

import com.google.gson.Gson;
import exception.ResponseException;
import model.*;
import org.mindrot.jbcrypt.BCrypt;
import request.RegisterRequest;

import java.sql.*;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SQLUserDAO extends SQL implements UserDAO
{
    public SQLUserDAO() throws ResponseException, DataAccessException
    {
        configureDatabase(createStatements);
    }

    public void createUser(RegisterRequest userData) throws DataAccessException, ResponseException
    {
        String hashedPassword = storeUserPassword(userData.password());
        var statement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
        int id = executeUpdate(statement, userData.username(), hashedPassword, userData.email());
    }

    public UserData getUser(String username) throws ResponseException
    {
        try (Connection conn = DatabaseManager.getConnection())
        {
            var statement = "SELECT username,password,email FROM `user` WHERE username=?";
            try (PreparedStatement ps = conn.prepareStatement(statement))
            {
                ps.setString(1, username);
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        return readUser(rs);
                    }
                }
            }
        } catch (Exception e)
        {
            throw new ResponseException(ResponseException.Code.ServerError,
                    String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    private UserData readUser(ResultSet rs) throws SQLException
    {
        var username = rs.getString("username");
        var password = rs.getString("password");
        var email = rs.getString("email");
        return new UserData(username, password, email);
    }

    private String readPassword(ResultSet rs) throws SQLException
    {
        return rs.getString("password");
    }

    private final String[] createStatements =
    {        """
            CREATE TABLE IF NOT EXISTS  user (
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              PRIMARY KEY (`username`),
              INDEX(username),
              INDEX(password),
              INDEX(email)
            )
            """
    };

    public String storeUserPassword(String clearTextPassword)
    {
        return BCrypt.hashpw(clearTextPassword, BCrypt.gensalt());
    }

    public boolean verifyUser(String username, String providedClearTextPassword) throws ResponseException
    {
        String hashedPassword = readHashedPasswordFromDatabase(username);
        return BCrypt.checkpw(providedClearTextPassword, hashedPassword);
    }

    public String readHashedPasswordFromDatabase(String username) throws ResponseException
    {
        UserData myUser;
        try (Connection conn = DatabaseManager.getConnection())
        {
            var statement = "SELECT password FROM `user` WHERE username=?";
            try (PreparedStatement ps = conn.prepareStatement(statement))
            {
                ps.setString(1, username);
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        return readPassword(rs);
                    }
                }
            }
        } catch (Exception e)
        {
            throw new ResponseException(ResponseException.Code.ServerError,
                    String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }
}
