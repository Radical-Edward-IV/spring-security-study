package edward.iv.restapi.security.controller;

import edward.iv.restapi.base.payload.response.ApiResponse;
import edward.iv.restapi.debug.model.dto.DebugInfo;
import edward.iv.restapi.exception.ApiException;
import edward.iv.restapi.security.JwtTokenProvider;
import edward.iv.restapi.security.payload.request.SignInRequest;
import edward.iv.restapi.security.payload.request.SignUpRequest;
import edward.iv.restapi.security.payload.response.JwtAuthenticationResponse;
import edward.iv.restapi.user.model.dto.UserDto;
import edward.iv.restapi.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/api/v1/auth")
public class AuthController {

//    private static final String USER_ROLE_NOT_SET = "User role not set";

    private final DebugInfo debug;

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserService userService;

    private final UserDetailsService userDetailsService;

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> authenticationUser(@Valid @RequestBody SignInRequest signInRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signInRequest.getUsername(), signInRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtTokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) throws ApiException {

        debug.getEndpoint().add("/api/v1/auth/signup");

        log.debug("[{}] {} - SignUpRequest: {}", debug.getDebugId(), debug.getEndpoint().get(0), signUpRequest);

        UserDto newcomer = userService.addUser(signUpRequest);

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/users/{userId}")
                .buildAndExpand(newcomer.getUsername()).toUri();

        /*
          created 메소드를 호출할 경우 Http 201 응답을 클라이언트에게 반환하게 된다.
          201 응답은 새로운 자원이 정상적으로 생성됨을 의미한다.
          URI 타입의 매개 변수는 자원에 접근할 수 있는 경로를 제공하는 역할을 한다.
         */
        return ResponseEntity.created(location).body(new ApiResponse(Boolean.TRUE, "User registerd successfully"));
    }
}
