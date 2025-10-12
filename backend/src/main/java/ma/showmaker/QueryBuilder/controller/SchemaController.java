package ma.showmaker.QueryBuilder.controller;

import ma.showmaker.QueryBuilder.model.DatabaseSchema;
import ma.showmaker.QueryBuilder.service.SchemaService;
import ma.showmaker.QueryBuilder.service.SqlSchemaParserService;
import net.sf.jsqlparser.JSQLParserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@RestController
@RequestMapping("/schema")
public class SchemaController {

    private final SqlSchemaParserService sqlSchemaParserService;
    private final SchemaService schemaService;

    public SchemaController(SqlSchemaParserService sqlSchemaParserService, SchemaService schemaService) {
        this.sqlSchemaParserService = sqlSchemaParserService;
        this.schemaService = schemaService;
    }

    @PostMapping("/upload-sql")
    public ResponseEntity<?> uploadSchema(@RequestParam("file") MultipartFile file){
        if(file.isEmpty()){
            return ResponseEntity.badRequest().body("no file found");
        }
        try{
            String fileContent = new String(file.getBytes(), StandardCharsets.UTF_8);
            DatabaseSchema userDb = sqlSchemaParserService.parseSqlFile("user_db", fileContent);
            schemaService.setDatabaseSchema(userDb);
            return ResponseEntity.ok(userDb);
        }  catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("failed to parse the file");
        }
    }

    @GetMapping
    public ResponseEntity<DatabaseSchema> getSchema(){
        DatabaseSchema databaseSchema = schemaService.getDatabaseSchema();
        if(Objects.isNull(databaseSchema)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(databaseSchema);
    }
}
