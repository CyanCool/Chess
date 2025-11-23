package dataaccess;

import exception.ResponseException;
import model.AuthData;
import request.RegisterRequest;

import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SQLAuthDAO implements AuthDAO
{
    public SQLAuthDAO() throws ResponseException, DataAccessException
    {
        configureDatabase();
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
            throw new ResponseException(ResponseException.Code.ServerError, String.format("Unable to read data: %s", e.getMessage()));
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
            throw new ResponseException(ResponseException.Code.ServerError, String.format("Unable to read data: %s", e.getMessage()));
        }

    }

    private AuthData readUser(ResultSet rs) throws SQLException
    {
        var authToken = rs.getString("authToken");
        var username = rs.getString("username");
        return new AuthData(authToken, username);
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
            CREATE TABLE IF NOT EXISTS  authData (
              `authToken` varchar(256) NOT NULL,
              `username` varchar(256) NOT NULL,
              PRIMARY KEY (`authToken`),
              INDEX(authToken),
              INDEX(username)
            )
            """
            };

    private void configureDatabase() throws ResponseException, DataAccessException
    {
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

    public void remove(AuthData myData) throws ResponseException, DataAccessException
    {
        var statement = "DELETE authToken, username FROM authData WHERE authToken = ?";
        int id = executeUpdate(statement, myData.username());
    }

    public void clearTableData() throws DataAccessException, SQLException
    {
        try(Connection conn = DatabaseManager.getConnection())
        {
            String sql = "SHOW TABLES";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            Statement truncateStmt = conn.createStatement();
            truncateStmt.execute("SET FOREIGN_KEY_CHECKS=0");

            while (rs.next())
            {
                String table = rs.getString(1);
                truncateStmt.executeUpdate("TRUNCATE TABLE " + table);
            }

            truncateStmt.execute("SET FOREIGN_KEY_CHECKS=1");

        }

    }
}
