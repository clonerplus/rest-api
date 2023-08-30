package ir.sobhan.restapi.controller.coursesection;

import ir.sobhan.restapi.model.entity.coursesection.Course;
import ir.sobhan.restapi.model.input.coursesection.CourseRequest;
import ir.sobhan.restapi.model.output.ListResponse;
import ir.sobhan.restapi.service.coursesection.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Course> showTermByTitle(@PathVariable String title) {
        return ResponseEntity.ok(courseService.getCourseByTitle(title));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @PostMapping("/create/course")
    public ResponseEntity<String> createCourse(@RequestBody CourseRequest courseRequest) {
        courseService.buildCourse(courseRequest);
        return ResponseEntity.ok("course created successfully!");
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @PutMapping("/update/course")
    public ResponseEntity<String> updateCourse(@RequestBody CourseRequest courseRequest) {
        courseService.buildCourse(courseRequest);
        return ResponseEntity.ok("course updated successfully!");
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @DeleteMapping("/delete/course")
    public ResponseEntity<String> deleteCourse(@RequestParam String title) {
        courseService.getCourseByTitle(title);
        courseService.deleteTerm(title);
        return ResponseEntity.ok("successfully deleted course!");
    }
}
