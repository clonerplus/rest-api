package ir.sobhan.restapi.request.coursesection;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseSectionRequest {
    private String TermTitle;
    private String CourseTitle;
}
