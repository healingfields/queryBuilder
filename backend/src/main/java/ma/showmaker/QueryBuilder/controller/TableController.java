package ma.showmaker.QueryBuilder.controller;

import ma.showmaker.QueryBuilder.model.DatabaseSchema;
import ma.showmaker.QueryBuilder.model.TableSchema;
import ma.showmaker.QueryBuilder.service.SchemaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TableController {

    private final SchemaService schemaService;

    public TableController(SchemaService schemaService) {
        this.schemaService = schemaService;
    }

    @GetMapping("/tables")
    public List<String> getTables(){
        DatabaseSchema databaseSchema = schemaService.getDatabaseSchema();
        return databaseSchema.getTables().stream().map(TableSchema::getName).toList();
    }
}
