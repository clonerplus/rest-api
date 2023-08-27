package ir.sobhan.restapi.controller.coursesection;

import ir.sobhan.restapi.model.coursesection.Term;
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
    public ResponseEntity<String> createTerm(@RequestBody Term term) {
        return ResponseEntity.ok(termService.buildTerm(term));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @PutMapping("/update/term")
    public ResponseEntity<String> updateTerm(@RequestBody Term term) {
        return ResponseEntity.ok(termService.buildTerm(term));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @DeleteMapping("/delete/term")
    public ResponseEntity<String> deleteTerm(@RequestParam String title) {

        if (title == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid term title!");

        Optional<Term> term = termService.getTermByTitle(title);

        if (term.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("term not found!");

        return ResponseEntity.ok(termService.deleteTerm(title));
    }
}
