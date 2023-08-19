package ir.sobhan.restapi.model.coursesection;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Term {
    @Id
    @GeneratedValue
    private long id;
    private String title;
    private boolean open;
    @OneToMany(mappedBy = "term")
    private List<CourseSection> courseSection;

}
