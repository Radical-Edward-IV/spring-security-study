package edward.iv.restapi.exception;

import edward.iv.restapi.payload.response.ApiResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @ResponseStatus
 * 기본적으로 예외가 발생하면 HttpStatus.INTERNAL_SERVER_ERROR를 반환하게 됩니다.
 * 예외에 @ResponseStatus를 정의하면 해당 HttpStatus로 반환할 수 있습니다.
 */
@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    private transient ApiResponse apiResponse; // transient: 직렬화 대상에서 제외하기 위한 키워드입니다.

    private String resourceName;

    private String fieldName;

    private String fieldValue;

    public ResourceNotFoundException(String resourceName, String fieldName, String fieldValue) {

        super();
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public void setApiResponse() {

        String message = String.format("%s not found with %s: '%s'", resourceName, fieldName, fieldValue);
        this.apiResponse = new ApiResponse(Boolean.FALSE, message);
    }
}
