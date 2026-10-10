package com.tejaswini;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * FireSmartTV Application
 * 
 * An AI-powered Fire TV content recommendation service that uses Amazon Bedrock
 * to generate personalized viewing suggestions based on user preferences.
 * 
 * Entry point for the Spring Boot application.
 */
@SpringBootApplication
public class FireSmartTVApplication {

    public static void main(String[] args) {
        SpringApplication.run(FireSmartTVApplication.class, args);
    }
}
