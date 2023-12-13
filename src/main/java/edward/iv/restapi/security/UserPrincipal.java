package edward.iv.restapi.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edward.iv.restapi.user.dto.AddressDto;
import edward.iv.restapi.user.model.User;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class UserPrincipal implements UserDetails {

    @Serial
    private static final long serialVersionUID = 7757628273398680902L;

    private Long id;

    private String firstName;

    private String lastName;

    private String username;

    @JsonIgnore
    private String password;

    private String phone;

    private String email;

    private AddressDto address;

    private Collection<? extends GrantedAuthority> authorities;

    /**
     * 추후 업데이트 예정: 계정 만료 및 잠금 기능
     */
//    private Boolean accountNonExpired;

//    private Boolean accountNonLocked;

    public UserPrincipal(Long id, String firstName, String lastName, String username, String password,
                         String phone, String email, AddressDto address, Collection<? extends GrantedAuthority> authorities) {

        this.id        = id;

        this.firstName = firstName;

        this.lastName  = lastName;

        this.username  = username;

        this.password  = password;

        this.phone     = phone;

        this.email     = email;

        this.address   = address;

        if (authorities == null) this.authorities = null;
        else this.authorities = new ArrayList(authorities);
    }

    public static UserPrincipal create(User user) {

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.getRole().getName().name()));

        return new UserPrincipal(user.getId(), user.getFirstName(), user.getLastName(), user.getUsername(),
                user.getPassword(), user.getPhone(), user.getEmail(), AddressDto.entityToDto(user.getAddress()), authorities);
    }

    public Long getId() { return this.id; }

    public String getFirstName() { return this.firstName; }

    public String getLastName() { return this.lastName; }

    public String getPhone() { return this.phone; }

    public String getEmail() { return this.email; }

    public AddressDto getAddress() { return this.address; }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        UserPrincipal that = (UserPrincipal) obj;
        return Objects.equals(id, that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { return this.authorities; }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
