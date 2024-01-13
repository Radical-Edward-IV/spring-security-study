package edward.iv.restapi.user.service.impl;

import edward.iv.restapi.user.payload.request.UserRequest;
import edward.iv.restapi.exception.ApiException;
import edward.iv.restapi.exception.AppException;
import edward.iv.restapi.exception.ResourceNotFoundException;
import edward.iv.restapi.exception.UnauthorizedException;
import edward.iv.restapi.security.payload.request.SignUpRequest;
import edward.iv.restapi.base.payload.response.ApiResponse;
import edward.iv.restapi.role.model.entity.Role;
import edward.iv.restapi.role.model.dto.RoleName;
import edward.iv.restapi.role.repository.RoleRepository;
import edward.iv.restapi.user.model.dto.AddressDto;
import edward.iv.restapi.user.model.dto.UserDto;
import edward.iv.restapi.user.model.entity.Address;
import edward.iv.restapi.user.model.entity.User;
import edward.iv.restapi.user.repository.AddressRepository;
import edward.iv.restapi.user.repository.UserRepository;
import edward.iv.restapi.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static edward.iv.restapi.role.model.dto.RoleName.ADMIN;

@RequiredArgsConstructor
@Transactional
@Service
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final AddressRepository addressRepository;

    @Override
    public User getUserById(Long id) { return userRepository.getReferenceById(id); }

    @Override
    public List<User> getUsers() { return userRepository.findAll(); }

    @Override
    public Page<User> getUsers(Pageable pageable) { return userRepository.findAll(pageable); }

    @Override
    public Page<User> getUserByUsernameOrRealName(Pageable pageable, String username) {

        username = "%" + username + "%";

        Page<User> users = userRepository.findUserByUsernameLikeOrRealNameLike(pageable, username);

        if (users.getTotalElements() == 0) new ResourceNotFoundException("User", "username", username);

        return users;
    }

//    @Override
    public UserDto addUser(UserRequest userRequest) {

        // 사용자명 중복 체크
        if (Boolean.TRUE.equals(userRepository.existsByUsername(userRequest.getUsername()))) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Username is already taken");
        }

        // 이메일 중복 체크
        if (Boolean.TRUE.equals(userRepository.existsByEmail(userRequest.getEmail()))) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Email is already taken");
        }

        // 사용자 추가
        String firstName = userRequest.getFirstName();

        String lastName  = userRequest.getLastName();

        String username  = userRequest.getUsername();

        String password  = passwordEncoder.encode(userRequest.getPassword());

        String phone     = userRequest.getPhone();

        String email     = userRequest.getEmail();

        User user = new User()
                .setFirstName(firstName).setLastName(lastName)
                .setUsername(username).setPassword(password)
                .setPhone(phone).setEmail(email);

        // 권한 추가
        Role role;

        if (userRepository.count() == 0) {
            role = roleRepository.findByName(ADMIN)
                    .orElseThrow(() -> new AppException("USER_ROLE_NOT_SET"));
        } else {
            role = roleRepository.findByName(RoleName.USER)
                    .orElseThrow(() -> new AppException("USER_ROLE_NOT_SET"));
        }

        user.setRole(role);

        User result = userRepository.save(user);

        // 주소 추가
        AddressDto addressDto  = userRequest.getAddress();

        Address address = null;

        if (addressDto != null) {

            address = new Address()
                    .setAddressLine01(addressDto.getAddressLine01())
                    .setAddressLine02(addressDto.getAddressLine02())
                    .setCity(addressDto.getCity())
                    .setState(addressDto.getState())
                    .setZipCode(addressDto.getZipCode())
                    .setUser(result);

            address.setCreatedBy(result.getId());
            address.setLastModifiedBy(result.getId());

            addressRepository.save(address);
        }

        /*
          created 메소드를 호출할 경우 Http 201 응답을 클라이언트에게 반환하게 된다.
          201 응답은 새로운 자원이 정상적으로 생성됨을 의미한다.
          URI 타입의 매개 변수는 자원에 접근할 수 있는 경로를 제공하는 역할을 한다.
         */
        return UserDto.entityToDto(result.setAddress(address));
    }

    @Override
    public UserDto updateUser(SignUpRequest userToBeUpdated, UserDto currentUser) {

        String username = userToBeUpdated.getUsername();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        // 현재 로그인한 사용자가 ADMIN || 현재 로그인한 사용자와 변경할 사용자가 동일함
        if (currentUser.getRole().equals(ADMIN.toString()) || currentUser.getUsername().equals(username)) {

            Address address = null;

            if (userToBeUpdated.getAddress() != null) {

                AddressDto addressToBeUpdated = userToBeUpdated.getAddress();

                address = addressRepository.findByUserId(user.getId())
                        .orElse(new Address());

                address.updateAddressByDto(addressToBeUpdated);
            } else {

                addressRepository.findByUserId(user.getId())
                        .ifPresent(addr -> addressRepository.delete(addr));
            }

            user.updateUserBySignUpRequest(userToBeUpdated);
            user.setAddress(address);

            return UserDto.entityToDto(userRepository.save(user));
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission to update profile of: " + username);
        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public UserDto updateUserRole(String username, RoleName roleName) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "name", roleName.name()));

        user.setRole(role);

        return UserDto.entityToDto(userRepository.save(user));
    }
}