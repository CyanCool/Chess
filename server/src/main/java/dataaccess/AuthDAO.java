package dataaccess;

import model.AuthData;

public interface AuthDAO
{
    public AuthData getAuth(String myToken);
    public String createAuth(String username);
    public void remove(AuthData myData);
}
