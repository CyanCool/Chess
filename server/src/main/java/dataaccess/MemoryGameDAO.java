package dataaccess;

import chess.ChessGame;
import exception.BadRequestException;
import model.GameData;

import java.util.ArrayList;
import java.util.UUID;

public class MemoryGameDAO implements GameDAO
{
    //authToken stored as string, leads to the game data
    private int nextID;
    private ArrayList<GameData> gameInfo;

    public MemoryGameDAO()
    {
        gameInfo = new ArrayList<>();
        nextID = 1;
    }

    public void createGame(String gameName)
    {
        ChessGame myGame = new ChessGame();
        GameData myData = new GameData(nextID++, null, null, gameName, myGame);
        gameInfo.add(myData);
    }

    public GameData getGame(String gameName)
    {
        for(GameData g: gameInfo)
        {
            if(g.gameName().equals(gameName))
            {
                return g;
            }
        }
        return null;
    }

    public ArrayList<GameData> getList()
    {
        return gameInfo;
    }

    public GameData getGame(int gameID)
    {
        for(GameData g: gameInfo)
        {
            if(g.gameID() == gameID)
            {
                return g;
            }
        }
        return null;
    }

    public void updateGame(int gameID, String playerColor, String username)
    {
        GameData oldGame = getGame(gameID);
        GameData newGame;
        if(playerColor.equals("WHITE"))
        {
            newGame = new GameData(gameID, username, oldGame.blackUsername(), oldGame.gameName(), oldGame.game());
            gameInfo.remove(oldGame);
            gameInfo.add(newGame);
        }
        else if(playerColor.equals("BLACK"))
        {
            newGame = new GameData(gameID, oldGame.whiteUsername(), username, oldGame.gameName(), oldGame.game());
            gameInfo.remove(oldGame);
            gameInfo.add(newGame);
        }
        else
        {
            throw new BadRequestException("This is not a valid player color");
        }
    }

    public void clearData()
    {
        gameInfo.clear();
    }

}
