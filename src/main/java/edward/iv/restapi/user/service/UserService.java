package edward.iv.restapi.user.service;

import edward.iv.restapi.user.payload.request.UserRequest;
import edward.iv.restapi.security.payload.request.SignUpRequest;
import edward.iv.restapi.role.model.dto.RoleName;
import edward.iv.restapi.user.model.dto.UserDto;
import edward.iv.restapi.user.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    public User getUserById(Long id);

    public List<User> getUsers();

    public Page<User> getUsers(Pageable pageable);

    public Page<User> getUserByUsernameOrRealName(Pageable pageable, String username);

    UserDto addUser(UserRequest userRequest);

    UserDto updateUser(SignUpRequest user, UserDto currentUser);

    UserDto updateUserRole(String username, RoleName roleName);
}
