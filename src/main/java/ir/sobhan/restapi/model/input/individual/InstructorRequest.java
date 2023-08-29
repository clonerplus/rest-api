package ir.sobhan.restapi.model.input.individual;

import ir.sobhan.restapi.model.entity.individual.Instructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InstructorRequest {
    public enum Rank {ASSISTANT, ASSOCIATE, FULL}
    private Instructor.Rank rank;
}
