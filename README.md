## Hi there 👋
Spring Boot, Security 공부 및 사이드 프로젝트를 목적으로 개발

- 데이터 표준관리

## Tech Stack
- Backend: Java (17), Spring Boot, Spring Security, Hibernate, Restful Services, Micro Services
- Frontend: HTML5, CSS3, JavaScript, Jquery, Thymeleaf
- Database: H2, PostgreSQL
- Build tools: Gradle
- Version Control: Git
- IDE: IntelliJ IDEA, VS Code
- Operating System: Linux, Mac, Windows

## User
| methods             | POST    | GET               | PUT | DELETE |
|---------------------|---------|-------------------|-----|--------|
| /api/v1/auth/signup | 사용자 추가  | 405               | 405 | 405    |
| /api/v1/auth/signin | 사용자 로그인 | 405               | 405 | 405    |
| /api/v1/users       |         | 전체 사용자 조회 (ADMIN) |     |        |


## Demo
<div>
  <img src="./src/main/resources/static/images/application-01.png" width="49%"/>
  <img src="./src/main/resources/static/images/application-02.png" width="49%"/>  
</div>
<div>
  <img src="./src/main/resources/static/images/application-03.png" width="49%"/>
  <img src="./src/main/resources/static/images/application-04.png" width="49%"/>  
</div>
<div>
  <img src="./src/main/resources/static/images/application-05.png" width="49%"/>  
</div>

## REST API
### 디자인 가이드
1. URI는 정보의 자원을 표현합니다. (리소스명은 되도록 명사를 사용함)   
`GET /members/{id}`
2. 컨트롤 자원을 의미하는 URI는 예외적으로 동사를 혀용합니다.   
`http://127.0.0.1/posts/{id}/duplicate`
3. 자원에 대한 행위는 HTTP Method(GET, POST, PUT, DELETE)로 표현합니다.   
`DELETE /members/{id}` (o)   
`DELETE /members/delete/{id}` (x)
4. 소문자를 사용합니다.
5. 빗금(slash, -)은 계층 관계를 표현하며, 마지막 문자로 사용하지 않습니다.   
`http://127.0.0.1/vehicles/trucks` (o)   
`http://127.0.0.1/animals/delphinidae/orcas/` (x)
6. 붙임표(Hyphen, -)는 가독성을 높이기 위해서 사용합니다. (Underscore은 사용하지 않음)   
`GET /users/{id}/first-name`
7. 리소스 간의 관계가 복잡할 경우 서브 리소스를 통해 표현할 수 있습니다.   
`GET /users/{id}/devices`   
`GET /users/{id}/like/devices`
8. 멱등(idempotent)하지 않은 요청(POST)을 처리할 경우 Content-Location을 사용하여 생성된 리소스를 식별할 수 있도록 합니다.   
    ```
    POST /users

    {
        "name": "user010"
    }
    ```
    ```
    HTTP/1.1 200 OK

    Content-Location: /users/1
    ```
9. Retry-After: 비정상적인 방법(DoS)으로 서버를 이용하려는 경우 429 Too Many Requests 오류 응답과 함께 일정 시간 요청을 막을 수 있도록 합니다.   
    ```
    HTTP/1.1 429 Too Many Requests

    Retry-After: 3600
    ```
10. 인증(Authentication)   
* `/auth`: OAuth, JWT 같은 인증 관련 리소스를 요청하는 경우 사용합니다.
* `/login`: ID, PWD를 이용한 로그인 작업에 사용합니다.
11. OPTIONS: 현재 End-point가 제공 가능한 API Method를 응답합니다.   
    `curl -X OPTIONS https://example.org -i`   
    ```
    HTTP/1.1 204 No Content
    Allow: OPTIONS, GET, HEAD, POST
    Cache-Control: max-age=604800
    Date: Thu, 13 Oct 2016 11:45:00 GMT
    Server: EOS (lax004/2813)
    ```
12. HEAD: 요청에 대한 Header 정보만 응답합니다.   
    `curl HEAD /users`
    ```
    HTTP/1.1 200 OK
    Content-Type: application/json
    Content-Length: 120
    ```
13. PATCH: 자원의 일부를 수정할 경우 사용합니다. (PUT 요청 시 바뀌지 않는 속성도 함께 보내야 함)

### Collection과 Document
`GET /departments/sales/users/3880`   
* Collection: departments, users
* Document: sales, 3880

### HTTP 응답 상태 코드
<a href="https://http.cat">
    <img src="https://http.cat/images/100.jpg" alt="100" width="50%;" />
</a>

* 2XX
  - 200: OK 
  - 201: Created (새로운 리소스 생성)
  - 202: Accepted (요청은 유효하나, 아직 처리하지 않음, 비동기)
  - 204: No Content (Body가 없는 자원 삭제 요청(DELETE))
* 4XX
  - 400: Bad Request (미리 정해진 파라미터 요구사항을 위반)
  - 401: Unauthorized (유효한 인증 정보가 없음)
  - 403: Forbidden (요청은 유효하나, 권한이 없음)
  - 404: Not Found
  - 405: Method Not Allowed (사용자 3880이 없는 경우 GET, PUT, DELETE `/users/3880` 요청에 404로 응답하지만 POST 요청에는 Method를 제공하지 않으므로 405가 적절할 수도 있음)
  - 409: Conflict (비즈니스 로직 처리가 불가능)
  - 429: Too Many Requests
* 5XX: 사용자에게 절대로 나타내서는 안됨
