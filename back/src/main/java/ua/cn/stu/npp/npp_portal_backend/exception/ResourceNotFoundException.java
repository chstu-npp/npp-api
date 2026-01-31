package ua.cn.stu.npp.npp_portal_backend.exception;

/**
 * Виняток коли ресурс не знайдено
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
