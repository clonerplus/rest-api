package ir.sobhan.restapi.model.input.coursesection;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseSectionRequest {
    private String TermTitle;
    private String CourseTitle;
}
