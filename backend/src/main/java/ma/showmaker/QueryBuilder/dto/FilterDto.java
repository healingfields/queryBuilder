package ma.showmaker.QueryBuilder.dto;

import lombok.Data;

@Data
public class FilterDto {

    private String column;
    private String operator;
    private String value;
    private String logicalOperator;
}
