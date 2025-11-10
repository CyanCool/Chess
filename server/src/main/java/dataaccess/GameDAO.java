package dataaccess;

import model.GameData;

public interface GameDAO
{
    public void createGame(String gameName);
    public GameData getGame(String gameName);
}
