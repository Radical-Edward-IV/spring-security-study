package edward.iv.restapi.exception;

import org.springframework.http.HttpStatus;

import java.io.Serial;

public class ApiException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -2756282230496251867L;

    private final HttpStatus status;

    private final String message;

    public ApiException(HttpStatus status, String message) {

        this.status = status;
        this.message = message;
    }

    public ApiException(HttpStatus status, String message, Throwable exception) {

        super(exception);
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return this.status;
    }

    public String getMessage() {
        return this.message;
    }
}
