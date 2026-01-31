package ua.cn.stu.npp.npp_portal_backend.exception;

/**
 * Виняток коли ресурс вже існує (дублікат)
 */
public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String message) {
        super(message);
    }

    public DuplicateResourceException(String message, Throwable cause) {
        super(message, cause);
    }
}