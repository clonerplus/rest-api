package ir.sobhan.restapi.model.entity.coursesection;

import jakarta.persistence.*;
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
@Table(indexes = {
        @Index(name = "TermId", columnList = "id"),
        @Index(name = "TermTitle", columnList = "title")
})
public class Term {
    @Id
    @GeneratedValue
    private long id;
    @Column(name = "title")
    private String title;
    private boolean open;
    @OneToMany(mappedBy = "term")
    private List<CourseSection> courseSection;
}
