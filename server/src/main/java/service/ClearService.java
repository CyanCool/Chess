package service;

import dataaccess.*;
import exception.InaccessibleConnection;
import exception.ResponseException;
import request.DeleteRequest;
import response.DeleteResponse;
import response.ErrorResponse;

import java.sql.SQLException;

public class ClearService
{
    private UserDAO myData;
    private AuthDAO myAuth;
    private GameDAO myGame;

    public ClearService(UserDAO myData, AuthDAO myAuth, GameDAO myGame)
    {
        this.myData = myData;
        this.myAuth = myAuth;
        this.myGame = myGame;
    }

    public DeleteResponse clear(DeleteRequest deleteRequest) throws SQLException, DataAccessException, ResponseException
    {
        if(deleteRequest != null)
        {
            try
            {
                myData.clearData();
                myAuth.clearData();
                myGame.clearData();
            }
            catch (Exception e)
            {
                throw new ResponseException(ResponseException.Code.ServerError, String.format("Unable to read data: %s", e.getMessage()));
            }
        }
        return new DeleteResponse();
    }
}
