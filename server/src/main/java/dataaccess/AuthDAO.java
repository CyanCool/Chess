package dataaccess;

import exception.ResponseException;
import model.AuthData;

public interface AuthDAO
{
    public AuthData getAuth(String myToken) throws ResponseException;
    public String createAuth(String username) throws DataAccessException, ResponseException;
    public void remove(AuthData myData) throws ResponseException, DataAccessException;
}
