package service;

import dataaccess.*;
import request.DeleteRequest;
import response.DeleteResponse;

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

    public DeleteResponse clear(DeleteRequest deleteRequest) throws SQLException, DataAccessException
    {
        if(deleteRequest != null)
        {
            myData.clearData();
            myAuth.clearData();
            myGame.clearData();
        }
        return new DeleteResponse();
    }
}
