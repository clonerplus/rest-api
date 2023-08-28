package ir.sobhan.restapi.controller.individuals;

import ir.sobhan.restapi.controller.exceptions.ApiRequestException;
import ir.sobhan.restapi.model.individual.*;
import ir.sobhan.restapi.request.individuals.auth.StaffRequest;
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
                                                      @RequestBody StaffRequest staffRequest) {
        try {
            staffService.authorizeStaff(username, staffRequest);

            return ResponseEntity.ok("Authorized user to staff limits successfully");

        } catch (ApiRequestException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }
}
