package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.UserData;
import request.RegisterRequest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SQLGameDAO
{
    private int nextID;
    public SQLGameDAO() throws ResponseException, DataAccessException
    {
        configureDatabase();
        nextID = 1;
    }

    public void createGame(String gameName) throws ResponseException, DataAccessException
    {
        var statement = "INSERT INTO gameData (gameId, whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?, ?)";
        ChessGame myGame = new ChessGame();
        String json = new Gson().toJson(myGame);
        int id = executeUpdate(statement, nextID++, null, null, gameName, json);
    }

    public GameData getGame(String gameName) throws ResponseException
    {
        try (Connection conn = DatabaseManager.getConnection())
        {
            var statement = "SELECT game FROM gameData WHERE gameName=?";
            try (PreparedStatement ps = conn.prepareStatement(statement))
            {
                ps.setString(1, gameName);
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

    public GameData getGame(int gameID) throws ResponseException
    {
        try (Connection conn = DatabaseManager.getConnection())
        {
            var statement = "SELECT game FROM gameData WHERE gameID=?";
            try (PreparedStatement ps = conn.prepareStatement(statement))
            {
                ps.setInt(1, gameID);
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

    private GameData readUser(ResultSet rs) throws SQLException
    {
        var gameID = rs.getInt("gameID");
        var whiteUsername = rs.getString("whiteUsername");
        var blackUsername = rs.getString("blackUsername");
        var gameName = rs.getString("gameName");
        var game = rs.getString("game");
        ChessGame myGame = new Gson().fromJson(game, ChessGame.class);
        GameData myData = new GameData(gameID, whiteUsername, blackUsername, gameName, myGame);
        return myData;
    }

    private final String[] createStatements =
            {        """
            CREATE TABLE IF NOT EXISTS  gameData (
              `gameID` int NOT NULL AUTO_INCREMENT,
              `whiteUsername` varchar(256) NOT NULL,
              `blackUsername` varchar(256) NOT NULL,
              `gameName` varchar(256) NOT NULL,
              `game` TEXT DEFAULT NULL,
              PRIMARY KEY (`gameID`),
              INDEX(gameID),
              INDEX(whiteUsername),
              INDEX(blackUsername),
              INDEX(gameName),
              INDEX(game)
            )
            """
            };

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
                    else if (param instanceof ChessGame g) ps.setString(i + 1, g.toString());
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

    public ArrayList<GameData> getList()
    {
        ArrayList<AuthData> myAuthData = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection())
        {
            var statement = "SELECT authToken FROM authData";
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
}
