package edward.iv.restapi.role;

import edward.iv.restapi.base.audit.BaseAudit;
import edward.iv.restapi.base.audit.BaseDateTimeAudit;
import edward.iv.restapi.user.model.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.NaturalId;

import java.io.Serial;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "ROLE")
public class Role extends BaseDateTimeAudit {

    @Serial
    private static final long serialVersionUID = -7760677320338237976L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    @NaturalId // 하단 주석 참조
    @Column(name = "NAME")
    private RoleName name;

    public Role(RoleName name) { this.name = name; }
}

/**
 * 실제로 대부분의 객체는 Natural Identifier를 가집니다. 실례로 출판 도서에 붙는 ISBN, 개인의 주민번호 등이 있습니다.
 * Model을 생성할 때 Natural Identifier를 Primary Key로 사용하는 것이 사람이 인식하기 편리합니다.
 * 그러나 프레임워크가 다루기에는 복잡한 Natural Identifier 보다는 일련의 숫자로 구성된 대리키가 적합합니다.
 *
 * 그럼에도 불구하고 우리는 Natural Identifier를 통해서 Object와 Record를 검색하게 됩니다.
 * 따라서 Natural Identifier 속성을 Unique Key로 지정하는 것이 좋습니다.
 * Hibernate는 Natural Identifier를 모델링할 수 있게 하며, API를 제공하여 검색할 수 있도록 합니다.
 *
 * """
 * This annotation is very useful when the primary key of an entity class is a surrogate key,
 * that is, a system-generated synthetic identifier, with no domain-model semantics.
 * """
 *
 * @NaturalId 애노테이션을 추가한 필드는 불변(Immutable, Default)하며, 수정자(setter)가 필요하지 않습니다.
 *
 * 데이터베이스에서 Natural Identifier를 이용하여 Entity를 조회할 때는 Hibernate Session Interface에서 제공하는 API를 이용할 수 있습니다.
 * byNaturalId, bySimpleNaturalId
 *
 * Session session = entityManager.unpwrap(Session.class);
 * Optional<Book> book = session.byNaturalId(Book.class)
 *         .using("isbn", “978-0321356680”)
 *         .loadOptional();
 *
 * 위의 API를 사용할 경우 두 개의 SQL이 실행됩니다.
 * 첫번째는 Primary Key를 조회(SELECT ID FROM BOOK WHERE ISBN = ?)하기 위한 쿼리이며,
 * 두번째는 Entity를 조회(SELECT * FROM BOOK WHERE ID = ?)하기 위한 쿼리입니다.
 * 두 개의 SQL이 실행되는 이유는 Hibernate가 내부적으로 1차 및 2차 레벨 캐시를 확인하기 위함입니다.
 * 대부분의 경우 두번째 실행되는 쿼리는 부하가 적은 편입니다. 그리고 Natural Id와 Primary Key 간에 매핑된 정보를 캐싱하고 있으므로
 * 다시 검색할 필요가 없는 경우도 있습니다.
 * 아래의 코드를 실행하면 Select Statement가 한 번만 실행됩니다.
 *
 * session.byId(Book.class).load(1L);
 * Optional<Book> book = session.byNaturalId(Book.class)
 *        .using("isbn", “978-0321356680”)
 *        .loadOptional();
 */
