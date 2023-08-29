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
    @Builder.Default
    private List<CourseSectionRegistration> courseSectionRegistration = new ArrayList<>();
}