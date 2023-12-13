package edward.iv.restapi.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edward.iv.restapi.payload.request.SignInRequest;
import edward.iv.restapi.payload.response.JwtAuthenticationResponse;
import edward.iv.restapi.payload.response.PageResponse;
import edward.iv.restapi.security.controller.AuthController;
import edward.iv.restapi.user.controller.UserController;
import edward.iv.restapi.user.dto.UserDto;
import edward.iv.restapi.user.model.User;
import edward.iv.restapi.user.repository.UserRepository;
import edward.iv.restapi.user.service.UserService;
import org.hibernate.type.CollectionType;
import org.hibernate.type.Type;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <h2>통합테스트</h2>
 * <hr>
 * • 실제 운영 환경에서 사용할 클래스를 통합 테스트합니다.<br>
 * • 단위 테스트처럼 기능을 점검하는 것이 아닌, 전체적인 흐름을 점검하는 테스트입니다.<br>
 * • 설정 정보와 Bean을 모두 로드하여 운영 환경과 가장 유사한 환경에서 테스트를 진행할 수 있는 장점이 있습니다.<br>
 * • 시간이 오래 걸리고 무거운 편이며, 디버깅이 어렵다는 단점이 있습니다.<br>
 * <br>
 * <br>
 * <h2>MockMvc와 TestRestTemplate의 차이점</h2>
 * <hr>
 * • <b>MockMvc:</b> 내장된 서블릿 컨테이너가 아닌 Mock 서블릿을 이용합니다.
 *   서버 입장에서 구현한 비즈니스 로직이 문제 없이 수행되는지 테스트합니다.
 *   <i>@AutoConfigureMockMvc</i> 애노테이션과 함께 사용합니다.<br>
 * • <b>TestRestTemplate:</b> 내장된 서블릿 컨테이너를 이용합니다.
 *   클라이언트 입장에서 RestApi를 사용하듯이 테스트를 수행할 수 있습니다.
 *   TestRestTemplate를 사용하려면 실제 가용한 포트로 내장 톰캣을 띄울 수 있도록 설정이 필요합니다.
 *   <i>@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)</i> 옵션을 추가하고,
 *   <i>@LocalServerPort private int port</i> 멤버를 추가하여 설정할 수 있습니다.
 */
//@SpringBootTest
//@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserIntegrationTests {

//    @Autowired
//    MockMvc mvc;

    private final static String LOOPBACK = "http://127.0.0.1:";

    @LocalServerPort
    private int PORT;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AuthController authController;

    @Autowired
    private UserController userController;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private HttpEntity httpEntity;

    private ObjectMapper objMapper = new ObjectMapper();

    // fixtures..;
    private SignInRequest admin;
    private SignInRequest dba;

    @BeforeEach
    void setUp() throws JsonProcessingException {

        admin = new SignInRequest("admin001", "1q2w3e4r!");
        dba = new SignInRequest("db_admin001", "1q2w3e4r!");
    }

    @DisplayName("전체 사용자 조회::ADMIN")
    @Test
    void getAllUsersWithAdmin() throws JsonProcessingException {

        HttpEntity<String> entity = new HttpEntity<>(getAuthenticationHeader(admin));

        ResponseEntity<String> response = restTemplate
                .exchange(LOOPBACK + PORT + "/api/v1/users?page=1&size=3", HttpMethod.GET, entity, String.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        List<UserDto> users = mapJsonToObject(response.getBody(), new TypeReference<PageResponse<UserDto>>() {}).getData();

        Assertions.assertEquals(3, users.size());

        for (int i = 3; i > 0; i--) {

            String expectedUsername = "developer00" + i;

            int idx = 3 - i;
            String actualUsername = users.get(idx).getUsername();

            Assertions.assertEquals(expectedUsername, actualUsername);
        }
    }

    @DisplayName("전체 사용자 조회::DBA")
    @Test
    void getAllUsersWithDBA() {

        HttpEntity entity = new HttpEntity(getAuthenticationHeader(dba));

        ResponseEntity<String> response = restTemplate.exchange(LOOPBACK + PORT + "/api/v1/users?page=1&size=3", HttpMethod.GET, entity, String.class);

        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    private <T> T mapJsonToObject(String json, TypeReference<T> t) {

        try {
            return objMapper.readValue(json, t);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private HttpHeaders getAuthenticationHeader(SignInRequest user) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(signIn(user).getAccessToken());

        return headers;
    }

    private JwtAuthenticationResponse signIn(SignInRequest user) {

        HttpEntity entity = new HttpEntity(user);
        String body = restTemplate.exchange(LOOPBACK + PORT + "/api/v1/auth/signin", HttpMethod.POST, entity, String.class)
                .getBody();

        return mapJsonToObject(body, new TypeReference<JwtAuthenticationResponse>() {});
    }
}
