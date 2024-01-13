package edward.iv.restapi.exception.handler;

import edward.iv.restapi.exception.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /*
     * TODO: MethodArgumentNotValidException -> BadRequestException으로 전환하려 했으나 실패함.
     *
     * ⦿ 응답
     * {
     *     "timestamp": "2024-01-01T10:57:10.398+00:00",
     *     "status": 400,
     *     "error": "Bad Request",
     *     "message": "Validation failed for object='signUpRequest'. Error count: 2",
     *     ...
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public void handleValidException(MethodArgumentNotValidException ex) throws BadRequestException {

//        List<String> errors = ex.getBindingResult().getFieldErrors()
//                .stream().map(fieldError -> fieldError.getField() + " " + fieldError.getDefaultMessage()).toList();

        List<FieldError> errors = ex.getBindingResult().getFieldErrors();

        List<String> fields = new ArrayList<>();

        String defaultMsg = "";

        for (FieldError fieldError : errors) {
            fields.add(fieldError.getField());
            defaultMsg = fieldError.getDefaultMessage();
        }

        throw new BadRequestException(fields + " " + defaultMsg);
    }
}