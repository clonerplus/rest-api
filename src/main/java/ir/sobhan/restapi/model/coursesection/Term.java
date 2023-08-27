package ir.sobhan.restapi.model.coursesection;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
