package ir.sobhan.restapi.request;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseSectionRequest {

    private String TermTitle;
    private String CourseTitle;
}
