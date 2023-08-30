package ir.sobhan.restapi.controller.individuals;

import ir.sobhan.restapi.model.entity.individual.Staff;
import ir.sobhan.restapi.model.input.individual.StaffRequest;
import ir.sobhan.restapi.model.output.ListResponse;
import ir.sobhan.restapi.service.individuals.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
public class StaffController {
    private final StaffService staffService;

    @Autowired
    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    @GetMapping("/all-staffs")
    public ResponseEntity<ListResponse<Staff>> getAllInstructors() {
        return ResponseEntity.ok(staffService.getAllStaffs());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/authorize/staff")
    public ResponseEntity<String> authorizeInstructor(@RequestParam String username,
                                                      @RequestBody StaffRequest staffRequest) {
        staffService.authorizeStaff(username, staffRequest);
        return ResponseEntity.ok("Authorized user to staff limits successfully");
    }
}
