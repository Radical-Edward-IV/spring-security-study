package edward.iv.restapi.user.controller;

import edward.iv.restapi.annotation.CurrentUser;
import edward.iv.restapi.base.payload.response.PageResponse;
import edward.iv.restapi.debug.model.dto.DebugInfo;
import edward.iv.restapi.role.model.dto.RoleName;
import edward.iv.restapi.security.UserPrincipal;
import edward.iv.restapi.user.model.dto.UserDto;
import edward.iv.restapi.user.model.entity.User;
import edward.iv.restapi.user.payload.request.UserRequest;
import edward.iv.restapi.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;

import static edward.iv.restapi.base.AppConstants.ASC;
import static edward.iv.restapi.base.AppConstants.DESC;
import static edward.iv.restapi.base.Utils.getPageResponse;
import static edward.iv.restapi.base.Utils.getPageable;

/**
 * 사용자 정보를 다루는 Controller 입니다.
 *
 * @author Edward Se Jong Pepelu Tivrusky IV
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final DebugInfo debug;

    private final UserService userService;

    /**
     * 신규 사용자를 등록합니다.
     *
     * @param userRequest 신규 사용자 정보
     * @return ResponseEntity
     * @throws URISyntaxException
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> addUser(@Valid @RequestBody UserRequest userRequest) throws URISyntaxException {

        log.debug("[{}] POST /api/v1/users - UserRequest: {})", debug.getDebugId(), userRequest);

        UserDto newcomer = userService.addUser(userRequest);

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/users/{username}")
                .buildAndExpand(newcomer.getUsername()).toUri();

        return ResponseEntity.created(location).body(newcomer);
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

        log.debug("[{}] GET /api/v1/users - RequestParam: page({}), size({})", debug.getDebugId(), page, size);

        Pageable pageable = getPageable(page, size, DESC, "id");
        Page<User> users = userService.getUsers(pageable);

        PageResponse<UserDto> payload = getPageResponse(users, UserDto::entitiesToDtoList);

        return ResponseEntity.ok(payload);
    }

    /**
     * 로그인 사용자 정보를 조회합니다.
     *
     * @param currentUser SecurityContext에 Authentication 객체를 참조하여 로그인 사용자 정보를 가져온다.
     * @return ResponseEntity
     */
    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser(@CurrentUser UserPrincipal currentUser) {

        log.debug("[{}] GET /api/v1/users/me - UserPrincipal: {}", debug.getDebugId(), currentUser);

        return ResponseEntity.ok(UserDto.principalToDto(currentUser));
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

        log.debug("[{}] GET /api/v1/users/{} - RequestParam: page({}), size({})", debug.getDebugId(), page, size);

        Pageable nativePageable = getPageable(page, size, ASC, "last_name", "first_name", "user_name");
        Page<User> users = userService.getUserByUsernameOrRealName(nativePageable, username);

        PageResponse<UserDto> payload = getPageResponse(users, UserDto::entitiesToDtoList);
        return payload;
    }

    /**
     * 사용자 정보를 갱신합니다.<br />
     * • ADMIN 권한이 있는 사용자는 모든 사용자를 갱신할 수 있습니다.<br />
     * • 로그인 사용자는 자신의 정보를 갱신할 수 있습니다.
     *
     * @param currentUser SecurityContext에 Authentication 객체를 참조하여 로그인 사용자 정보를 가져온다.
     * @param user 갱신할 대상 사용자의 정보
     * @return ResponseEntity
     */
    //@PutMapping("/{username}")
    @PutMapping
    public ResponseEntity<UserDto> updateUser(@CurrentUser UserPrincipal currentUser, @Valid @RequestBody UserRequest user) {

        log.debug("[{}] PUT /api/v1/users/{} - UserPrincipal: {}, UserRequest: {}", debug.getDebugId(), currentUser, user);

        UserDto updatedUser = userService.updateUser(user, UserDto.principalToDto(currentUser));
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * 사용자 권한을 할당합니다.<br />
     * • 특정 사용자에게 권한을 할당하기 위해서는 ADMIN 권한이 필요합니다.
     *
     * @param username 권한을 할당할 대상 사용자
     * @param role 할당할 권한
     * @return
     */
    @PatchMapping("/{username}/give/{role}")
    public ResponseEntity<UserDto> updateUserRole(@PathVariable String username, @PathVariable String role) {

        log.debug("[{}] PATCH /api/v1/users/{}/give/{}", debug.getDebugId(), username, role);

        RoleName roleName = RoleName.valueOf(role.toUpperCase(Locale.ROOT));

        UserDto user = userService.updateUserRole(username, roleName);

        return ResponseEntity.ok(user);
    }
}