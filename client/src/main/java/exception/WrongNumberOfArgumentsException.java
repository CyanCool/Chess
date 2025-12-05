package exception;

public class WrongNumberOfArgumentsException extends RuntimeException
{
    public WrongNumberOfArgumentsException(String message)
    {
        super(message);
    }
}
