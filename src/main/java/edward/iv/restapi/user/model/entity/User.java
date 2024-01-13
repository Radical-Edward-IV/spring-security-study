package edward.iv.restapi.user.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edward.iv.restapi.base.audit.BaseDateTimeAudit;
import edward.iv.restapi.exception.BadRequestException;
import edward.iv.restapi.security.payload.request.SignUpRequest;
import edward.iv.restapi.role.model.entity.Role;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true) // equals, hashCode 메소드 자동 생성 시 부모 클래스의 필드까지 감안하여 생성
@Accessors(chain = true) // Setter로 객체 설정 시 Chaining을 가능하게 해준다.
@Entity
@Table(name = "USERS", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"USER_NAME"}),
        @UniqueConstraint(columnNames = {"EMAIL"})
})
public class User extends BaseDateTimeAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "FIRST_NAME", length = 40)
//    @Size(max = 40)
//    @NotBlank
    private String firstName;

    @Column(name = "LAST_NAME", length = 40)
//    @Size(max = 40)
//    @NotBlank
    private String lastName;

    @Column(name = "USER_NAME", length = 20)
//    @Size(max = 20)
//    @NotBlank
    private String username;

    @Column(name = "ENCRYPTED_PASSWORD", length = 100)
//    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // @JsonIgnore 사용 시 Deserialize까지 무시되어 검증에 실패하게 됨
//    @Size(max = 100)
//    @NotBlank
    private String password;

    @Column(name = "PHONE", length = 30)
    private String phone;

    @Column(name = "EMAIL", length = 40)
//    @Size(max = 40)
//    @NotBlank
//    @Email
    private String email;

    /*
     * ⦿ JPA CacadeType 종류
     *     • ALL: 상위 엔터티에서 하위 엔터티로 모든 작업을 전파
     *     • PERSIST: 하위 엔터티까지 영속성 전달 (User 엔터티를 저장하면 Address 엔터티도 저장)
     *     • MERGE: 하위 엔터티까지 병합 작업을 지속 (Address와 User 엔터티를 조회한 후 업데이트)
     *     • REMOVE: 하위 엔터티까지 제거 작업을 지속 (연결된 하위 엔터티까지 제거)
     *     • REFRESH: 데이터베이스로부터 인스턴스 값을 다시 읽어옴 (하위 엔터티까지 인스턴스 값 새로고침)
     *     • DETACH: 영속성 컨텍스트에서 엔터티 제거 (연결된 하위 엔터티까지 영속성 제거)
     *
     * ⦿ orphanRemoval
     *     • 고아 객체: 부모 엔터티와 연관 관계가 끊어진 엔터티
     *     • 부모 엔터티 삭제되면 자식 엔터티도 삭제됨
     *     • 즉, 부모가 자식의 삭제 생명주기를 관라함
     *     • 부모, 자식 엔터티 사이의 연관 관계를 제거하면, 자식 엔터티틑 고아 객체로 취급되어 삭제됨
     */
    @JsonIgnore
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Address address;

    @ManyToOne(fetch = FetchType.EAGER)
    private Role role;

    public User setAddress(Address address) {

        if (address == null) return this;

        this.address = address;
        address.setUser(this);
        return this;
    }

    public void updateUserBySignUpRequest(SignUpRequest srcUser) {

        if (!this.firstName.equals(srcUser.getFirstName()) || !this.lastName.equals(srcUser.getLastName())) {
            throw new BadRequestException("Sorry, your name cannot be changed.");
        }
        this.password = srcUser.getPassword();
        this.phone    = srcUser.getPhone();
        this.email    = srcUser.getEmail();
    }

//    @ManyToMany(fetch = FetchType.EAGER) // 즉시 로딩, User와 Role을 함께 조회
//    @JoinTable(name = "ROLE",
//            joinColumns = @JoinColumn(name = "USER_ID", referencedColumnName = "id"), // 현재 Entity를 참조하는 외래키
//            inverseJoinColumns = @JoinColumn(name = "ROLE_ID", referencedColumnName = "id")) // 반대 방향 Entity를 참조하는 외래키
//    private List<Role> role;
}