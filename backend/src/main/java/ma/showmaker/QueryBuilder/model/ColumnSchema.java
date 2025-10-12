package ma.showmaker.QueryBuilder.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ColumnSchema {
    private String name;
    private String type;
    private boolean nullable;
}
