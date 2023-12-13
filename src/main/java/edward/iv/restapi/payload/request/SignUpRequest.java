package edward.iv.restapi.payload.request;

import edward.iv.restapi.user.dto.AddressDto;
import edward.iv.restapi.user.model.Address;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignUpRequest {

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
    private String password;

    @NotBlank
    @Size(min = 10, max = 30)
    private String phone;

    @NotBlank
    @Size(max = 40)
    @Email
    private String email;

    private AddressDto address;
}
