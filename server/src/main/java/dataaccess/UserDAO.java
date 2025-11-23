package dataaccess;

import exception.ResponseException;
import request.RegisterRequest;
import model.UserData;

import java.sql.SQLException;

public interface UserDAO
{
    public UserData getUser(String username) throws ResponseException;
    public void createUser(RegisterRequest userData) throws DataAccessException, ResponseException;

    void clearData() throws DataAccessException, SQLException;
}