package com.michaelcherrera.mycount;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the MyCount Spring Boot application.
 * MyCount is an inventory tracking system that manages items, locations,
 * stores, units of measurement, and users across multiple retail or food-service locations.
 */
@SpringBootApplication
public class MyCountApplication {

    /**
     * Launches the MyCount application by starting the Spring Boot embedded server
     * and initializing all configured beans, data sources, and auto-configuration.
     *
     * @param args command-line arguments passed to the application at startup
     */
    public static void main(String[] args) {

        SpringApplication.run(MyCountApplication.class, args);
    }

}
