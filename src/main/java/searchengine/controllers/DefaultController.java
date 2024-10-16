package searchengine.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@RestController
@RequestMapping("/api")
public class DefaultController {

    private final AtomicBoolean isIndexingInProgress = new AtomicBoolean(false);

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/startIndexing")
    public ResponseEntity<Map<String, Object>> startIndexing() {
        if (isIndexingInProgress.get()) {
            return ResponseEntity.badRequest().body(createErrorResponse("Индексация уже запущена"));
        }

        isIndexingInProgress.set(true);
        try {
            performFullIndexing();
            return ResponseEntity.ok(createSuccessResponse());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(createErrorResponse("Ошибка при запуске индексации: " + e.getMessage()));
        } finally {
            isIndexingInProgress.set(false);
        }
    }

    private void performFullIndexing() throws InterruptedException {
        System.out.println("Начало индексации...");
        Thread.sleep(5000);
        System.out.println("Индексация завершена.");
    }

    private Map<String, Object> createSuccessResponse() {
        Map<String, Object> response = new HashMap<>();
        response.put("result", true);
        return response;
    }

    private Map<String, Object> createErrorResponse(String errorMessage) {
        Map<String, Object> response = new HashMap<>();
        response.put("result", false);
        response.put("error", errorMessage);
        return response;
    }
}