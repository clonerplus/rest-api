package ir.sobhan.restapi.service.individuals;

import ir.sobhan.restapi.auth.Role;
import ir.sobhan.restapi.dao.CustomUserRepository;
import ir.sobhan.restapi.dao.StaffRepository;
import ir.sobhan.restapi.model.individual.CustomUser;
import ir.sobhan.restapi.model.individual.Staff;
import ir.sobhan.restapi.response.ListResponse;
import org.springframework.beans.factory.annotation.Autowired;
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

    public ListResponse<Staff> getAllStaffs() {
        return ListResponse.<Staff>builder()
                .responseList(staffRepository.findAll())
                .build();
    }

    public String authorizeStaff(String username, Staff staff) {

        if (username == null)
            return "Invalid username!";

        Optional<CustomUser> customUser = customUserRepository.findByUsername(username);

        if (customUser.isEmpty())
            return "username not found!";

        staff.setCustomUser(customUser.get());
        customUser.get().setStaff(staff);
        customUser.get().setRole(Role.STAFF);
        staffRepository.save(staff);

        return "Authorized user to staff limits successfully";
    }
}
