package ir.sobhan.restapi.model.input.coursesection;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SetStudentsScoreRequest {
    @JsonProperty
    private Map<String, Double> scores;
}
