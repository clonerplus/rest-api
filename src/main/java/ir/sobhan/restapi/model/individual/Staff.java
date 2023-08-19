package ir.sobhan.restapi.model.individual;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Staff {
    @Id
    @GeneratedValue
    private Long id;
    private String personnelId;
    @OneToOne(fetch = FetchType.LAZY)
    private CustomUser customUser;

}
