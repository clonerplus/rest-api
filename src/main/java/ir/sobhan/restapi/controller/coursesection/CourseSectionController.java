package ir.sobhan.restapi.controller.coursesection;

import ir.sobhan.restapi.model.coursesection.CourseSection;
import ir.sobhan.restapi.request.CourseSectionRequest;
import ir.sobhan.restapi.request.SetStudentsScoreRequest;
import ir.sobhan.restapi.response.ListResponse;
import ir.sobhan.restapi.service.coursesection.CourseSectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
            @RequestBody CourseSectionRequest courseSectionRequest,
            Authentication authentication) {

        Map<String, HttpStatus> statusMap = new HashMap<>(
                Map.of("Term not found!", HttpStatus.NOT_FOUND,
                "Course not found!", HttpStatus.NOT_FOUND,
                        "courseSection created successfully!", HttpStatus.OK));

        String resultMsg = courseSectionService
                .buildCourseSection(courseSectionRequest, authentication.getName());

        return ResponseEntity.status(statusMap.get(resultMsg)).body(resultMsg);
    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @PostMapping("set-scores/{courseSectionId}")
    public ResponseEntity<String> setStudentsScores(
            @PathVariable long courseSectionId,
            @RequestBody SetStudentsScoreRequest setStudentsScoreRequest) {

        Map<String, HttpStatus> statusMap = new HashMap<>(
                Map.of("course section not found!", HttpStatus.NOT_FOUND,
                        "updated scores successfully!", HttpStatus.OK));

        String resultMsg = courseSectionService.setStudentsScore(courseSectionId, setStudentsScoreRequest);

        return ResponseEntity.status(statusMap.get(resultMsg)).body(resultMsg);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @PutMapping("/update/course-section")
    public ResponseEntity<String> updateCourseSection(
            @RequestBody CourseSectionRequest courseSectionRequest,
            Authentication authentication) {

        Map<String, HttpStatus> statusMap = new HashMap<>(
                Map.of("Term not found!", HttpStatus.NOT_FOUND,
                        "Course not found!", HttpStatus.NOT_FOUND,
                        "courseSection created successfully!", HttpStatus.OK));

        String resultMsg = courseSectionService
                .buildCourseSection(courseSectionRequest, authentication.getName());

        return ResponseEntity.status(statusMap.get(resultMsg)).body(resultMsg);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @DeleteMapping("/delete/course-section")
    public ResponseEntity<String> deleteTerm(
            @RequestBody CourseSectionRequest courseSectionRequest,
            Authentication authentication) {

        Map<String, HttpStatus> statusMap = new HashMap<>(
                Map.of("user not authorized to delete this course section", HttpStatus.BAD_REQUEST,
                        "successfully deleted term!", HttpStatus.OK));

        String resultMsg = courseSectionService.deleteCourseSection(courseSectionRequest, authentication);

        return ResponseEntity.status(statusMap.get(resultMsg)).body(resultMsg);
    }
}
