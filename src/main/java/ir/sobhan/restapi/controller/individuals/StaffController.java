package ir.sobhan.restapi.controller.individuals;

import ir.sobhan.restapi.auth.Role;
import ir.sobhan.restapi.dao.*;
import ir.sobhan.restapi.model.individual.*;
import ir.sobhan.restapi.response.ListResponse;
import ir.sobhan.restapi.service.individuals.CustomUserService;
import ir.sobhan.restapi.service.individuals.InstructorService;
import ir.sobhan.restapi.service.individuals.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class StaffController {

    private final StaffService staffService;

    @Autowired
    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    @GetMapping("/all-staffs")
    public ResponseEntity<ListResponse<Staff>> getAllInstructors() {
        return staffService.getAllStaffs();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/authorize/staff")
    public ResponseEntity<String> authorizeInstructor(@RequestParam String username,
                                                      @RequestBody Staff staff) {

        return staffService.authorizeStaff(username, staff);
    }
}
