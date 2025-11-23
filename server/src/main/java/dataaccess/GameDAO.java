package dataaccess;

import model.GameData;

import java.util.ArrayList;

public interface GameDAO
{
    public void createGame(String gameName);
    public GameData getGame(String gameName);
    public GameData getGame(int gameID);
    public void clearData();
    public void updateGame(int i, String s, String username);
    public ArrayList<GameData> getList();
}
