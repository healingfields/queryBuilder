package ma.showmaker.QueryBuilder.service;

import ma.showmaker.QueryBuilder.model.ColumnSchema;
import ma.showmaker.QueryBuilder.model.DatabaseSchema;
import ma.showmaker.QueryBuilder.model.TableSchema;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SqlSchemaParserService {

    public DatabaseSchema parseSqlFile(String databaseName, String sqlContent) throws JSQLParserException {
        List<TableSchema> tables = new ArrayList<>();
        String[] statements = sqlContent.split(";");

        for(String statement: statements) {
            Statement stm = CCJSqlParserUtil.parse(statement);

            if (stm instanceof CreateTable createTable) {
                String tableName = createTable.getTable().getName();
                List<ColumnSchema> columns = new ArrayList<>();
                for (ColumnDefinition column : createTable.getColumnDefinitions()) {
                    ColumnSchema columnSchema = ColumnSchema.builder()
                            .name(column.getColumnName())
                            .type(column.getColDataType().getDataType())
                            .nullable(false)
                            .build();
                    columns.add(columnSchema);
                }
                tables.add(new TableSchema(tableName, columns));
            }
        }
        return new DatabaseSchema(databaseName, tables);
    }
}
