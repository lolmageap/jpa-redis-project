package cherhy.soloProject.exception;

import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public abstract class SnsException extends RuntimeException {

    private final Map<String, String> validation = new ConcurrentHashMap<>();

    public SnsException(String message) {
        super(message);
    }

    public SnsException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int getStatusCode();

    public void addValidation(String fieldName, String message) {
        validation.put(fieldName, message);
    }

}
