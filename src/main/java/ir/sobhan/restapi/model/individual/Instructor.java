package ir.sobhan.restapi.model.individual;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Instructor {
    @Id
    @GeneratedValue
    private Long id;
    @JsonBackReference
    @OneToOne(fetch = FetchType.LAZY)
    private CustomUser customUser;
    private enum Rank {ASSISTANT, ASSOCIATE, FULL}
    private Rank rank;

}
