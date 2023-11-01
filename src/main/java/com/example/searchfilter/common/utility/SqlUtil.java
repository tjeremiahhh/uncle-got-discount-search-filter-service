package com.example.searchfilter.common.utility;
import java.text.MessageFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Order;


public class SqlUtil {

    public static String sortBuilder(Pageable pageable) {
        List<Order> orders = pageable.getSort().toList();
        StringBuilder sortAndOffsetQuery = new StringBuilder();
        String sortQuery = IntStream.range(0, orders.size()).mapToObj(i -> {
            String order = orders.get(i).getProperty().toString() + " " + orders.get(i).getDirection().name();
            return i == 0 ? " ORDER BY " + order : order;
        }).collect(Collectors.joining(", "));

        String offsetQuery = MessageFormat.format(" OFFSET {0} ROWS FETCH NEXT {1} ROWS ONLY", String.valueOf(pageable.getOffset()), String.valueOf(pageable.getPageSize()));
        offsetQuery = StringUtils.isBlank(sortQuery) ? " ORDER BY 1" + offsetQuery : offsetQuery;
        sortAndOffsetQuery.append(sortQuery).append(offsetQuery);

        return sortAndOffsetQuery.toString();
    }

    public static String criteriaBuilder(List<String> criteriaList) {
        String criteriaQuery = IntStream.range(0, criteriaList.size()).mapToObj(i -> {
            String c = criteriaList.get(i);
            return i == 0 ? " WHERE " + c : c;
        }).collect(Collectors.joining(" AND "));

        return criteriaQuery;
    }

    public static String sortBuilder(Pageable pageable, String defaultSort) {
        List<Order> orders = pageable.getSort().toList();
        StringBuilder sortAndOffsetQuery = new StringBuilder();
        String sortQuery = IntStream.range(0, orders.size()).mapToObj(i -> {
            String order = orders.get(i).getProperty().toString() + " " + orders.get(i).getDirection().name();
            return i == 0 ? " ORDER BY " + order : order;
        }).collect(Collectors.joining(", "));

        String offsetQuery = MessageFormat.format(" LIMIT {0}, {1} ",
                String.valueOf(pageable.getOffset()), String.valueOf(pageable.getPageSize()));
        offsetQuery = StringUtils.isBlank(sortQuery) ? defaultSort + offsetQuery : offsetQuery;
        sortAndOffsetQuery.append(sortQuery).append(offsetQuery);

        return sortAndOffsetQuery.toString();
    }

    public static String sortOnlyBuilder(Pageable pageable, String defaultOrder) {

        List<Order> orders = pageable.getSort().toList();

        String sortQuery = IntStream.range(0, orders.size()).mapToObj(i -> {

            String order = orders.get(i).getProperty().toString() + " " + orders.get(i).getDirection().name();
            return i == 0 ? " ORDER BY " + order : order;
        }).collect(Collectors.joining(", "));

        sortQuery = StringUtils.isBlank(sortQuery) ? defaultOrder : sortQuery;

        return sortQuery;

    }

    public static String deriveCountQuery(String sql) {
        int end = lastIndexOfByRegex(sql, "\\s+(ORDER)\\s+(BY)");
        StringBuilder strBuilder = new StringBuilder(sql.substring(0, end > 0 ? end : sql.length()));
        return "SELECT COUNT(*) FROM (" + strBuilder.toString() + ") A";
    }

    public static int lastIndexOfByRegex(String str, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);

        int lastIndex = -1;

        while (matcher.find()) {
            lastIndex = matcher.start();
        }
        return lastIndex;
    }

}
