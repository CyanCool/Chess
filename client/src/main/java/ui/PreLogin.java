package ui;

import exception.ResponseException;

import java.util.Arrays;
import java.util.Scanner;


public class PreLogin
{
    private String serverURL;
    private ServerFacade serverFacade;

    public PreLogin(String serverURL)
    {
        this.serverURL = serverURL;
        serverFacade = new ServerFacade(serverURL);
    }

    public void run()
    {
        System.out.println(" Welcome to 240 chess. Type help to get started.");
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

    public String help()
    {
        return """
                - register -> <USERNAME> <PASSWORD> <EMAIL>
                - login -> <USERNAME> <PASSWORD>
                - quit -> playing chess
                - help -> see possible commands
                """;
    }

    public String eval(String input)
    {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help"; //maybe respond in a different way instead of setting the command to help by default
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd)
            {
                case "register" -> register(params);
                case "login" -> login(params);
                case "quit" -> quit();
                case "help" -> help();
                default -> help();
            };
        } catch (ResponseException ex) { //catch whatever exceptions my server facade throws
            return ex.getMessage();
        }
    }

    public void register(String[] params)
    {
        try
        {
            System.out.println("Enter your username, password, and email. Each entry should be separated by a space");

            serverFacade.register(params);
            PostLogin ui = new PostLogin();
            ui.run();
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    private void printPrompt()
    {
        System.out.print("\nWhich action would you like to take?:\n");
    }

    public void quit()
    {
        //exits the program, ends the infinite loops
    }

    public void login()
    {
        //Prompts the user for login information
        //Makes a login request based on the information, checking to see if the input is wrong in various
        //cases and making sure to return an error that the user can understand
        //If successful, make a new postlogin object and call its run function
    }

    public void register()
    {
        //call server facade register
        //catch any exceptions thrown by it

    }


}
