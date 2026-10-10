package com.tejaswini.controller;

import com.tejaswini.service.BedrockService;
import com.tejaswini.util.HtmlConstants;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * REST controller for Fire TV content recommendations.
 * Handles HTTP requests for the recommendation UI and API endpoints.
 */
@RestController
public class RecommendationController {

    private final BedrockService bedrockService;

    public RecommendationController(BedrockService bedrockService) {
        this.bedrockService = bedrockService;
    }

    /**
     * Serves the home page with the recommendation form.
     */
    @GetMapping("/")
    public String index() {
        return HtmlConstants.HOME_PAGE;
    }

    /**
     * Handles recommendation requests.
     * Accepts user preferences and returns AI-generated recommendations.
     */
    @PostMapping("/recommend")
    public ResponseEntity<Map<String, Object>> recommend(@RequestParam Map<String, String> formData) {
        String mood = formData.getOrDefault("mood", "funny");
        String audience = formData.getOrDefault("audience", "solo");
        String time = formData.getOrDefault("time", "evening");
        String genre = formData.getOrDefault("genre", "comedy");
        String extra = formData.getOrDefault("extra", "");

        String result = bedrockService.getRecommendations(mood, audience, time, genre, extra);

        Map<String, Object> response = new HashMap<>();
        if (result != null && (result.contains("AWS") || result.contains("Error") || result.contains("error"))) {
            response.put("success", false);
            response.put("error", result);
            return ResponseEntity.ok(response);
        }

        response.put("success", true);
        response.put("result", result);
        return ResponseEntity.ok(response);
    }
}
