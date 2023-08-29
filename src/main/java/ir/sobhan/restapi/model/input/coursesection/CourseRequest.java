package ir.sobhan.restapi.model.input.coursesection;

import ir.sobhan.restapi.model.entity.coursesection.CourseSection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseRequest {
    private String title;
    private int units;
    private List<CourseSection> courseSection;
}
