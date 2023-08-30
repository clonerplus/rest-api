package ir.sobhan.restapi.model.entity.coursesection;

import ir.sobhan.restapi.model.entity.individual.Student;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(indexes = {
        @Index(name = "courseSectionRegistrationId", columnList = "id"),
        @Index(name = "courseSectionRegistrationStudent", columnList = "student"),
        @Index(name = "courseSectionRegistrationCourseSection", columnList = "courseSection")
})
public class CourseSectionRegistration {
    @Id
    @GeneratedValue
    private long id;
    private Double score;
    @ManyToOne
    @JoinColumn(name = "student")
    private Student student;
    @ManyToOne
    @JoinColumn(name = "courseSection")
    private CourseSection courseSection;
}
