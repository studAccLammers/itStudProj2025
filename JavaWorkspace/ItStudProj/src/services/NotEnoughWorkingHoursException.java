package services;

public class NotEnoughWorkingHoursException extends IllegalStateException {
    public NotEnoughWorkingHoursException() {
        super("Not enough contracts available to ensure minimum employee working hours");
    }
}
