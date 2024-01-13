package edward.iv.restapi.role.repository;

import edward.iv.restapi.role.model.entity.Role;
import edward.iv.restapi.role.model.dto.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(RoleName name);
}
