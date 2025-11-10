package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import io.javalin.http.Context;

public class JoinGameService extends ParentService
{
    private MemoryAuthDAO myAuth;
    private MemoryGameDAO myGame;

    public JoinGameService(MemoryAuthDAO myAuth, MemoryGameDAO myGame)
    {
        super(myAuth);
        this.myGame = myGame;

    }
}
