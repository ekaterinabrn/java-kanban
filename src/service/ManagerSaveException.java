package service;

public class ManagerSaveException extends RuntimeException {
    //cause — вложенное исключение из за которого началась ошибка
    public ManagerSaveException(String message, Throwable cause) {
        super(message, cause);
    }
}
