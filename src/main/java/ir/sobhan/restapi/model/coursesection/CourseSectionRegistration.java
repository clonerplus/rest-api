package ir.sobhan.restapi.model.coursesection;

import ir.sobhan.restapi.model.individual.Student;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseSectionRegistration {

    @Id
    @GeneratedValue
    private long id;
    private Double score;
    @ManyToOne
    private CourseSection courseSection;
    @ManyToOne
    private Student student;

}
