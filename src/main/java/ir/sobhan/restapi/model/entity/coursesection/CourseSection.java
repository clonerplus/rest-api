package ir.sobhan.restapi.model.entity.coursesection;
import ir.sobhan.restapi.model.entity.individual.Instructor;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(indexes = {
        @Index(name = "courseSectionId", columnList = "id"),
        @Index(name = "courseSectionTitle", columnList = "term, course")
})
public class CourseSection {
    @Id
    @GeneratedValue
    private long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Instructor instructor;
    @ManyToOne
    @JoinColumn(name = "term")
    private Term term;
    @ManyToOne
    @JoinColumn(name = "course")
    private Course course;
    @OneToMany(fetch = FetchType.EAGER)
    @Builder.Default
    private List<CourseSectionRegistration> courseSectionRegistration = new ArrayList<>();
}
