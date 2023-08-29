package ir.sobhan.restapi.controller.coursesection;

import ir.sobhan.restapi.model.entity.coursesection.Term;
import ir.sobhan.restapi.model.input.coursesection.TermRequest;
import ir.sobhan.restapi.model.output.ListResponse;
import ir.sobhan.restapi.service.coursesection.TermService;
import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class TermController {
    private final TermService termService;
    @GetMapping("all-terms")
    public ResponseEntity<ListResponse<Term>> showAllTerms() {
        return ResponseEntity.ok(termService.getAllTerms());
    }
    @GetMapping("all-terms/{title}")
    public ResponseEntity<Term> showTermByTitle(@PathVariable String title) {
        return ResponseEntity.ok(termService.getTermByTitle(title));
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @PostMapping("/create/term")
    public ResponseEntity<String> createTerm(@RequestBody TermRequest termRequest) {
        termService.buildTerm(termRequest);
        return ResponseEntity.ok("term created successfully!");
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @PutMapping("/update/term")
    public ResponseEntity<String> updateTerm(@RequestBody TermRequest termRequest) {
        termService.buildTerm(termRequest);
        return ResponseEntity.ok("term updated successfully!");
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @DeleteMapping("/delete/term")
    public ResponseEntity<String> deleteTerm(@RequestParam String title) {
        termService.getTermByTitle(title);
        termService.deleteTerm(title);
        return ResponseEntity.ok("successfully deleted term!");
    }
}
