package edward.iv.restapi.user.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import edward.iv.restapi.role.model.dto.RoleName;
import edward.iv.restapi.security.payload.request.SignUpRequest;
import edward.iv.restapi.user.model.dto.AddressDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class UserRequest extends SignUpRequest {

    private RoleName role;

    public UserRequest() {
        super();
    }
}