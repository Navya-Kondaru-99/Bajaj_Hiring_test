
package com.example.webhooksql;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class WebhookSqlAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebhookSqlAppApplication.class, args);
    }

    @Bean
    public CommandLineRunner verifySql(JdbcTemplate jdbcTemplate) {
        return args -> {
            String sql = "SELECT e.EMP_ID, e.FIRST_NAME, e.LAST_NAME, d.DEPARTMENT_NAME, " +
                    "(SELECT COUNT(*) FROM EMPLOYEE e2 WHERE e2.DEPARTMENT = e.DEPARTMENT AND e2.DOB > e.DOB) AS YOUNGER_EMPLOYEES_COUNT " +
                    "FROM EMPLOYEE e JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID " +
                    "ORDER BY e.EMP_ID DESC";
            List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);
            System.out.println("\n--- SQL Query Output ---");
            for (Map<String, Object> row : results) {
                System.out.println(row);
            }
            System.out.println("--- End of Output ---\n");
        };
    }
}
