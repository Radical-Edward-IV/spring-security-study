package edward.iv.restapi.user.repository;

import edward.iv.restapi.user.model.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Bean Validation: Java 표준 스펙으로 Object, Parameter, Return 값을 검증합니다.
 *
 * 1. Object 적용 예제
 *     Class: public class User { @Notnull String username; }
 *     Method: User findByUsername(@Valid User user) {...}
 *
 * 2. Parameter 적용 예제
 *     User findByUsername(@NotBlank String username)
 *
 * 3. @Notnull, @NotEmpty, @NotBlank 차이
 *     Notnull: null(x), ""(o), " "(o)
 *     NotEmpty: null(x), ""(x), " "(o)
 *     NotBlank: null(x), ""(x), " "(x)
 */
@Repository
@PropertySource("/queries.properties")
public interface UserRepository  extends JpaRepository<User, Long> {

    Optional<User> findByUsername(@NotBlank String username);

    Optional<User> findByEmail(@NotBlank String email);

    Boolean existsByUsername(@NotBlank String username);

    @Query(countQuery = "SELECT * FROM users WHERE user_name LIKE :username OR first_name LIKE :username OR last_name LIKE :username", nativeQuery = true)
    Page<User> findUserByUsernameLikeOrRealNameLike(Pageable pageable, String username);

    Boolean existsByEmail(@NotBlank String email);

    Optional<User> findByUsernameOrEmail(String username, String email);

//    default User getUserByName(String username) {
//        return findByUsername(username)
//                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
//    }
}
