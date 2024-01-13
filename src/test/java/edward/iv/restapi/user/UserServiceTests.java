package edward.iv.restapi.user;

import edward.iv.restapi.user.payload.request.UserRequest;
import edward.iv.restapi.role.model.entity.Role;
import edward.iv.restapi.role.model.dto.RoleName;
import edward.iv.restapi.role.repository.RoleRepository;
import edward.iv.restapi.user.model.dto.AddressDto;
import edward.iv.restapi.user.model.dto.UserDto;
import edward.iv.restapi.user.model.entity.Address;
import edward.iv.restapi.user.model.entity.User;
import edward.iv.restapi.user.repository.AddressRepository;
import edward.iv.restapi.user.repository.UserRepository;
import edward.iv.restapi.user.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static edward.iv.restapi.role.model.dto.RoleName.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * <h2>Service 단위 테스트</h2>
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UserServiceTests {

    // 의존성을 필요로 하는 Field에 사용하며, @Mock이나 @Spy 애노테이션이 붙은 Field를 주입해준다.
    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    private List<User> userMemoryRepository;

    @Mock
    private RoleRepository roleRepository;

    private List<Role> roleMemoryRepository;

    @Mock
    private AddressRepository addressRepository;

    @Spy
    private PasswordEncoder passwordEncoder;

    @DisplayName("사용자 생성")
    @Test
    void userAddTest() {

        AddressDto address = new AddressDto("Test01", "Test02", "City", "State", "ZipCode");

        UserRequest signUp = UserRequest.builder()
                .firstName("In").lastName("Im")
                .username("db_admin003").password("1q2w3e4r!")
                .phone("010-9876-5432").email("auto-q@email.com")
                .address(address)
                .build();

        userService.addUser(signUp);

        verify(userRepository, times(1)).existsByUsername(any(String.class));
        verify(userRepository, times(1)).existsByEmail(any(String.class));
    }

    @DisplayName("사용자 권한 변경")
    @Test
    void updateUserRoleTest() {

        User user = userService.getUserById(2L);

        Assertions.assertEquals(USER, user.getRole().getName());

        UserDto updatedUser = userService.updateUserRole(user.getUsername(), ADMIN);

        Assertions.assertEquals(ADMIN.toString(), updatedUser.getRole());
    }

    @BeforeEach
    void setMockUserRepository() {

        /*
         * Arrays.of() 메소드는 Fixed-size list를 반환합니다.
         * add() 메소드를 사용할 경우 java.lang.UnsupportedOperationException이 발생합니다.
         */
        userMemoryRepository = new ArrayList<>();

        userMemoryRepository.addAll(Arrays.asList(
                new User()
                        .setId(1L)
                        .setFirstName("Edward Se Jong Pepelu Tivrusky")
                        .setLastName("Kim")
                        .setUsername("admin001")
                        .setPhone("010-1234-5678")
                        .setEmail("radical-edward@email.com")
                        .setAddress(null)
                        .setRole(new Role(ADMIN)),
                new User()
                        .setId(2L)
                        .setFirstName("Theodore")
                        .setLastName("Twombly")
                        .setUsername("db_admin001")
                        .setPhone("010-4433-6677")
                        .setEmail("theo@email.com")
                        .setAddress(null)
                        .setRole(new Role(USER)),
                new User()
                        .setId(3L)
                        .setFirstName("Wade")
                        .setLastName("Ripple")
                        .setUsername("modeler001")
                        .setPhone("010-9876-5432")
                        .setEmail("elemental@email.com")
                        .setAddress(null)
                        .setRole(new Role(USER)),
                new User()
                        .setId(4L)
                        .setFirstName("Sook")
                        .setLastName("Kim")
                        .setUsername("user0001")
                        .setPhone("010-2222-5555")
                        .setEmail("dkdlemf@email.com")
                        .setAddress(null)
                        .setRole(new Role(USER)),
                new User()
                        .setId(5L)
                        .setFirstName("Sun")
                        .setLastName("Im")
                        .setUsername("developer001")
                        .setPhone("010-8989-7777")
                        .setEmail("sundal@email.com")
                        .setAddress(null)
                        .setRole(new Role(USER)),
                new User()
                        .setId(6L)
                        .setFirstName("Hyeok")
                        .setLastName("Kim")
                        .setUsername("developer002")
                        .setPhone("010-6655-4433")
                        .setEmail("blacksocks@email.com")
                        .setAddress(null)
                        .setRole(new Role(USER)),
                new User()
                        .setId(7L)
                        .setFirstName("Junny")
                        .setLastName("Im")
                        .setUsername("developer003")
                        .setPhone("010-8282-1199")
                        .setEmail("junny@email.com")
                        .setAddress(null)
                        .setRole(new Role(USER))
        ));

        when(userRepository.findAll()).thenReturn(this.userMemoryRepository);

        when(userRepository.findByUsername(anyString())).then(invocation -> {

            String username = invocation.getArgument(0, String.class);

            return userMemoryRepository.stream().filter(u -> username.equals(u.getUsername())).findFirst();
        });

        when(userRepository.getReferenceById(any(Long.class))).then(invocation -> {

            User user = userMemoryRepository.stream().filter(u -> u.getId() == invocation.getArgument(0, Long.class)).findFirst().get();

            return user;
        });

        when(userRepository.save(any(User.class))).then(invocation -> {

            User user = invocation.getArgument(0, User.class);
            this.userMemoryRepository.add(user);
            return user;
        });

        when(userRepository.existsByUsername(any(String.class))).then(invocation -> {

            String username = invocation.getArgument(0, String.class);

            return userMemoryRepository.stream().filter(user -> username.equals(user.getUsername()))
                    .findFirst().isPresent();
        });
    }

    @BeforeEach
    void setMockRoleRepository() {

        roleMemoryRepository = List.of(
                new Role().setId(1).setName(ADMIN),
                new Role().setId(2).setName(USER),
                new Role().setId(3).setName(DBA),
                new Role().setId(4).setName(MODELER),
                new Role().setId(5).setName(DEVELOPER)
        );

        when(roleRepository.findByName(any(RoleName.class))).then(invocation -> {

            RoleName roleName = invocation.getArgument(0, RoleName.class);

            return roleMemoryRepository.stream().filter(role -> role.getName() == roleName).findFirst();
        });
    }

    @BeforeEach
    void setMockAddressRepository() {

        when(addressRepository.save(any(Address.class))).thenReturn(null);
    }

    @Test
    void mockTest() {

        List<User> users = this.userRepository.findAll();
        Assertions.assertEquals(7, users.size());

        User user = new User()
                .setId(8L)
                .setFirstName("In")
                .setLastName("Im")
                .setUsername(("db_admin003"))
                .setPhone("010-9876-5432")
                .setEmail("auto2-q@email.com")
                .setAddress(null)
                .setRole(new Role(USER));
        User savedUser = userRepository.save(user);

        boolean existsUser = userRepository.existsByUsername("db_admin003");

        Assertions.assertEquals(8, userMemoryRepository.size());
        Assertions.assertTrue(existsUser);

        User foundUser = userRepository.getReferenceById(2L);

        Assertions.assertEquals(userMemoryRepository.get(1), foundUser);
    }
}
