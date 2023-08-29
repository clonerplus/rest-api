package ir.sobhan.restapi.model.output;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListResponse<T> {
    @JsonProperty
    private List<T> responseList;
}
