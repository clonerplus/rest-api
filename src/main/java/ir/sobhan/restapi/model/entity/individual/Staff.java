package ir.sobhan.restapi.model.entity.individual;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Table(indexes = {
        @Index(name = "staffId", columnList = "id")
})
public class Staff {
    @Id
    @GeneratedValue
    private Long id;
    private String personnelId;
    @JsonBackReference
    @OneToOne(fetch = FetchType.LAZY)
    private CustomUser customUser;
}
