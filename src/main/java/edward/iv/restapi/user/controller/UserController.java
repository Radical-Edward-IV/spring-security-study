package edward.iv.restapi.user.controller;

import edward.iv.restapi.payload.request.SignUpRequest;
import edward.iv.restapi.payload.response.PageResponse;
import edward.iv.restapi.security.CurrentUser;
import edward.iv.restapi.security.UserPrincipal;
import edward.iv.restapi.user.dto.UserDto;
import edward.iv.restapi.user.model.User;
import edward.iv.restapi.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static edward.iv.restapi.base.AppConstants.ASC;
import static edward.iv.restapi.base.AppConstants.DESC;
import static edward.iv.restapi.base.Utils.getPageResponse;
import static edward.iv.restapi.base.Utils.getPageable;

/**
 * 사용자 정보를 다루는 Controller 입니다.
 *
 * @author Edward Se Jong Pepelu Tivrusky IV
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    /**
     * 로그인 사용자 정보를 조회합니다.
     *
     * @param currentUser SecurityContext에 Authentication 객체를 참조하여 로그인 사용자 정보를 가져온다.
     * @return ResponseEntity
     */
    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser(@CurrentUser UserPrincipal currentUser) {

        return ResponseEntity.ok(UserDto.principalToDto(currentUser));
    }

    /**
     * ADMIN 권한이 있는 사용자가 전체 사용자 목록을 조회합니다.
     *
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return ResponseEntity
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageResponse<UserDto>> getUsers(
            @RequestParam(defaultValue = "1", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size) {

        Pageable pageable = getPageable(page, size, DESC, "id");
        Page<User> users = userService.getUsers(pageable);

        PageResponse<UserDto> payload = getPageResponse(users, UserDto::entitiesToDtoList);

        return ResponseEntity.ok(payload);
    }

    /**
     * 사용자 ID 또는 사용자명을 키워드로 사용자 목록을 조회합니다.
     *
     * @param username 사용자 ID 또는 사용자명
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return ResponseEntity
     */
    @GetMapping("/{username}")
    public PageResponse<UserDto> getUser(
            @PathVariable String username,
            @RequestParam(defaultValue = "1", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size) {

        Pageable nativePageable = getPageable(page, size, ASC, "last_name", "first_name", "user_name");
        Page<User> users = userService.getUserByUsernameOrRealName(nativePageable, username);

        PageResponse<UserDto> payload = getPageResponse(users, UserDto::entitiesToDtoList);
        return payload;
    }

    /**
     * 신규 사용자를 등록합니다.
     *
     * @param signUpRequest 신규 사용자 정보
     * @return ResponseEntity
     * @throws URISyntaxException
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> addUser(@Valid @RequestBody SignUpRequest signUpRequest) throws URISyntaxException {

        UserDto newcomer = userService.addUser(signUpRequest);

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/users/{username}")
                .buildAndExpand(newcomer.getUsername()).toUri();

        return ResponseEntity.created(location).body(newcomer);
    }

    @PutMapping("/{username}")
    public ResponseEntity<UserDto> updateUser(@CurrentUser UserPrincipal currentUser,
                                              @Valid @RequestBody SignUpRequest user) {

        UserDto updatedUser = userService.updateUser(user, UserDto.principalToDto(currentUser));
        return ResponseEntity.ok(updatedUser);
    }
}
