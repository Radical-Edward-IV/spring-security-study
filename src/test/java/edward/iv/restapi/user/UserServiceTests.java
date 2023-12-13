package edward.iv.restapi.user;

import edward.iv.restapi.role.Role;
import edward.iv.restapi.user.dto.UserDto;
import edward.iv.restapi.user.model.User;
import edward.iv.restapi.user.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static edward.iv.restapi.role.RoleName.ADMIN;
import static edward.iv.restapi.role.RoleName.USER;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * <h2>Service 단위 테스트</h2>
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    private List<User> userMemoryRepository;

//    @BeforeEach
//    void setUserRepository() {
//    }

    @BeforeEach
    void setMockObj() {

        userMemoryRepository = List.of(
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
        );

        when(userRepository.findAll()).thenReturn(this.userMemoryRepository);

        when(userRepository.save(any(User.class))).then(invocation -> {

            User user = invocation.getArgument(0, User.class);
            this.userMemoryRepository.add(user);
            return user;
        });

        when(userRepository.existsByUsername(any(String.class))).then(invocation -> {
            String username = invocation.getArgument(0, String.class);
            return userMemoryRepository.stream().filter(user -> {
                return user.getUsername().contains(username);
            }).collect(Collectors.toList());
        });
    }

    @Test
    void mockTest() {

        // UserRepository.findAll();
//        List<User> users = this.userRepository.findAll();
//        Assertions.assertEquals(7, users.size());

        // UserRepository.save(User user);
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

        Assertions.assertEquals(8, userMemoryRepository.size());
//        Assertions.assertEquals(savedUser, userRepository.findByUsername("db_admin003"));
    }
}
