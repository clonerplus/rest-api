package ir.sobhan.restapi.request.coursesection;

import ir.sobhan.restapi.model.coursesection.CourseSection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TermRequest {
    private String title;
    private boolean open;
    private List<CourseSection> courseSection;
}
