package application.cli.exceptions;

public class MccExistException extends Exception{
    public MccExistException() {
        super();
    }

    public MccExistException(String message) {
        super(message);
    }
}
