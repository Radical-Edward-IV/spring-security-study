package edward.iv.restapi.security.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignInRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    public SignInRequest(String username, String password) {

        this.username = username;
        this.password = password;
    }
}
