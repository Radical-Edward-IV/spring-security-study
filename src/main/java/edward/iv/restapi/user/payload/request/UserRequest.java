package edward.iv.restapi.user.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import edward.iv.restapi.role.model.dto.RoleName;
import edward.iv.restapi.user.model.dto.AddressDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class UserRequest {

    @NotBlank
    @Size(min = 2, max = 40)
    private String firstName;

    @NotBlank
    @Size(min = 2, max = 40)
    private String lastName;

    @NotBlank
    @Size(min = 8, max = 20)
    private String username;

    @NotBlank
    @Size(min = 8, max = 20)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // @JsonIgnore 사용 시 Deserialize까지 무시되어 검증에 실패하게 됨
    private String password;

    @NotBlank
    @Size(min = 10, max = 30)
    private String phone;

    @NotBlank
    @Size(max = 40)
    @Email
    private String email;

    private AddressDto address;

    private RoleName role;
}
