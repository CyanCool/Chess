package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.UserMemoryDAO;
import exception.BadRequestException;
import model.RegisterResponse;
import model.RegisterRequest;
import exception.AlreadyTakenException;

public class UserService
{
    private UserMemoryDAO myData; //where do i put this idk
    private MemoryAuthDAO myAuth;

    public UserService()
    {
        myData = new UserMemoryDAO();
        myAuth = new MemoryAuthDAO();
    }

    public RegisterResponse register(RegisterRequest registerRequest)
    {
        String token;
        if(myData.getUser(registerRequest.username()) == null)
        {
            myData.createUser(registerRequest);
            token = myAuth.createAuth();

            RegisterResponse registerResponse = new RegisterResponse(registerRequest.username(), token);
            return registerResponse;
        }
        else if((registerRequest.username() != null && (registerRequest.password() == null || registerRequest.email() == null)) || (registerRequest.password() != null && (registerRequest.username() == null || registerRequest.email() == null)) || (registerRequest.email() != null && (registerRequest.username() == null || registerRequest.password() == null)))
        {
            throw new BadRequestException("One of the required fields (username, password, or email) are missing");
        }
        else
        {
            throw new AlreadyTakenException("This Username is already taken");
        }
    }
    //unpack the register request into its components instead of passing it in
    //will have the JSON info for the username and Password passed into Register as parameters
    //will call DataAccess Method of getUser with the username
    //if getUser returns null, then service will call
    //Create User and Create Auth from Data Access
    //return the new User Data to the Handler so it can
    //translate it back into the weird script which will
    //then be sent back to the computer
    //if getUser returns something, then the service will
    //throw an already taken exception
}
