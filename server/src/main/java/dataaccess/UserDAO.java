package dataaccess;

import request.RegisterRequest;
import model.UserData;

public interface UserDAO
{
    public UserData getUser(String username);
    public void createUser(RegisterRequest userData);
}



    // will have the basic methods for a data access object
    //have a variable that stores the database items, in other words
    //create the database
    //Do I make a database class?

    //For User Data:
    //access the user data in the database, given the username
    //create user data given the username and password
    //Remove user data
    //user data stores username and password, will be a Hash map


    //For Game Data:
    //list all the game data
    //create a new game to add to game data using the game name
    //get a game by providing the game ID
    //update a game by providing a player color and game ID
    //Remove Game data
    //needs to retrieve a game based on the player color and game ID
    //ideas:
    //make a Game Data class that stores the game name, player color, and game ID
    //make a hash map with the game Id as the key and the game name as the value???


    //Clear method that implements remove user data, remove auth data,
    //and remove game data
