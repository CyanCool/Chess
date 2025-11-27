package dataaccess;

import exception.ResponseException;
import model.GameData;

import java.util.ArrayList;

public interface GameDAO
{
    public int createGame(String gameName) throws ResponseException, DataAccessException;
    public void clearData() throws DataAccessException;
    public void updateGame(int i, String s, String username) throws ResponseException, DataAccessException;
    public ArrayList<GameData> getList() throws ResponseException;

    GameData getClassInfo(String s) throws ResponseException;
    GameData getClassInfo(int id) throws ResponseException;
}
