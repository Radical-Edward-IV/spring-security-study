package edward.iv.restapi.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -4183839697576885103L;

    public BadRequestException(String message) { super(message); }

    public BadRequestException(String message, Throwable cause) { super(message, cause); }
}
