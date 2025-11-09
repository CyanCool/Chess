package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import model.*;
import org.junit.jupiter.api.*;
import exception.*;

public class UserServiceTests
{
    private UserService userService;
    public UserServiceTests(MemoryUserDAO myData, MemoryAuthDAO myAuth)
    {
        userService = new UserService(myData, myAuth);
        RegisterRequest myRequest = new RegisterRequest("Steven", "password", "steven@gmail.com");
        userService.register(myRequest);
    }

    @Test
    @Order(1)
    @DisplayName("Username already taken")
    public void tryUserTaken()
    {
        RegisterRequest myRequest = new RegisterRequest("Steven", "password", "steven@gmail.com");
        Assertions.assertThrowsExactly(AlreadyTakenException.class, () -> {userService.register(myRequest); }); //check to see if it returns the exception
    }

    @Test
    @Order(2)
    @DisplayName("Bad Password Request")
    public void tryBadPassword()
    {
        RegisterRequest myRequest = new RegisterRequest("Rose", null, "rose@gmail.com");
        Assertions.assertThrowsExactly(BadRequestException.class, () -> {userService.register(myRequest); });
    }

    @Test
    @Order(3)
    @DisplayName("Bad Email Request")
    public void tryBadEmail()
    {
        RegisterRequest myRequest = new RegisterRequest("Rose", "password", null);
        Assertions.assertThrowsExactly(BadRequestException.class, () -> {userService.register(myRequest); });
    }

    @Test
    @Order(4)
    @DisplayName("Bad Username Request")
    public void tryBadUsername()
    {
        RegisterRequest myRequest = new RegisterRequest(null, "password", "rose@gmail.com");
        Assertions.assertThrowsExactly(BadRequestException.class, () -> {userService.register(myRequest); });
    }

    @Test
    @Order(5)
    @DisplayName("Successful Registration")
    public void registerSuccess()
    {
        RegisterRequest myRequest = new RegisterRequest("Bartholemue", "Imanerd", "bartholemue@gmail.com");
        RegisterResponse myResponse = userService.register(myRequest);

        //make sure the username of the request matches the response
        Assertions.assertEquals(myRequest.username(), myResponse.username());
        //make sure the authtoken in the response isn't null
        Assertions.assertNotNull(myResponse.authToken());
    }
}
