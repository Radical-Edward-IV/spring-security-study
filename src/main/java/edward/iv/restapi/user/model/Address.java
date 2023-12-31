package edward.iv.restapi.user.model;

import edward.iv.restapi.base.audit.BaseAudit;
import edward.iv.restapi.user.dto.AddressDto;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serial;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true) // equals, hashCode 메소드 자동 생성 시 부모 클래스의 필드까지 감안하여 생성
@Accessors(chain = true) // Setter로 객체 설정 시 Chaining을 가능하게 해준다.
@Entity
@Table(name = "ADDRESS")
public class Address extends BaseAudit {

    @Serial
    private static final long serialVersionUID = 1950301872971220671L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "ADDRESS_LINE_01")
    private String addressLine01;

    @Column(name = "ADDRESS_LINE_02")
    private String addressLine02;

    @Column(name = "CITY")
    private String city;

    @Column(name = "STATE")
    private String state;

    @Column(name = "ZIP_CODE")
    private String zipCode;

    @OneToOne
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
    private User user;
    
    public void updateAddressByDto(AddressDto srcAddr) {

        if (srcAddr.getAddressLine01() != null) this.setAddressLine01(srcAddr.getAddressLine01());
        if (srcAddr.getAddressLine02() != null) this.setAddressLine02(srcAddr.getAddressLine02());
        if (srcAddr.getCity() != null) this.setCity(srcAddr.getCity());
        if (srcAddr.getState() != null) this.setState(srcAddr.getState());
        if (srcAddr.getZipCode() != null) this.setZipCode(srcAddr.getZipCode());
    }
}