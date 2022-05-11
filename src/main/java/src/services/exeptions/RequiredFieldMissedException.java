package src.services.exeptions;

public class RequiredFieldMissedException extends Exception{
    public RequiredFieldMissedException(String message) {
        super(message);
    }
}
