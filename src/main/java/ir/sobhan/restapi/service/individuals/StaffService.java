package ir.sobhan.restapi.service.individuals;

import ir.sobhan.restapi.auth.Role;
import ir.sobhan.restapi.dao.CustomUserRepository;
import ir.sobhan.restapi.dao.StaffRepository;
import ir.sobhan.restapi.model.individual.CustomUser;
import ir.sobhan.restapi.model.individual.Staff;
import ir.sobhan.restapi.response.ListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StaffService {

    private final CustomUserRepository customUserRepository;
    private final StaffRepository staffRepository;

    @Autowired
    public StaffService(CustomUserRepository customUserRepository, StaffRepository staffRepository) {
        this.customUserRepository = customUserRepository;
        this.staffRepository = staffRepository;
    }

    public ResponseEntity<ListResponse<Staff>> getAllStaffs() {

        ListResponse<Staff> listResponse = ListResponse.<Staff>builder()
                .responseList(staffRepository.findAll())
                .build();

        return ResponseEntity.ok(listResponse);
    }

    public ResponseEntity<String> authorizeStaff(String username, Staff staff) {

        if (username == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid username!");

        Optional<CustomUser> customUser = customUserRepository.findByUsername(username);

        if (customUser.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("username not found!");

        staff.setCustomUser(customUser.get());
        customUser.get().setStaff(staff);
        customUser.get().setRole(Role.STAFF);
        staffRepository.save(staff);


        return ResponseEntity.ok("Authorized user to staff limits successfully");
    }
}
