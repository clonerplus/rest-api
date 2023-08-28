package ir.sobhan.restapi.service.individuals;

import ir.sobhan.restapi.auth.Role;
import ir.sobhan.restapi.controller.exceptions.ApiRequestException;
import ir.sobhan.restapi.dao.CustomUserRepository;
import ir.sobhan.restapi.dao.StaffRepository;
import ir.sobhan.restapi.model.individual.CustomUser;
import ir.sobhan.restapi.model.individual.Staff;
import ir.sobhan.restapi.response.ListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    public void authorizeStaff(String username, Staff staff) {

        if (username == null)
            throw new ApiRequestException("Invalid username!", HttpStatus.BAD_REQUEST);

        CustomUser customUser = customUserRepository.findByUsername(username)
                .orElseThrow(() -> new ApiRequestException("username not found!", HttpStatus.NOT_FOUND));

        staff.setCustomUser(customUser);
        customUser.setStaff(staff);
        customUser.setRole(Role.STAFF);
        staffRepository.save(staff);
    }
}
