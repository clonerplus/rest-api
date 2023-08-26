package ir.sobhan.restapi.controller.individuals;

import ir.sobhan.restapi.model.individual.*;
import ir.sobhan.restapi.response.ListResponse;
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
        return ResponseEntity.ok(staffService.getAllStaffs());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/authorize/staff")
    public ResponseEntity<String> authorizeInstructor(@RequestParam String username,
                                                      @RequestBody Staff staff) {

        Map<String, HttpStatus> statusMap = new HashMap<>(
                Map.of("Invalid username!", HttpStatus.BAD_REQUEST,
                        "username not found!", HttpStatus.NOT_FOUND,
                        "Authorized user to staff limits successfully", HttpStatus.OK));

        String resultMsg = staffService.authorizeStaff(username, staff);

        return ResponseEntity.status(statusMap.get(resultMsg)).body(resultMsg);
    }
}
