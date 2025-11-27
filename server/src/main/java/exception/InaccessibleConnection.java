package exception;

public class InaccessibleConnection extends RuntimeException
{
    public InaccessibleConnection(String message)
    {
        super(message);
    }
}
