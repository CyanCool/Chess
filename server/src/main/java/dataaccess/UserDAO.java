package dataaccess;

import exception.ResponseException;
import model.Data;
import request.RegisterRequest;
import model.UserData;

import java.sql.SQLException;

public interface UserDAO
{
    public void createUser(RegisterRequest userData) throws DataAccessException, ResponseException;

    void clearData() throws DataAccessException, SQLException;

    boolean verifyUser(String username, String password) throws ResponseException;

    UserData getClassInfo(String username) throws ResponseException;

    String readHashedPasswordFromDatabase(String lisa) throws ResponseException;
}