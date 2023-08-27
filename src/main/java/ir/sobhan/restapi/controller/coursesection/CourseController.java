package ir.sobhan.restapi.controller.coursesection;

import ir.sobhan.restapi.model.coursesection.Course;
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
    public ResponseEntity<String> createCourse(@RequestBody Course course) {
        return ResponseEntity.ok(courseService.buildCourse(course));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @PutMapping("/update/course")
    public ResponseEntity<String> updateCourse(@RequestBody Course course) {
        return ResponseEntity.ok(courseService.buildCourse(course));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @DeleteMapping("/delete/course")
    public ResponseEntity<String> deleteCourse(@RequestParam String title) {

        if (title == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid course title!");

        Optional<Course> course = courseService.getCourseByTitle(title);

        if (course.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("course not found!");

        return ResponseEntity.ok(courseService.deleteTerm(title));
    }
}
