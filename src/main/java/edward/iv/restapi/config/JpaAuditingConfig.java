package edward.iv.restapi.config;

import edward.iv.restapi.security.UserPrincipal;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
@EnableJpaAuditing // JPA 테스트를 수행할 경우 다음 애노테이션이 추가되어야 함, @Import(JpaAuditingConfig.class)
public class JpaAuditingConfig {

    @Bean
    public AuditorAware<Long> auditorProvider() {
        return new SpringSecurityAuditAwareImpl();
    }

    private class SpringSecurityAuditAwareImpl implements AuditorAware<Long> {

        /**
         * 반환값은 @CreatedBy로 매핑된 필드에 사용됩니다.
         * @CreatedBy private String username;
         */
        @Override
        public Optional<Long> getCurrentAuditor() {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
                return Optional.empty();
            }

            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            return Optional.ofNullable(userPrincipal.getId());
        }
    }
}
