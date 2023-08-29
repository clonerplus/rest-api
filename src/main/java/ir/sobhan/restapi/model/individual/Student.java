package ir.sobhan.restapi.model.individual;

import com.fasterxml.jackson.annotation.JsonBackReference;
import ir.sobhan.restapi.model.coursesection.CourseSectionRegistration;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class Student {
    @Id
    @GeneratedValue
    private Long id;
    public enum Degree{BS, MS, PHD}
    private String studentId;
    private Date startDate;
    private Degree degree;
    @JsonBackReference
    @OneToOne(fetch = FetchType.LAZY)
    private CustomUser customUser;
    @OneToMany(fetch = FetchType.EAGER)
    @Builder.Default
    private List<CourseSectionRegistration> courseSectionRegistration = new ArrayList<>();
}
