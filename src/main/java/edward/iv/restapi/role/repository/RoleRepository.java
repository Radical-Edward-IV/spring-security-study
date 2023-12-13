package edward.iv.restapi.role.repository;

import edward.iv.restapi.role.Role;
import edward.iv.restapi.role.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(RoleName name);
}
