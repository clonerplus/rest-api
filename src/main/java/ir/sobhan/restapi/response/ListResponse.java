package ir.sobhan.restapi.response;

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
