package edward.iv.restapi.exception;

import edward.iv.restapi.exception.payload.response.ErrorResponse;

import java.io.Serial;

public class ApiException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -1955880542027836285L;

    private ErrorResponse errorResponse;

    public ApiException(Exception e) { super(e); }

    public ApiException(ErrorResponse errorResponse) { this.errorResponse = errorResponse; }

    public ApiException(Exception ex, ErrorResponse error) {

        super(ex);

        this.errorResponse = error;
    }

    public ApiException(ErrorResponse errorResponse, Throwable exception) {

        super(exception);
        this.errorResponse = errorResponse;
    }

    public ErrorResponse getErrorResponse() { return this.errorResponse; }
}
