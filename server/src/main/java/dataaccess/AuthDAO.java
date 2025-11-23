package dataaccess;

import exception.ResponseException;
import model.AuthData;

import java.sql.SQLException;

public interface AuthDAO
{
    public AuthData getAuth(String myToken) throws ResponseException;
    public String createAuth(String username) throws DataAccessException, ResponseException;
    public void remove(AuthData myData) throws ResponseException, DataAccessException;

    void clearData() throws DataAccessException, SQLException;
}
