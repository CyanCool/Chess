package exception;

public class StringTooLargeException extends RuntimeException
{
    public StringTooLargeException(String message)
    {
        super(message);
    }
}
