package ma.showmaker.QueryBuilder.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class QueryResponseDto<T> {
    private List<T> rows;
    private String query;
}
