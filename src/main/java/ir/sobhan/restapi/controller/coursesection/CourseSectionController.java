package ir.sobhan.restapi.controller.coursesection;

import ir.sobhan.restapi.dao.CourseSectionRepository;
import ir.sobhan.restapi.model.coursesection.CourseSection;
import ir.sobhan.restapi.request.CourseSectionRequest;
import ir.sobhan.restapi.request.SetStudentsScoreRequest;
import ir.sobhan.restapi.response.ListResponse;
import ir.sobhan.restapi.service.coursesection.CourseSectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class CourseSectionController {

    private final CourseSectionService courseSectionService;

    @Autowired
    public CourseSectionController(CourseSectionService courseSectionService) {
        this.courseSectionService = courseSectionService;
    }

    @GetMapping("all-course-sections/{termTitle}")
    public ResponseEntity<ListResponse<CourseSection>> showAllTerms(@PathVariable String termTitle) {

        return courseSectionService.getAllTerms(termTitle);
    }
    @GetMapping("all-course-sections-and-students/{termTitle}")
    public ResponseEntity<ListResponse<CourseSection>> showTermByTitle(@PathVariable String termTitle) {

        return courseSectionService.getAllTermsAndStudentsCount(termTitle);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @PostMapping("/create/course-section")
    public ResponseEntity<String> createCourseSection(
            @RequestBody CourseSectionRequest courseSectionRequest,
            Authentication authentication) {

        return courseSectionService.buildCourseSection(courseSectionRequest, authentication.getName());
    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @PostMapping("set-scores/{courseSectionId}")
    public ResponseEntity<String> setStudentsScores(
            @PathVariable long courseSectionId,
            @RequestBody SetStudentsScoreRequest setStudentsScoreRequest) {

        return courseSectionService.setStudentsScore(courseSectionId, setStudentsScoreRequest);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @PutMapping("/update/course-section")
    public ResponseEntity<String> updateCourseSection(
            @RequestBody CourseSectionRequest courseSectionRequest,
            Authentication authentication) {

        return courseSectionService.buildCourseSection(courseSectionRequest, authentication.getName());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @DeleteMapping("/delete/course-section")
    public ResponseEntity<String> deleteTerm(
            @RequestBody CourseSectionRequest courseSectionRequest,
            Authentication authentication) {


        return courseSectionService.deleteCourseSection(courseSectionRequest);
    }
}
