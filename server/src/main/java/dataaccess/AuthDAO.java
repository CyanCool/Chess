package dataaccess;

import exception.ResponseException;
import model.AuthData;
import model.Data;

import java.sql.SQLException;
import java.util.ArrayList;

public interface AuthDAO
{
    public String createAuth(String username) throws DataAccessException, ResponseException;
    public void remove(AuthData myData) throws ResponseException, DataAccessException;

    void clearData() throws DataAccessException, SQLException;

    AuthData getClassInfo(String s) throws ResponseException;

    ArrayList<AuthData> getAllAuthData() throws ResponseException;
}
