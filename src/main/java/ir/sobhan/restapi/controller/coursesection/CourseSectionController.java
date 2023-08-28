package ir.sobhan.restapi.controller.coursesection;

import ir.sobhan.restapi.controller.exceptions.ApiRequestException;
import ir.sobhan.restapi.model.coursesection.CourseSection;
import ir.sobhan.restapi.request.coursesection.CourseSectionRequest;
import ir.sobhan.restapi.request.coursesection.SetStudentsScoreRequest;
import ir.sobhan.restapi.response.ListResponse;
import ir.sobhan.restapi.service.coursesection.CourseSectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
public class CourseSectionController {
    private final CourseSectionService courseSectionService;

    @Autowired
    public CourseSectionController(CourseSectionService courseSectionService) {
        this.courseSectionService = courseSectionService;
    }
    @GetMapping("all-course-sections/{termTitle}")
    public ResponseEntity<ListResponse<CourseSection>> showAllTerms(@PathVariable String termTitle) {

        var termCourseSections = courseSectionService.getAllTerms(termTitle);

        return termCourseSections.map(courseSections -> ResponseEntity.ok(ListResponse.<CourseSection>builder()
                .responseList(courseSections)
                .build())).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
    @GetMapping("all-course-sections-and-students/{termTitle}")
    public ResponseEntity<ListResponse<CourseSection>> showTermByTitle(@PathVariable String termTitle) {

        var termCourseSections = courseSectionService.getAllTermsAndStudentsCount(termTitle);

        return termCourseSections.map(courseSections -> ResponseEntity.ok(ListResponse.<CourseSection>builder()
                .responseList(courseSections)
                .build())).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @PostMapping("/create/course-section")
    public ResponseEntity<String> createCourseSection(
            @RequestBody CourseSectionRequest courseSectionRequest, Authentication authentication) {
        try {
            courseSectionService.buildCourseSection(courseSectionRequest, authentication.getName());
            return ResponseEntity.ok("courseSection created successfully!");

        } catch (ApiRequestException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @PostMapping("set-scores/{courseSectionId}")
    public ResponseEntity<String> setStudentsScores(@PathVariable long courseSectionId,
            @RequestBody SetStudentsScoreRequest setStudentsScoreRequest) {
        try {
            courseSectionService.setStudentsScore(
                    courseSectionId, setStudentsScoreRequest);

            return ResponseEntity.ok("updated scores successfully!");

        } catch (ApiRequestException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @PutMapping("/update/course-section")
    public ResponseEntity<String> updateCourseSection(
            @RequestBody CourseSectionRequest courseSectionRequest, Authentication authentication) {
        try {
            courseSectionService.buildCourseSection(courseSectionRequest, authentication.getName());
            return ResponseEntity.ok("courseSection created successfully!");

        } catch (ApiRequestException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @DeleteMapping("/delete/course-section")
    public ResponseEntity<String> deleteTerm(@RequestBody CourseSectionRequest courseSectionRequest,
                                             Authentication authentication) {
        try {
            courseSectionService.deleteCourseSection(courseSectionRequest, authentication);
            return ResponseEntity.ok("successfully deleted term!");

        } catch (ApiRequestException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }
}
