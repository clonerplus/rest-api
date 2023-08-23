package ir.sobhan.restapi.model.individual;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import ir.sobhan.restapi.auth.Role;
import ir.sobhan.restapi.auth.Token;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomUser implements UserDetails {
    @Id
    @GeneratedValue
    private long id;
    private String username;
    private String password;
    private String phone;
    private String nationalId;
    private boolean admin;
    private boolean active;
    @JsonManagedReference
    @OneToOne(mappedBy = "customUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private Instructor instructor;
    @OneToOne(mappedBy = "customUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private Student student;
    @Enumerated(EnumType.STRING)
    private Role role;
    @JsonManagedReference
    @OneToMany(mappedBy = "customUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Token> tokens;


    public void setRole(Role role) {
        this.role = role;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
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
