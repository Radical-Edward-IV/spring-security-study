package edward.iv.restapi.base.audit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

@Data
@MappedSuperclass // 공통 필드 선언용 클래스, 상속 관계와는 무관
@EntityListeners(AuditingEntityListener.class) // Hibernate가 DB 작업을 수행하기 전후에 호출될 수 있는 콜백 함수를 제공
@JsonIgnoreProperties(
        value = {"createdDate", "lastModifiedDate"},
        allowGetters = true // 직렬화는 가능하나, 역직렬화는 불가능하게 설정
)
public abstract class BaseDateTimeAudit implements Serializable {

    @Serial
    private static final long serialVersionUID = 6335008205386729057L;

    /*
     * Instant: EPOCH부터 경과된 시간을 나노초 단위로 표현 - UTC(+00:00)
     * LocalDateTime: Local PC의 시스템 날짜와 시간을 표현
     */
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdDate;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant lastModifiedDate;
}