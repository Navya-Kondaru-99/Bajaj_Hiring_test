package com.example.webhooksql;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class StartupService {

    @EventListener(ApplicationReadyEvent.class)
    public void onStartup() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

            WebhookRequest request = new WebhookRequest();
            request.setName("Kondaru Navya");
            request.setRegNo("22BEC7223");
            request.setEmail("navyakondaru591@gmail.com");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<WebhookRequest> entity = new HttpEntity<>(request, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
            System.out.println("Webhook generation response: " + response.getBody());

            // Parse response (assume JSON with webhook, accessToken, and sql fields)
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());
            String webhookUrl = root.path("webhook").asText();
            String accessToken = root.path("accessToken").asText();
            String sql = root.path("sql").asText();

            System.out.println("Webhook URL: " + webhookUrl);
            System.out.println("Access Token: " + accessToken);
            System.out.println("SQL Problem: " + sql);

            // Final SQL query for Kondaru Navya's question
            String finalQuery = "SELECT p.AMOUNT AS SALARY, " +
                "(e.FIRST_NAME || ' ' || e.LAST_NAME) AS NAME, " +
                "(YEAR(CURRENT_DATE) - YEAR(e.DOB)) AS AGE, " +
                "d.DEPARTMENT_NAME " +
                "FROM PAYMENTS p " +
                "JOIN EMPLOYEE e ON p.EMP_ID = e.EMP_ID " +
                "JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID " +
                "WHERE DAY(p.PAYMENT_TIME) <> 1 " +
                "ORDER BY p.AMOUNT DESC " +
                "LIMIT 1";

            // Prepare to submit the solution
            HttpHeaders submitHeaders = new HttpHeaders();
            submitHeaders.setContentType(MediaType.APPLICATION_JSON);
            submitHeaders.set("Authorization", accessToken);
            String submitBody = "{\"finalQuery\": \"" + finalQuery.replace("\"", "\\\"") + "\"}";
            HttpEntity<String> submitEntity = new HttpEntity<>(submitBody, submitHeaders);

            try {
                ResponseEntity<String> submitResponse = restTemplate.postForEntity(webhookUrl, submitEntity, String.class);
                System.out.println("Submission response: " + submitResponse.getBody());
            } catch (Exception ex) {
                System.out.println("Error submitting solution: " + ex.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
