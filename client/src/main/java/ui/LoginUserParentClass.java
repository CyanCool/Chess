package ui;

import java.util.Scanner;

public class LoginUserParentClass
{
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
        return "";
    }

    public String eval(String input)
    {
        return "";
    }

    public void printPrompt()
    {
        System.out.print("\nWhich action would you like to take?:\n");
    }
}
