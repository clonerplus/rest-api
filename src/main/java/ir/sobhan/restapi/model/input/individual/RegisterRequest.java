package ir.sobhan.restapi.model.input.individual;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String username;
    private String password;
    private String phone;
    private String nationalId;
    private boolean admin;
    private boolean active;
}
