package ma.showmaker.QueryBuilder.model;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TableSchema {
    private String name;
    private List<ColumnSchema> columns;
}
