package ma.showmaker.QueryBuilder.controller;

import jakarta.persistence.EntityNotFoundException;
import ma.showmaker.QueryBuilder.dto.FilterDto;
import ma.showmaker.QueryBuilder.dto.QueryRequestDto;
import ma.showmaker.QueryBuilder.dto.QueryResponseDto;
import ma.showmaker.QueryBuilder.model.DatabaseSchema;
import ma.showmaker.QueryBuilder.model.TableSchema;
import ma.showmaker.QueryBuilder.service.QueryService;
import ma.showmaker.QueryBuilder.service.SchemaService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class QueryController {

    private final SchemaService schemaService;
    private final QueryService queryService;

    public QueryController(SchemaService schemaService, QueryService queryService) {
        this.schemaService = schemaService;
        this.queryService = queryService;
    }

    @PostMapping("/query")
    public QueryResponseDto<Object> executeQuery(@RequestBody QueryRequestDto queryRequestDto){
        DatabaseSchema databaseSchema = schemaService.getDatabaseSchema();
        TableSchema tableSchema = databaseSchema
                .getTables()
                .stream()
                .filter(ts -> Objects.equals(ts.getName(), queryRequestDto.getTable()))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("table not found"));
        String table = queryRequestDto.getTable();
        String selectedColumns = (queryRequestDto.getColumns() == null || queryRequestDto.getColumns().isEmpty())
                ? "*"
                : String.join(", ", queryRequestDto.getColumns());

        StringBuilder statement = new StringBuilder("select " + selectedColumns + " from " + table);

        StringBuilder filters = new StringBuilder();
        if(queryRequestDto.getFilters() != null || !queryRequestDto.getFilters().isEmpty()){
            filters = queryService.createFilters(tableSchema.getColumns(), queryRequestDto.getFilters());
        }
        statement.append(filters);
        return QueryResponseDto.builder()
                .query(statement.toString())
                .build();
    }
}
