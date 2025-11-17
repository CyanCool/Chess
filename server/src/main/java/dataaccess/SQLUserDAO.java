package dataaccess;

import com.google.gson.Gson;
import exception.ResponseException;
import model.*;
import request.RegisterRequest;

import java.sql.*;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SQLUserDAO implements UserDAO
{
    public SQLUserDAO() throws ResponseException, DataAccessException
    {
        configureDatabase();
    }

    public void createUser(RegisterRequest userData) throws DataAccessException, ResponseException
    {
        var statement = "INSERT INTO userData (username, password, email) VALUES (?, ?, ?)";
        int id = executeUpdate(statement, userData.username(), userData.password(), userData.email());
    }

    public UserData getUser(String username) throws ResponseException
    {
        try (Connection conn = DatabaseManager.getConnection())
        {
            var statement = "SELECT username, json FROM userData WHERE username=?";
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
            throw new ResponseException(ResponseException.Code.ServerError, String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    private UserData readUser(ResultSet rs) throws SQLException
    {
        var username = rs.getString("json");
        UserData myUser = new Gson().fromJson(username, UserData.class);
        return myUser;
    }

    private int executeUpdate(String statement, Object... params) throws ResponseException, DataAccessException
    {
        try (Connection conn = DatabaseManager.getConnection())
        {
            try (PreparedStatement ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS))
            {
                for (int i = 0; i < params.length; i++)
                {
                    Object param = params[i];
                    if (param instanceof String p) ps.setString(i + 1, p);
                    else if (param instanceof Integer p) ps.setInt(i + 1, p);
                    else if (param == null) ps.setNull(i + 1, NULL);
                }
                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next())
                {
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (SQLException e)
        {
            throw new ResponseException(ResponseException.Code.ServerError, String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }

    private final String[] createStatements =
    {        """
            CREATE TABLE IF NOT EXISTS  user (
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL
              `json` TEXT DEFAULT NULL,
              PRIMARY KEY (`username`),
              INDEX(username),
              INDEX(password)
              INDEX(email)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    }; //don't exactly know what this means

    private void configureDatabase() throws ResponseException, DataAccessException
    {
        DatabaseManager.createDatabase();
        try(Connection conn = DatabaseManager.getConnection())
        {
            for(String statement : createStatements)
            {
                try(var preparedStatement = conn.prepareStatement(statement))
                {
                    preparedStatement.executeUpdate();
                }
            }
        }
        catch(SQLException ex)
        {
            throw new ResponseException(ResponseException.Code.ServerError, String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }
}
