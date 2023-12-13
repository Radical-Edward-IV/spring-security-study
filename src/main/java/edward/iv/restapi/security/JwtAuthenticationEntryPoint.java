package edward.iv.restapi.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 1. Spring Security는 인증/인가 실패 시 두 가지의 예외를 발생시킵니다.
 *     - 인증 실패: AuthenticationException
 *     - 인가 실패: AccessDeniedException
 * <p>
 * 2. AuthenticationEntryPoint, AccessDeniedHandler Interface를 구현하여 예외 커스텀이 가능힙니다.
 *    인증이 되지 않은 사용자의 요청을 처리하게 됩니다.
 * <p>
 * 3. 커스텀 예외를 설정 정보에 추가합니다.
 * httpSecurity
 *         .exceptionHandling()
 *         .accessDeniedHandler(jwtAccessDeniedHandler)
 *         .authenticationEntryPoint(jwtAuthenticationEntryPoint);
 */
@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        log.error("Responding with unauthorized error. Message - {}", authException.getMessage());
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Sorry, You're not authorized to access this resource.");
    }
}
