package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import request.DeleteRequest;
import response.DeleteResponse;

public class ClearService
{
    private MemoryUserDAO myData;
    private MemoryAuthDAO myAuth;
    private MemoryGameDAO myGame;

    public ClearService(MemoryUserDAO myData, MemoryAuthDAO myAuth, MemoryGameDAO myGame)
    {
        this.myData = myData;
        this.myAuth = myAuth;
        this.myGame = myGame;
    }

    public DeleteResponse clear(DeleteRequest deleteRequest)
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
