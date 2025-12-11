package ui;

import exception.ResponseException;

import java.util.Arrays;
import java.util.Scanner;
import ui.ServerFacade;


public class PreLogin extends LoginUserParentClass
{
    private String serverURL;
    private static ServerFacade serverFacade;


    public PreLogin(String serverURL)
    {
        //Connect to the server
        this.serverURL = serverURL;
        serverFacade = new ServerFacade(serverURL);
    }

    @Override
    public void run()
    {
        super.run();
    }

    @Override
    public String help()
    {
        return """
                - register -> <USERNAME> <PASSWORD> <EMAIL>
                - login -> <USERNAME> <PASSWORD>
                - quit -> playing chess
                - help -> see possible commands
                """;
    }

    @Override
    public String eval(String input)
    {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            //maybe respond in a different way instead of setting the command to help by default
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd)
            {
                case "register" -> register(params);
                case "login" -> login(params);
                case "quit" -> "quit";
                case "help" -> help();
                default -> help();
            };
        } catch (ResponseException ex) { //catch whatever exceptions my server facade throws
            return ex.getMessage();
        }
    }

    public String register(String[] params) throws ResponseException
    {
        try
        {
            serverFacade.register(params);
            serverFacade.login(new String[]{params[0],params[1]});
            PostLogin ui = new PostLogin(serverURL,serverFacade);
            ui.run();
            return String.format("You registered as %s.", params[0]);
        }
        catch(Exception e)
        {
            throw new ResponseException(ResponseException.Code.ClientError, e.getMessage());
        }
    }

    public String login(String[] params) throws ResponseException
    {
        try
        {
            serverFacade.login(params);
            PostLogin ui = new PostLogin(serverURL,serverFacade);
            ui.run();
            return String.format("You logged in as %s.", params[0]);
        }
        catch(Exception e)
        {
            throw new ResponseException(ResponseException.Code.ClientError, e.getMessage());
        }
    }

    @Override
    public void printPrompt()
    {
        super.printPrompt();
    }
}
