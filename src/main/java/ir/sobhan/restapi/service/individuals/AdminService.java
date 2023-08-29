package ir.sobhan.restapi.service.individuals;

import ir.sobhan.restapi.auth.Role;
import ir.sobhan.restapi.config.AdminConfig;
import ir.sobhan.restapi.dao.CustomUserRepository;
import ir.sobhan.restapi.model.entity.individual.CustomUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    private static AdminConfig adminConfig;
    private static CustomUserRepository customUserRepository;
    private static PasswordEncoder passwordEncoder;
    @Autowired
    public void setSecurityConfig(AdminConfig adminConfig) {
        AdminService.adminConfig = adminConfig;
    }
    @Autowired
    public void setCustomUserRepository(CustomUserRepository customUserRepository) {
        AdminService.customUserRepository = customUserRepository;
    }
    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        AdminService.passwordEncoder = passwordEncoder;
    }
    public static void createAdmin() {
        String username = adminConfig.getUsername();
        String password = adminConfig.getPassword();

        // Hash the password
        String hashedPassword = passwordEncoder.encode(password);

        // Create the user and save it to the database
        CustomUser admin = CustomUser.builder()
                .id(1)
                .username(username)
                .password(hashedPassword)
                .phone(null)
                .nationalId(null)
                .admin(true)
                .active(true)
                .instructor(null)
                .staff(null)
                .student(null)
                .role(Role.ADMIN)
                .build();
        customUserRepository.save(admin);
    }
}
