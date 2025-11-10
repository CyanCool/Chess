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
        nextID = 1;
    }

    public void createGame(String gameName, String authToken)
    {
        //verify authToken
        ChessGame myGame = new ChessGame();
        GameData myData = new GameData(nextID++, null, null, gameName, myGame);
        gameInfo.add(myData);
    }
//
//    public GameData getGame(String authToken)
//    {
//        return gameInfo.get(authToken);
//    }

//    public String listGames(String authToken)
//    {
//        //verify the authToken
//        String concat = "";
//        for(GameData g : gameInfo)
//        {
//            concat += g.toString();
//        }
//        return concat;
//    }
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
