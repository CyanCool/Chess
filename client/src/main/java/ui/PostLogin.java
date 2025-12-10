package ui;

import com.google.gson.Gson;
import exception.ResponseException;
import model.GameData;
import request.RegisterRequest;
import response.ListgamesResponse;
import tools.State;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class PostLogin
{
    private String serverURL;
    private ServerFacade serverFacade;
    private ArrayList<GameData> mapOfGames;

    public PostLogin(String serverURL, ServerFacade serverFacade)
    {
        this.serverURL = serverURL;
        this.serverFacade = serverFacade;
        mapOfGames = new ArrayList<>();
    }

    public void run()
    {
        System.out.print(help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit"))
        {
            printPrompt();
            String line = scanner.nextLine();

            try
            {
                result = eval(line);
                System.out.print(result);
            } catch (Throwable e)
            {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    public String eval(String input)
    {
        try
        {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd)
            {
                case "create" -> createGame(params);
                case "list" -> listGames();
                case "join" -> joinGame(params);
//                case "observe" -> observeGame(params);
                case "logout" -> logout();
                case "quit" -> "quit";
                case "help" -> help();
                default -> help();
            };
        }
        catch (ResponseException ex)
        {
            return ex.getMessage();
        }
    }
    public String createGame(String[] params) throws ResponseException
    {
        try
        {
            serverFacade.createGame(params);
            return String.format("You created the game %s.", params[0]);
        }
        catch(Exception e)
        {
            throw new ResponseException(ResponseException.Code.ClientError, e.getMessage());
        }

    }

    public String listGames() throws ResponseException
    {
        try
        {
            HttpResponse<String> myResponse = serverFacade.listGames();
            ListgamesResponse listGames = new Gson().fromJson(myResponse.body(), ListgamesResponse.class);
            String response = "";
            for(int i = 0; i<listGames.games().size(); i++)
            {
                mapOfGames.add(listGames.games().get(i));
                response += String.format("Game Name: %s\nGame #: %d\nGame Players: %s, %s\n", mapOfGames.get(i).gameName(), i+1, mapOfGames.get(i).whiteUsername(), mapOfGames.get(i).blackUsername());
            }
            return String.format("Current Games:\n%s", response);
        }
        catch(Exception e)
        {
            throw new ResponseException(ResponseException.Code.ClientError, e.getMessage());
        }
    }

    public String joinGame(String[] params) throws ResponseException
    {
        try
        {
            int index = Integer.parseInt(params[0])-1;
            if(index > mapOfGames.size() -1)
            {
                throw new ResponseException(ResponseException.Code.ClientError, "This id number is invalid");
            }
            GameData myGame = mapOfGames.get(Integer.parseInt(params[0])-1);
            serverFacade.joinGame(myGame.gameID(), params);
            return String.format("You joined the game #: %d",myGame.gameID());
        }
        catch(Exception e)
        {
            throw new ResponseException(ResponseException.Code.ClientError, e.getMessage());
        }
    }

//    public String observeGame(String[] params)
//    {
//
//    }

    public String logout() throws ResponseException
    {
        try
        {
            serverFacade.logout();
            return "quit"; //will this quit out of only the postlogin ui or everything
        }
        catch(Exception e)
        {
            throw new ResponseException(ResponseException.Code.ServerError, e.getMessage());
        }
    }

    public String help()
    {
        return """
                - create -> <NAME>
                - list -> games
                - join -> <ID> [WHITE|BLACK] -> a game
                - observe -> <ID> -> a game
                - logout -> when you are done
                - quit -> playing chess
                - help -> see possible commands
                """;
    }

    private void printPrompt()
    {
        System.out.print("\nWhich action would you like to take?:\n");
    }
}
