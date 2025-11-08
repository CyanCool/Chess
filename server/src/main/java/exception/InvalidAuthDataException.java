package exception;

public class InvalidAuthDataException extends RuntimeException
{
    public InvalidAuthDataException(String message)
    {
        super(message);
    }
}
