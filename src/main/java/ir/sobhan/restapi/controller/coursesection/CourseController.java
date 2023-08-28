package ir.sobhan.restapi.controller.coursesection;

import ir.sobhan.restapi.controller.exceptions.ApiRequestException;
import ir.sobhan.restapi.model.coursesection.Course;
import ir.sobhan.restapi.request.coursesection.CourseRequest;
import ir.sobhan.restapi.response.ListResponse;
import ir.sobhan.restapi.service.coursesection.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class CourseController {

    private final CourseService courseService;
    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("all-courses")
    public ResponseEntity<ListResponse<Course>> showAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }
    @GetMapping("all-courses/{title}")
    public ResponseEntity<Optional<Course>> showTermByTitle(@PathVariable String title) {
        return ResponseEntity.ok(courseService.getCourseByTitle(title));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @PostMapping("/create/course")
    public ResponseEntity<String> createCourse(@RequestBody CourseRequest courseRequest) {
        try {
            courseService.buildCourse(courseRequest);

            return ResponseEntity.ok("course created successfully!");

        } catch (ApiRequestException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @PutMapping("/update/course")
    public ResponseEntity<String> updateCourse(@RequestBody CourseRequest courseRequest) {
        try {
            courseService.buildCourse(courseRequest);

            return ResponseEntity.ok("course updated successfully!");

        } catch (ApiRequestException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @DeleteMapping("/delete/course")
    public ResponseEntity<String> deleteCourse(@RequestParam String title) {

        if (title == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid course title!");

        Optional<Course> course = courseService.getCourseByTitle(title);

        if (course.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("course not found!");

        courseService.deleteTerm(title);
        return ResponseEntity.ok("successfully deleted course!");
    }
}
