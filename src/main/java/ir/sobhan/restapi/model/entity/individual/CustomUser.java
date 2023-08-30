package ir.sobhan.restapi.model.entity.individual;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import ir.sobhan.restapi.auth.*;
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
@Table(indexes = {
        @Index(name = "customUserId", columnList = "id"),
        @Index(name = "username", columnList = "username")
})
public class CustomUser implements UserDetails {
    @Id
    @GeneratedValue
    private long id;
    @Column(name = "username", unique = true)
    private String username;
    private String password;
    private String phone;
    private String nationalId;
    private boolean admin;
    private boolean active;
    @JsonManagedReference
    @OneToOne(mappedBy = "customUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private Instructor instructor;
    @JsonManagedReference
    @OneToOne(mappedBy = "customUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private Staff staff;
    @JsonManagedReference
    @OneToOne(mappedBy = "customUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private Student student;
    @Enumerated(EnumType.STRING)
    private Role role;
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
