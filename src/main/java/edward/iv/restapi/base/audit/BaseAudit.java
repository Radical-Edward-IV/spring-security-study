package edward.iv.restapi.base.audit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;

@Data
@EqualsAndHashCode(callSuper = true) // equals, hashCode 메소드 자동 생성 시 부모 클래스의 필드까지 감안하여 생성
@MappedSuperclass // 공통 필드 선언용 클래스, 상속 관계와는 무관
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(
        value = {"createdBy", "lastModifiedBy"},
        allowGetters = true // 직렬화는 가능하나, 역직렬화는 불가능하게 설정
)
public abstract class BaseAudit extends BaseDateTimeAudit {

    @Serial
    private static final long serialVersionUID = 6970074894264849532L;

    @CreatedBy
    @Column(updatable = false)
    private Long createdBy;

    @LastModifiedBy
    private Long lastModifiedBy;
}