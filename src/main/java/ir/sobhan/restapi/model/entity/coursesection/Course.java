package ir.sobhan.restapi.model.entity.coursesection;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(indexes = {
        @Index(name = "CourseId", columnList = "id"),
        @Index(name = "CourseTitle", columnList = "title")
})
public class Course {
    @Id
    @GeneratedValue
    private long id;
    @Column(name = "title")
    private String title;
    private int units;
    @OneToMany(mappedBy = "course")
    private List<CourseSection> courseSection;
}
