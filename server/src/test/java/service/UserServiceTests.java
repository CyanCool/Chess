package service;

import chess.ChessGame;
import model.*;
import org.junit.jupiter.api.*;
import passoff.model.TestAuthResult;
import service.UserService;

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

    @Test
    @Order(2)
    @DisplayName("Bad Password Request")
    public void tryBadPassword()
    {
        RegisterRequest myRequest = new RegisterRequest("Rose", null, "rose@gmail.com");
        userService.register(myRequest);
    }

    @Test
    @Order(3)
    @DisplayName("Bad Email Request")
    public void tryBadEmail()
    {
        RegisterRequest myRequest = new RegisterRequest("Rose", "password", null);
        userService.register(myRequest);
    }

    @Test
    @Order(4)
    @DisplayName("Bad Username Request")
    public void tryBadUsername()
    {
        RegisterRequest myRequest = new RegisterRequest(null, "password", "rose@gmail.com");
        userService.register(myRequest);
    }

    @Test
    @Order(5)
    @DisplayName("Successful Registration")
    public void registerSuccess()
    {
        RegisterRequest myRequest = new RegisterRequest("Bartholemue", "Imanerd", "bartholemue@gmail.com");
        RegisterResponse myResponse = userService.register(myRequest);

        //make sure the username of the request matches the response
        if(!myRequest.username().equals(myResponse.username()))
        {
            System.out.println("Registration Request did not have the same username as registration response");
        }
        //make sure the authtoken in the response isn't null
        else if(myResponse.authtoken() == null)
        {
            System.out.println("The authToken is null");
        }
        else
        {
            System.out.println("Registration was successful");
        }
    }
}
