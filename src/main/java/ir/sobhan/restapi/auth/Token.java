package ir.sobhan.restapi.auth;

import com.fasterxml.jackson.annotation.JsonBackReference;
import ir.sobhan.restapi.model.individual.CustomUser;
import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Token {

    public enum TokenType {
        BEARER
    }
    @Id
    @GeneratedValue
    public long id;

    @Column(unique = true)
    public String token;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    public TokenType tokenType = TokenType.BEARER;

    public boolean revoked;

    public boolean expired;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    public CustomUser customUser;
}