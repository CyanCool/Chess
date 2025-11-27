package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.BadRequestException;
import exception.ResponseException;
import model.GameData;

import java.sql.*;
import java.util.ArrayList;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SQLGameDAO extends SQL implements GameDAO
{
    private int nextID;
    public SQLGameDAO() throws ResponseException, DataAccessException
    {
        configureDatabase(createStatements);
        nextID = 0;
    }

    public int createGame(String gameName) throws ResponseException, DataAccessException
    {
        var statement = "INSERT INTO gameData (gameId, whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?, ?)";
        ChessGame myGame = new ChessGame();
        String json = new Gson().toJson(myGame);
        int id = executeUpdate(statement, nextID, null, null, gameName, json);
        nextID+= 1;
        return nextID--;
    }

    public GameData getGame(String gameName) throws ResponseException
    {
        try (Connection conn = DatabaseManager.getConnection())
        {
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM gameData WHERE gameName=?";
            try (PreparedStatement ps = conn.prepareStatement(statement))
            {
                ps.setString(1, gameName);
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        return readGame(rs);
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

    public GameData getGame(int gameID) throws ResponseException
    {
        try (Connection conn = DatabaseManager.getConnection())
        {
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM gameData WHERE gameID=?";
            try (PreparedStatement ps = conn.prepareStatement(statement))
            {
                ps.setInt(1, gameID);
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        return readGame(rs);
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

    private GameData readGame(ResultSet rs) throws SQLException
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
              `whiteUsername` varchar(256) DEFAULT NULL,
              `blackUsername` varchar(256) DEFAULT NULL,
              `gameName` varchar(256) NOT NULL,
              `game` TEXT DEFAULT NULL,
              PRIMARY KEY (`gameID`),
              INDEX(gameID),
              INDEX(whiteUsername),
              INDEX(blackUsername),
              INDEX(gameName)
            )
            """
            };

    public ArrayList<GameData> getList() throws ResponseException
    {
        ArrayList<GameData> myGameData = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection())
        {
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM gameData";
            try (PreparedStatement ps = conn.prepareStatement(statement))
            {
                try (ResultSet rs = ps.executeQuery())
                {
                    while (rs.next())
                    {
                        GameData game = readGame(rs);
                        myGameData.add(game);
                    }
                }
            }
            return myGameData;
        } catch (Exception e)
        {
            throw new ResponseException(ResponseException.Code.ServerError,
                    String.format("Unable to read data: %s", e.getMessage()));
        }
    }

    public void updateGame(int gID, String playerColor, String username) throws ResponseException, DataAccessException
    {
        if(getGame(gID) == null)
        {
            throw new ResponseException(ResponseException.Code.ServerError,"Unable to read data: %s");
        }
        else
        {
            GameData oldGame = getGame(gID);
            GameData newGame;
            if(playerColor.equals("WHITE"))
            {
                String statement = "UPDATE gameData SET whiteUsername = (?) WHERE gameID = (?)";
                int id = executeUpdate(statement,username,gID);
            }
            else if(playerColor.equals("BLACK"))
            {
                String statement = "UPDATE gameData SET blackUsername = (?) WHERE gameID = (?)";
                int id = executeUpdate(statement,username,gID);
            }
            else
            {
                throw new BadRequestException("This is not a valid player color");
            }
        }
    }

    @Override
    public void clearData() throws DataAccessException
    {
        super.clearData();
        nextID = 0;
    }
}
