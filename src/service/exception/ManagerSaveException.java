package service.exception;

public class ManagerSaveException extends RuntimeException {
   //вынесено отдельно
    //cause — вложенное исключение из за которого началась ошибка
    public ManagerSaveException(String message, Throwable cause) {
        super(message, cause);
    }
}

