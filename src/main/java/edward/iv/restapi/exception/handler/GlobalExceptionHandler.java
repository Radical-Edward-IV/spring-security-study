package edward.iv.restapi.exception.handler;

import edward.iv.restapi.debug.model.dto.DebugInfo;
import edward.iv.restapi.exception.ApiException;
import edward.iv.restapi.exception.BadRequestException;
import edward.iv.restapi.exception.payload.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    DebugInfo debug;

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
    public void handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) throws BadRequestException {

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

    @ExceptionHandler(ApiException.class)
    public ResponseEntity handleApiException(ApiException ex) {

        int status = ex.getErrorResponse().getStatus();

        return ResponseEntity.status(status).body(ex.getErrorResponse());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity handleDataIntegrityViolationException(DataIntegrityViolationException ex) {

        return ResponseEntity.badRequest().body(ErrorResponse.invalidRequest().debugId(debug.getDebugId()));
    }
}