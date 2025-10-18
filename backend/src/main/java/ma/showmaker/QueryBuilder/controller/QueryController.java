package ma.showmaker.QueryBuilder.controller;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import ma.showmaker.QueryBuilder.dto.FilterDto;
import ma.showmaker.QueryBuilder.dto.QueryRequestDto;
import ma.showmaker.QueryBuilder.dto.QueryResponseDto;
import ma.showmaker.QueryBuilder.service.SchemaService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
public class QueryController {

    private final SchemaService schemaService;

    public QueryController(SchemaService schemaService) {
        this.schemaService = schemaService;
    }

    @PostMapping("/query")
    public QueryResponseDto<Object> executeQuery(@RequestBody QueryRequestDto queryRequestDto){
        String table = queryRequestDto.getTable();

        String selectedColumns = (queryRequestDto.getColumns() == null || queryRequestDto.getColumns().isEmpty())
                ? "*"
                : String.join(", ", queryRequestDto.getColumns());
        // [age, name] age, name

        StringBuilder statement = new StringBuilder("select " + selectedColumns + " from " + table);

        //where name <>=like 'reda'
        //adding filters
        if(queryRequestDto.getFilters() != null || !queryRequestDto.getFilters().isEmpty()){
            statement.append(" where");
            for(int i = 0; i < queryRequestDto.getFilters().size(); i++){
                FilterDto filter = queryRequestDto.getFilters().get(i);

                statement.append(" ")
                        .append(filter.getColumn())
                        .append(" ")
                        .append(filter.getOperator())
                        .append(" ");
                //this needs to be handled in a better way
                if (filter.getColumn().equalsIgnoreCase("amount") ||
                        filter.getColumn().equalsIgnoreCase("age") || filter.getColumn().equalsIgnoreCase("id")) {
                    statement.append(filter.getValue()); // numeric
                } else {
                    statement.append("'").append(filter.getValue()).append("'"); // string
                }
                if(i < queryRequestDto.getFilters().size() - 1){
                    statement.append(" AND");
                }
            }
        }
        return QueryResponseDto.builder()
                .query(statement.toString())
                .build();
    }
}
