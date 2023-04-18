package br.com.jacto.cloverindustryauth.model.user;

import br.com.jacto.cloverindustryauth.dto.accesslevel.AccessLevelResponseDto;
import br.com.jacto.cloverindustryauth.dto.user.UserCreateRequestDto;
import br.com.jacto.cloverindustryauth.model.accesslevel.AccessLevel;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity(name = "User")
@Table(name = "users")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class User implements Serializable, UserDetails {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;

    private String email;

    private String password;

    private LocalDateTime created_at;

    @ManyToMany
    @JoinTable(name = "users_accesslevels",
            joinColumns = {
                    @JoinColumn(name = "user_id", referencedColumnName = "id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "accesslevel_id", referencedColumnName = "id")
            }
    )
    @Fetch(FetchMode.JOIN)
    private List<AccessLevel> access_levels;

    public User(String name, String email, String password, LocalDateTime created_at, List<AccessLevelResponseDto> access_levels) {}

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (AccessLevel accessLevel : access_levels) {
            authorities.add(new SimpleGrantedAuthority(accessLevel.getAccess_level()));
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
