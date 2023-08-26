package ir.sobhan.restapi.model.individual;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Staff {
    @Id
    @GeneratedValue
    private Long id;
    private String personnelId;
    @JsonBackReference
    @OneToOne(fetch = FetchType.LAZY)
    private CustomUser customUser;

}
