package dataaccess;

import model.AuthData;

import java.util.ArrayList;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO
{
    //For Authentication Data:
    //access the authentication data in the database using the authToken
    //create auth data by generating a unique Auth Token
    //Remove auth data
    //stores authToken, will be stored in an Array List to easily see if it contains the token
    private ArrayList<AuthData> authDataList;

    public MemoryAuthDAO()
    {
        authDataList = new ArrayList<>();
    }

    public AuthData getAuth(String myToken)
    {
        for(AuthData a : authDataList)
        {
            if(a.authToken().equals(myToken))
            {
                return a;
            }
        }
        return null;
    }

    public String createAuth()
    {
        String myToken = UUID.randomUUID().toString();
        AuthData myData = new AuthData(myToken);
        authDataList.add(myData);

        return myToken;
    }

    public void remove(AuthData myData)
    {
        authDataList.remove(myData);
    }
}
