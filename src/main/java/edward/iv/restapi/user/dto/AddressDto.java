package edward.iv.restapi.user.dto;

import edward.iv.restapi.user.model.Address;
import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {

    private String addressLine01;

    private String addressLine02;

    private String city;

    private String state;

    private String zipCode;

    public static AddressDto entityToDto(Address address) {

        if (address == null) return null;

        return new AddressDto(
                address.getAddressLine01(), address.getAddressLine02(),
                address.getCity(), address.getState(), address.getZipCode()
        );
    }
}
