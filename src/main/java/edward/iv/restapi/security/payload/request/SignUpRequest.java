package edward.iv.restapi.security.payload.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import edward.iv.restapi.user.payload.request.UserRequest;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SignUpRequest extends UserRequest {

}
