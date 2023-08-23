package ir.sobhan.restapi.model.coursesection;
import ir.sobhan.restapi.model.individual.Instructor;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseSection {
    @Id
    @GeneratedValue
    private long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Instructor instructor;
    @ManyToOne
    private Term term;
    @ManyToOne
    private Course course;
    @OneToMany(fetch = FetchType.EAGER)
    private List<CourseSectionRegistration> courseSectionRegistration = new ArrayList<>();
}
