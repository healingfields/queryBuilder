package ma.showmaker.QueryBuilder.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class QueryResponseDto<T> {
    private String query;
}
