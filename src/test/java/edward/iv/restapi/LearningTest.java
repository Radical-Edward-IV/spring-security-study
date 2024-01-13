package edward.iv.restapi;

import edward.iv.restapi.user.payload.request.UserRequest;
import edward.iv.restapi.role.model.dto.RoleName;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class LearningTest {

    /**
     * 도메인 클래스의 검증을 편리하게 돕는 표준 검증 스펙인 BeanValidation 테스트를 수행함.
     */
    @Test
    void ValidationTest() {

        // Superclass 검증 애노테이션이 Subclass에 영향을 주는 것으로 확인함.
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        List<String> NotBlank = Arrays.asList("firstName", "lastName", "username", "password", "phone", "email", "role");

        UserRequest userRequest = UserRequest.builder()
                .firstName("Edward Se Jong Tivrusky Pepelu IV")
                .lastName("Kim")
                .build();

        Set<ConstraintViolation<UserRequest>> violations = validator.validate(userRequest);

        Assertions.assertEquals(5, violations.size());

        violations.stream().forEach(cv -> {
            String propertyName = cv.getPropertyPath().toString();
            Assertions.assertTrue(NotBlank.contains(propertyName));
        });
    }

    /**
     * ⦿ ToString(callSuper = ture) 포멧을 확인하기 위한 테스트를 수행함.<br />
     *     &nbsp;&nbsp;&nbsp;&nbsp;
     *     • Superclass, Subclass 모두 @toString이 있는 경우:<br />
     *               &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     *               UserRequest(super=SignUpRequest(...), role=ADMIN)<br />
     *     &nbsp;&nbsp;&nbsp;&nbsp;
     *     • Subclass에만 @toString이 있는 경우:<br />
     *               &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     *               UserRequest(super=edward.iv.restapi.user...UserRequest@45ed0d6f, role=ADMIM)
     */
    @Test
    void toStringCallSuperTrueTest() {

        UserRequest user = UserRequest.builder()
                .firstName("Edward Se Jong Tvirusky Pepelu IV")
                .lastName("Kim")
                .username("admin001")
                .password("1q2w3e4r!")
                .phone("010-1234-5678")
                .email("admin001@email.com")
                .role(RoleName.ADMIN)
                .build();

        System.out.println(user);
    }

    @Test
    void extendsTest() {

    }

    private class Parent {

        private String parentName;

        private int parentAge;

        public Parent(String parentName, int parentAge) {
            this.parentName = parentName;
            this.parentAge  = parentAge;
        }

        public String getName() { return this.parentName; }

        public int getAge() { return this.parentAge; }

        public ParentBuilder builder() {
            return new ParentBuilder();
        }
    }

    private class ParentBuilder {

        private String parentName;

        private int parentAge;

        public ParentBuilder parentName(String parentName) {
            this.parentName = parentName;
            return this;
        }

        public ParentBuilder parentAge(int parentAge) {
            this.parentAge = parentAge;
            return this;
        }

        public Parent build() {
            return new Parent(this.parentName, this.parentAge);
        }
    }
//    private class Child extends Parent {
//
//        private String childName;
//
//        private int childAge;
//
//        @Override
//        public String getName() { return this.childName; }
//
//        @Override
//        public int getAge() { return -1; }
//
//
//    }
}
