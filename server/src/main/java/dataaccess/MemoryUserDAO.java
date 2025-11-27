package dataaccess;

import exception.ResponseException;
import org.mindrot.jbcrypt.BCrypt;
import request.RegisterRequest;
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

    public UserData getClassInfo(String username)
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
        String hashedPassword = storeUserPassword(userData.password());
        UserData currentUser = new UserData(userData.username(), hashedPassword, userData.email());
        userInfo.put(currentUser.username(), currentUser);
    }

    public void clearData()
    {
        userInfo.clear();
    }

    public String storeUserPassword(String clearTextPassword)
    {
        return BCrypt.hashpw(clearTextPassword, BCrypt.gensalt());
    }

    public boolean verifyUser(String username, String password) throws ResponseException
    {
        String hashedPassword = userInfo.get(username).password();
        return BCrypt.checkpw(password, hashedPassword);
    }
}
