package ir.sobhan.restapi.model.individual;

import com.fasterxml.jackson.annotation.JsonBackReference;
import ir.sobhan.restapi.model.coursesection.CourseSection;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class Instructor {
    @Id
    @GeneratedValue
    private Long id;
    public enum Rank {ASSISTANT, ASSOCIATE, FULL}
    private Rank rank;
    @JsonBackReference
    @OneToOne(fetch = FetchType.LAZY)
    private CustomUser customUser;
    @ManyToOne
    private CourseSection courseSection;
}
