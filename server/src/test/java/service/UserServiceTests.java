package service;

import chess.ChessGame;
import org.junit.jupiter.api.*;
import passoff.model.TestAuthResult;
import service.UserService;
import model.RegisterRequest;

import java.net.HttpURLConnection;
import java.util.*;

public class UserServiceTests
{

    //test register
        //try making a login with a username that is already taken
        //try logging in with a unique username
    private UserService userService;
    public UserServiceTests()
    {
        userService = new UserService(); //maybe populate the database beforehand
    }

    @Test
    @Order(1)
    @DisplayName("Username already taken")
    public void tryUserTaken()
    {
        RegisterRequest myRequest = new RegisterRequest("Steven", "password", "steven@gmail.com");
        userService.register(myRequest);
        userService.register(myRequest);

        //check to see if it returns the exception
        //check the database to make sure it doesn't add the user twice, maybe
    }

    public void tryBadRequest()
    {
        RegisterRequest myRequest = new RegisterRequest("Rose", null, "rose@gmail.com");
        userService.register(myRequest);
    }
}
