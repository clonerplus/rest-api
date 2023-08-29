package ir.sobhan.restapi.controller.coursesection;

import ir.sobhan.restapi.controller.exceptions.ApiRequestException;
import ir.sobhan.restapi.model.coursesection.Term;
import ir.sobhan.restapi.request.coursesection.TermRequest;
import ir.sobhan.restapi.response.ListResponse;
import ir.sobhan.restapi.service.coursesection.TermService;
import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@AllArgsConstructor
public class TermController {
    private final TermService termService;
    @GetMapping("all-terms")
    public ResponseEntity<ListResponse<Term>> showAllTerms() {
        return ResponseEntity.ok(termService.getAllTerms());
    }
    @GetMapping("all-terms/{title}")
    public ResponseEntity<Optional<Term>> showTermByTitle(@PathVariable String title) {
        return ResponseEntity.ok(termService.getTermByTitle(title));
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @PostMapping("/create/term")
    public ResponseEntity<String> createTerm(@RequestBody TermRequest termRequest) {
        try {
            termService.buildTerm(termRequest);

            return ResponseEntity.ok("term created successfully!");

        } catch (ApiRequestException e) {
            return e.getResponseEntity();
        }
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @PutMapping("/update/term")
    public ResponseEntity<String> updateTerm(@RequestBody TermRequest termRequest) {
        try {
            termService.buildTerm(termRequest);

            return ResponseEntity.ok("term updated successfully!");

        } catch (ApiRequestException e) {
            return e.getResponseEntity();
        }
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @DeleteMapping("/delete/term")
    public ResponseEntity<String> deleteTerm(@RequestParam String title) {
        if (title == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid term title!");

        Optional<Term> term = termService.getTermByTitle(title);

        if (term.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("term not found!");

        termService.deleteTerm(title);
        return ResponseEntity.ok("successfully deleted term!");
    }
}
