package service;

import exception.BadRequestException;
import dataaccess.*;
import exception.DoesNotExistException;
import model.AuthData;

public class ParentService
{
    private MemoryAuthDAO myAuth;

    public ParentService(MemoryAuthDAO myAuth)
    {
        this.myAuth = myAuth;
    }

    public AuthData verifyAuth(String authToken)throws BadRequestException
    {
        //using the authToken, make sure it is not null then look through the database to see if it exists
        if(authToken == null)
        {
            throw new BadRequestException("Bad Request");
        }
        else if(myAuth.getAuth(authToken) == null)
        {
            throw new DoesNotExistException("This session doesn't exist");
        }
        else
        {
            return myAuth.getAuth(authToken);
        }
    }
}
