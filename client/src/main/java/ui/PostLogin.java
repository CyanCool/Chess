package ui;

import exception.ResponseException;
import tools.State;

import java.util.Arrays;
import java.util.Scanner;

public class PostLogin
{
    private String serverURL;
    private ServerFacade serverFacade;

    public PostLogin(String serverURL, ServerFacade serverFacade)
    {
        this.serverURL = serverURL;
        this.serverFacade = serverFacade;
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
//                case "list" -> listGames(params);
//                case "join" -> joinGame(params);
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

    public String listGames(String[] params) throws ResponseException
    {
        //make sure the user is logged in
        //make sure params has the right number of arguments
        //make sure none of the arguments are null or whitespace
        //
        try
        {
            serverFacade.listGames(params);
            return String.format("Current Games: ", params[0]);
        }
        catch(Exception e)
        {
            throw new ResponseException(ResponseException.Code.ClientError, e.getMessage());
        }
    }
//
//    public String joinGame(String[] params)
//    {
//
//    }
//
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
