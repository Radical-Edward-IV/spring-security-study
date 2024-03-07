package edward.iv.restapi.exception.payload.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 필수값 : errorCode, debugId
 */
@Getter
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"name", "message", "debugId", "details"})
public class ErrorResponse {

    @JsonProperty(value = "name")
    private ErrorCode errorCode;

    private String debugId;

    private Details details;

    private ErrorResponse(ErrorCode errorCode) { this.errorCode = errorCode; }

    @JsonIgnore
    public int getStatus() {
        return this.errorCode.getStatus().value();
    }

    @JsonProperty
    public String getMessage() { return this.errorCode.getMessage(); }

    /* Set ErrorCode */
    public static ErrorResponse invalidRequest() { return new ErrorResponse(ErrorCode.INVALID_REQUEST); }

    public static ErrorResponse authenticationFailure() { return new ErrorResponse(ErrorCode.AUTHENTICATION_FAILURE); }

    /* Set DebugID */
    public ErrorResponse debugId(String debugId) {

        this.debugId = debugId;
        return this;
    }

    /* Set ErrorFieldAndValue */
    public ErrorResponse errorFieldAndValue(String field, String value) {

        if (this.details == null) this.details = new Details();

        this.details.errorFieldAndValue(field, value);

        return this;
    }

    /* Set Details::Location */
    public ErrorResponse head() {

        if (this.details == null) this.details = new Details();

        this.details.setLocation("head");

        return this;
    }

    public ErrorResponse body() {

        if (this.details == null) this.details = new Details();

        this.details.setLocation("body");

        return this;
    }

    public ErrorResponse path() {

        if (this.details == null) this.details = new Details();

        this.details.setLocation("path");

        return this;
    }

    /* Set Details::Issue */
    public ErrorResponse issue(String issue) {

        if (this.details == null) this.details = new Details();

        this.details.issue = issue;

        return this;
    }

    /* Set Details::Description */
    public ErrorResponse description(String description) {

        if (this.details == null) this.details = new Details();

        this.details.description = description;

        return this;
    }

    public ErrorResponse query() {

        if (this.details == null) this.details = new Details();

        this.details.setLocation("query");

        return this;
    }


    @RequiredArgsConstructor
    private enum ErrorCode {

        INVALID_REQUEST       (HttpStatus.BAD_REQUEST, "Request is not well-formed, syntactically incorrect, or violates schema"),
        AUTHENTICATION_FAILURE(HttpStatus.UNAUTHORIZED, "Authentication failed due to invalid authentication credentials or a missing Authorization header.");

        private final HttpStatus status;

        private final String message;

        public HttpStatus getStatus() {
            return this.status;
        }

        public String getMessage() {
            return this.message;
        }
    }

    @Getter
    @Setter
    private class Details {

        Map<String, String> fieldAndValue;

        String location;

        String issue;

        String description;

        void errorFieldAndValue(String field, String value) {

            if (this.fieldAndValue == null) this.fieldAndValue = new ConcurrentHashMap<>();

            this.fieldAndValue.put(field, value);
        }
    }
}
