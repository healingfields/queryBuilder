package ma.showmaker.QueryBuilder.model;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DatabaseSchema {
    private String name;
    private List<TableSchema> tables;
}
