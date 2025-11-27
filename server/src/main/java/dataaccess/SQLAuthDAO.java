package dataaccess;

import exception.ResponseException;
import model.AuthData;
import request.RegisterRequest;

import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SQLAuthDAO extends SQL implements AuthDAO
{
    public SQLAuthDAO() throws ResponseException, DataAccessException
    {
        configureDatabase(createStatements);
    }

    public String createAuth(String username) throws DataAccessException, ResponseException
    {
        String myToken = UUID.randomUUID().toString();
        var statement = "INSERT INTO authData (authToken, username) VALUES (?, ?)";
        int id = executeUpdate(statement, myToken, username);

        return myToken;
    }

    public AuthData getAuth(String myToken) throws ResponseException
    {
        try (Connection conn = DatabaseManager.getConnection())
        {
            var statement = "SELECT authToken,username FROM authData WHERE authToken=?";
            try (PreparedStatement ps = conn.prepareStatement(statement))
            {
                ps.setString(1, myToken);
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

    public ArrayList<AuthData> getAllAuthData() throws ResponseException
    {
        ArrayList<AuthData> myAuthData = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection())
        {
            var statement = "SELECT authToken, username FROM authData";
            try (PreparedStatement ps = conn.prepareStatement(statement))
            {
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        AuthData authAsString = readUser(rs);
                        myAuthData.add(authAsString);
                    }
                }
                return myAuthData;
            }
        } catch (Exception e)
        {
            throw new ResponseException(ResponseException.Code.ServerError,
                    String.format("Unable to read data: %s", e.getMessage()));
        }

    }

    private AuthData readUser(ResultSet rs) throws SQLException
    {
        var authToken = rs.getString("authToken");
        var username = rs.getString("username");
        return new AuthData(authToken, username);
    }

    private final String[] createStatements =
            {        """
            CREATE TABLE IF NOT EXISTS  authData (
              `authToken` varchar(256) NOT NULL,
              `username` varchar(256) NOT NULL,
              PRIMARY KEY (`authToken`),
              INDEX(authToken),
              INDEX(username)
            )
            """
            };

    public void remove(AuthData myData) throws ResponseException, DataAccessException
    {
        var statement = "DELETE FROM authData WHERE authToken = ?";
        int id = executeUpdate(statement, myData.authToken());
    }
}
