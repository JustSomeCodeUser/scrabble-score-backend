package com.scrabble.score.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig : WebMvcConfigurer {
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")  // Apply CORS settings to all endpoints
            .allowedOrigins("http://localhost:3000")  // Allow frontend server
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // Specify allowed methods
            .allowedHeaders("*")  // Allow all headers
            .allowCredentials(true)  // Allow credentials
    }
}
