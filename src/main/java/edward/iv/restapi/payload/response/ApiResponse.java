package edward.iv.restapi.payload.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({
        "success",
        "message"
})
public class ApiResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 3013835291199082900L;

    @JsonProperty("success")
    private boolean success;

    @JsonProperty("message")
    private String message;

    @JsonIgnore
    private HttpStatus status;

    public ApiResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}