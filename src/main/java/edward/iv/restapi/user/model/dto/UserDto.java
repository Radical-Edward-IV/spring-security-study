package edward.iv.restapi.user.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import edward.iv.restapi.security.UserPrincipal;
import edward.iv.restapi.user.model.entity.User;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

    private String firstName;

    private String lastName;

    private String username;

    private String phone;

    private String email;

    private AddressDto address;

    private String role;

    public static UserDto principalToDto(UserPrincipal user) {

        return new UserDto(
                user.getFirstName(), user.getLastName(), user.getUsername(),
                user.getPhone(), user.getEmail(), user.getAddress(),
                user.getAuthorities().stream().collect(Collectors.toList()).get(0).getAuthority()
        );
    }

    public static UserDto entityToDto(User user) {

        return new UserDto(
                user.getFirstName(), user.getLastName(), user.getUsername(),
                user.getPhone(), user.getEmail(),
                AddressDto.entityToDto(user.getAddress()),
                user.getRole().getName().name()
        );
    }

    public static List<UserDto> entitiesToDtoList(List<User> users) {

        return users.stream().map(user -> entityToDto(user)).collect(Collectors.toList());
    }
}
