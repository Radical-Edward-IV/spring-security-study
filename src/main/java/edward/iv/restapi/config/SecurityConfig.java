package edward.iv.restapi.config;

import edward.iv.restapi.security.JwtAuthenticationEntryPoint;
import edward.iv.restapi.security.JwtAuthenticationFilter;
import edward.iv.restapi.user.service.impl.CustomUserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true)
public class SecurityConfig {

// private static final String[] WHITE_LIST_URL = {
//         "/api/v1/management/sayhello"
// };

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final JwtAuthenticationEntryPoint unauthorizedHandler;

    private final CustomUserDetailsServiceImpl customUserDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, HandlerMappingIntrospector introspector
    ) throws Exception {

        http
                // csrf 비활성화: RestApi를 이용한 서버는 인증 정보를 별도로 저장하지 않음(Stateless).
                .csrf(AbstractHttpConfigurer::disable)
                // cors 비활성화, 설정 정보를 적용할 경우 Parameter를 제거한 후 메소드 호출
                .cors(AbstractHttpConfigurer::disable)
                /*
                 * Spring Security의 세션 생성 정책 설정
                 * IF_REQUIRED: 필요할 경우 세션 생성 (default)
                 * ALWAYS: 항상 Spring Security가 세션 생성
                 * NEVER: Spring Security가 생성하지 않고 존재할 경우에 사용
                 * STATELESS: Spring Security가 생성하지도 않고 존재할 경우에도 사용하지 않음
                 */
                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션에 인증(Authentication) 정보를 저장하지 않음
                // h2-console 접속을 위한 설정
                .headers(config -> config.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                // 동일한 도메인에서만 iframe 접근 허용
//                .headers(config -> config.frameOptions(Customizer.withDefaults()))
                /*
                 * AntPathRequestMatcher: Ant 스타일 패턴을 사용하며, Spring Security에서 주로 사용됩니다.
                 *     AntPathRequestMatcher("/secured") matches only the exact /secured URL
                 * MvcRequestMatcher: 주요 Spring Web MVC에서 사용됩니다(안정적).
                 *     MvcRequestMatcher("/secured") matches /secured as well as /secured/, /secured.html
                 */
                .authorizeHttpRequests(request -> request
                        // Service
                        .requestMatchers(new MvcRequestMatcher(introspector, "/api/v1/auth/**")).permitAll()
                        .requestMatchers(new MvcRequestMatcher(introspector, "/index")).permitAll()
                        .requestMatchers(new MvcRequestMatcher(introspector, "/signin-view")).permitAll()
                        .requestMatchers(new MvcRequestMatcher(introspector, "/about-view")).permitAll()
                        .requestMatchers(new MvcRequestMatcher(introspector, "/static/**")).permitAll()
                        .requestMatchers(new MvcRequestMatcher(introspector, "/css/**")).permitAll()
                        .requestMatchers(new MvcRequestMatcher(introspector, "/fonts/**")).permitAll()
                        .requestMatchers(new MvcRequestMatcher(introspector, "/images/**")).permitAll()
                        .requestMatchers(new MvcRequestMatcher(introspector, "/javascript/**")).permitAll()
                        .requestMatchers(new MvcRequestMatcher(introspector, "/scss/**")).permitAll()
                        .requestMatchers(new MvcRequestMatcher(introspector, "/favicon.png")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                // AuthenticationProvider 주입
                .authenticationProvider(authenticationProvider())
                // 인증되지 않은 사용자의 요청을 처리
//                .exceptionHandling(config -> config.authenticationEntryPoint(unauthorizedHandler))
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    GrantedAuthorityDefaults grantedAuthorityDefaults() {

        // Remove the ROLE_ prefix
        return new GrantedAuthorityDefaults("");
    }

    @Bean
    public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

}
