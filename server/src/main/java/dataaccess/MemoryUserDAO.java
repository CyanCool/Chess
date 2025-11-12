package dataaccess;

import model.RegisterRequest;
import model.UserData;
import java.util.HashMap;

public class MemoryUserDAO implements UserDAO
{
    //has the username as the key and the UserData as the value
    private HashMap<String, UserData> userInfo;

    public MemoryUserDAO()
    {
        userInfo = new HashMap<>();
    }

    public UserData getUser(String username)
    {
        return userInfo.get(username);
    }

    public HashMap<String, UserData> getMap()
    {
        if(userInfo.size() > 0)
        {
            return userInfo;
        }
        return null;
    }

    public void createUser(RegisterRequest userData)
    {
        UserData currentUser = new UserData(userData.username(), userData.password(), userData.email());
        userInfo.put(currentUser.username(), currentUser);
    }

    public void removeUser(String username)
    {
        userInfo.remove(username);
    }

    public void clearData()
    {
        userInfo.clear();
    }
}
