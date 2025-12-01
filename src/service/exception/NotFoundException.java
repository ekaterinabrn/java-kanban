package service.exception;

// исключение для обработки случаев, когда ресурс не найден
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}

