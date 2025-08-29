package com.example.webhooksql;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.List;
import java.util.Map;

@RestController
public class SqlDebugController {
    private final JdbcTemplate jdbcTemplate;

    public SqlDebugController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/debug/sql-output")
    public List<Map<String, Object>> getSqlOutput() {
        String sql = "SELECT e.EMP_ID, e.FIRST_NAME, e.LAST_NAME, d.DEPARTMENT_NAME, " +
                "(SELECT COUNT(*) FROM EMPLOYEE e2 WHERE e2.DEPARTMENT = e.DEPARTMENT AND e2.DOB > e.DOB) AS YOUNGER_EMPLOYEES_COUNT " +
                "FROM EMPLOYEE e JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID " +
                "ORDER BY e.EMP_ID DESC";
        return jdbcTemplate.queryForList(sql);
    }
}
