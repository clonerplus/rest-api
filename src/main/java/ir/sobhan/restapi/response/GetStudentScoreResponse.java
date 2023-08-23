package ir.sobhan.restapi.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetStudentScoreResponse {

    @JsonProperty
    private Map<String, Double> scores;
    @JsonProperty("average")
    private Double average;
}
