package edward.iv.restapi.user;

import edward.iv.restapi.base.Utils;
import edward.iv.restapi.exception.ResourceNotFoundException;
import edward.iv.restapi.base.payload.response.PageResponse;
import edward.iv.restapi.role.model.entity.Role;
import edward.iv.restapi.role.repository.RoleRepository;
import edward.iv.restapi.user.model.dto.UserDto;
import edward.iv.restapi.user.model.entity.User;
import edward.iv.restapi.user.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

import static edward.iv.restapi.role.model.dto.RoleName.ADMIN;
import static edward.iv.restapi.role.model.dto.RoleName.USER;

@DataJpaTest
public class UserJpaTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    // Fixtures
    private Pageable pageable;

    private Pageable native_pageable;

    private List<UserDto> expected;

    @BeforeEach
    void setUp() {

        this.pageable = PageRequest.of(0, 3, Sort.Direction.ASC, "lastName", "firstName", "username");
        this.native_pageable = PageRequest.of(0, 3, Sort.Direction.ASC, "last_Name", "first_Name", "user_name");

        // 기등록된 사용자 목록
        expected = List.of(
                new UserDto(
                        "Edward Se Jong Pepelu Tivrusky", "Kim", "admin001",
                        "010-1234-5678", "radical-edward@email.com", null,
                        ADMIN.name()),
                new UserDto(
                        "Theodore", "Twombly", "db_admin001",
                        "010-4433-6677", "theo@email.com", null,
                        USER.name()),
                new UserDto(
                        "Wade", "Ripple", "modeler001",
                        "010-9876-5432", "elemental@email.com", null,
                        USER.name()),
                new UserDto(
                        "Sook", "Kim", "user0001",
                        "010-2222-5555", "dkdlemf@email.com", null,
                        USER.name()),
                new UserDto(
                        "Sun", "Im", "developer001",
                        "010-8989-7777", "sundal@email.com", null,
                        USER.name()),
                new UserDto(
                        "Hyeok", "Kim", "developer002",
                        "010-6655-4433", "blacksocks@email.com", null,
                        USER.name()),
                new UserDto(
                        "Junny", "Im", "developer003",
                        "010-8282-1199", "junny@email.com", null,
                        USER.name())
        );

    }

    @DisplayName("전체 사용자 조회")
    @Test
    void findAllUsers() {

        // 전체 사용자 조회
        Page<User> users = userRepository.findAll(pageable);

        /*
         * 응답 데이터 생성:
         *     ① Entity를 DTO로 변환
         *     ② PageResponse 객체 생성하여 반환
         */
        PageResponse<UserDto> result = Utils.getPageResponse(users, UserDto::entitiesToDtoList);

        // 검증
        result.getData().forEach(user -> Assertions.assertTrue(expected.contains(user)));
        Assertions.assertEquals(3, result.getData().size());
    }

    @DisplayName("이메일 중복 체크")
    @Test
    void existsByEmailTest() {

        // 특정 사용자 조회
        Page<User> users = userRepository.findUserByUsernameLikeOrRealNameLike(native_pageable, "%001%");

        /*
         * 응답 데이터 생성:
         *     ① Entity를 DTO로 변환
         *     ② PageResponse 객체 생성하여 반환
         */
        PageResponse<UserDto> result = Utils.getPageResponse(users, UserDto::entitiesToDtoList);

        // 검증
        Assertions.assertEquals(1, result.getPage());
        Assertions.assertEquals(3, result.getSize());
        Assertions.assertEquals(2, result.getTotalPage());
        Assertions.assertEquals(5, result.getTotalElement());
        Assertions.assertTrue(result.isFirst());
        Assertions.assertTrue(!result.isLast());
        result.getData().forEach(user -> {
            Assertions.assertTrue(expected.contains(user));
        });
    }

    @DisplayName("사용자 권한 변경")
    @Test
    public void updateUserRoleTest() {

        Long userId = 2L;

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId.toString()));

        Assertions.assertEquals(USER, user.getRole().getName());

        Role admin = roleRepository.findByName(ADMIN)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId.toString()));

        userRepository.save(user.setRole(admin));

        user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId.toString()));

        Assertions.assertEquals(ADMIN, user.getRole().getName());
    }
}