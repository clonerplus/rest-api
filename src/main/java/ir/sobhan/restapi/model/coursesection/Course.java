package ir.sobhan.restapi.model.coursesection;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Course {
    @Id
    @GeneratedValue
    private long id;
    private String title;
    private int units;
    @OneToMany(mappedBy = "course")
    private List<CourseSection> courseSection;
}
