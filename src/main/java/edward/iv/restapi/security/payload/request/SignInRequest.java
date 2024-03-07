package edward.iv.restapi.security.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignInRequest {

    @NotBlank
    private String username;

    @NotBlank
//    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // @JsonIgnore 사용 시 Deserialize까지 무시되어 검증에 실패하게 됨
    private String password;
}
