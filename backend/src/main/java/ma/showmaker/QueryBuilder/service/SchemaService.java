package ma.showmaker.QueryBuilder.service;

import lombok.Getter;
import lombok.Setter;
import ma.showmaker.QueryBuilder.model.DatabaseSchema;
import org.springframework.stereotype.Service;

@Getter
@Setter
@Service
public class SchemaService {
    private DatabaseSchema databaseSchema;
}
