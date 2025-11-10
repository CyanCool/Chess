package handler;

import com.google.gson.Gson;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import model.DeleteRequest;
import model.DeleteResponse;
import service.ClearService;
import io.javalin.http.Context;

public class ClearHandler
{
    private final ClearService clearService;

    public ClearHandler(MemoryUserDAO myData, MemoryAuthDAO myAuth, MemoryGameDAO myGame)
    {
        clearService = new ClearService(myData, myAuth, myGame);
    }

    public void clear(Context ctx)
    {
        DeleteRequest deleteRequest = new DeleteRequest();
        DeleteResponse deleteResponse = clearService.clear(deleteRequest);
        ctx.result(new Gson().toJson(deleteResponse));
        ctx.status(200);
    }
}
