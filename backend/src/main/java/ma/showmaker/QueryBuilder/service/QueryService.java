package ma.showmaker.QueryBuilder.service;

import jakarta.persistence.EntityNotFoundException;
import ma.showmaker.QueryBuilder.dto.FilterDto;
import ma.showmaker.QueryBuilder.model.ColumnSchema;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.logging.Filter;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class QueryService {

    public StringBuilder createFilters(List<ColumnSchema> columns, List<FilterDto> filters){
        String conditions = IntStream.range(0, filters.size())
                .mapToObj( i -> {
                FilterDto filterDto = filters.get(i);
                    String format = String.format("%s %s %s",
                            filterDto.getColumn(),
                            filterDto.getOperator(),
                            parseFilterValue(filterDto, columns));
                    if( i < filters.size() - 1 ){
                        String logicalOperator = Objects.nonNull(filterDto.getLogicalOperator()) ? filterDto.getLogicalOperator() : "AND";
                        format += " " + logicalOperator + " ";
                    }
                    return format;
                })
                .collect(Collectors.joining());
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
