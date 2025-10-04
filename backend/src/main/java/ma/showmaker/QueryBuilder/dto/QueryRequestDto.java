package ma.showmaker.QueryBuilder.dto;

import lombok.Data;

import java.util.List;

@Data
public class QueryRequestDto {
    private String table;
    private List<String> columns;
    private List<FilterDto> filters;
}
