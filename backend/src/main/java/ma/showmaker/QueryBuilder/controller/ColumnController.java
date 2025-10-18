package ma.showmaker.QueryBuilder.controller;

import jakarta.persistence.EntityNotFoundException;
import ma.showmaker.QueryBuilder.model.ColumnSchema;
import ma.showmaker.QueryBuilder.model.DatabaseSchema;
import ma.showmaker.QueryBuilder.model.TableSchema;
import ma.showmaker.QueryBuilder.service.SchemaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
public class ColumnController {

    private final SchemaService schemaService;
    public ColumnController(SchemaService schemaService) {
        this.schemaService = schemaService;
    }

    @GetMapping("/columns")
    public List<String> getTableColumns(@RequestParam String tableName){
        DatabaseSchema databaseSchema = this.schemaService.getDatabaseSchema();
        TableSchema table = databaseSchema
                .getTables()
                .stream().
                filter(tableSchema -> Objects.equals(tableSchema.getName(), tableName))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("table not found"));
        return table
                .getColumns()
                .stream()
                .map(ColumnSchema::getName)
                .toList();
    }
}
