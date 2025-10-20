package ma.showmaker.QueryBuilder.service;

import jakarta.persistence.EntityNotFoundException;
import ma.showmaker.QueryBuilder.dto.FilterDto;
import ma.showmaker.QueryBuilder.model.ColumnSchema;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.logging.Filter;
import java.util.stream.Collectors;

@Service
public class QueryService {

    public StringBuilder createFilters(List<ColumnSchema> columns, List<FilterDto> filters){
        String conditions = filters
                .stream()
                .map(filterDto -> String.format("%s %s %s",
                        filterDto.getColumn(),
                        filterDto.getOperator(),
                        parseFilterValue(filterDto, columns)))
                .collect(Collectors.joining(" AND "));
        return new StringBuilder(" WHERE ").append(conditions);
    }

    private String parseFilterValue(FilterDto filter, List<ColumnSchema> columns) {
        ColumnSchema columnSchema1 = columns
                .stream()
                .filter(columnSchema -> Objects.equals(filter.getColumn(), columnSchema.getName()))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("column not found"));
        return columnSchema1.getType().equalsIgnoreCase("VARCHAR (255)") ? String.format("' %s '", filter.getValue()) : filter.getValue();
    }
}
