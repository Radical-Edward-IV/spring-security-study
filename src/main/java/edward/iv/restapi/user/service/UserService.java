package edward.iv.restapi.user.service;

import edward.iv.restapi.payload.request.SignUpRequest;
import edward.iv.restapi.security.UserPrincipal;
import edward.iv.restapi.user.dto.UserDto;
import edward.iv.restapi.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    public UserPrincipal getUserById(Long id);

    public List<User> getUsers();

    public Page<User> getUsers(Pageable pageable);

    public Page<User> getUserByUsernameOrRealName(Pageable pageable, String username);

    UserDto addUser(SignUpRequest signUpRequest);

    UserDto updateUser(SignUpRequest user, UserDto currentUser);
}
