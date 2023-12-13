package edward.iv.restapi.exception;

import edward.iv.restapi.payload.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 4181742273021690941L;

    private ApiResponse apiResponse;

    private String message;

    public UnauthorizedException(ApiResponse apiResponse) {

        super();
        this.apiResponse = apiResponse;
    }

    public UnauthorizedException(String message) {

        super(message);
        this.message = message;
    }

    public UnauthorizedException(String message, Throwable cause) { super(message, cause); }

    public ApiResponse getApiResponse() { return apiResponse; }

    public void setApiResponse(ApiResponse apiResponse) { this.apiResponse = apiResponse; }

    public String getMessage() { return message; }

    public void setMessage(String message) { this.message = message; }
}