package dataaccess;

import chess.ChessGame;
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
    }

//    public void createGame(String whiteUsername, String blackUsername, String gameName, String authToken)
//    {
//        int gameID = Integer.parseInt(UUID.randomUUID().toString());
//        ChessGame myGame = new ChessGame();
//        GameData myData = new GameData(gameID, whiteUsername, blackUsername, gameName, myGame);
//
//        gameInfo.put(nextID, myData); //i think i need to put it in the list?
//    }
//
//    public GameData getGame(String authToken)
//    {
//        return gameInfo.get(authToken);
//    }

    public String listGames(String authToken)
    {
        //verify the authToken
        String concat = "";
        for(GameData g : gameInfo)
        {
            concat += g.toString();
        }
        return concat;
    }
//
//    public void updateGame()
//    {
//
//
//    }
//
//    public void clear()
//    {
//
//    }
}
