package com.example.users_test_task.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * open API (swagger) configuration class
 */
@Configuration
@RequiredArgsConstructor
public class OpenAPIConfig {

    private final ResourceLoader resourceLoader;

    /**
     * configuration OpenAPI Bean
     *
     * @return configured OpenAPI
     */
    @Bean
    public OpenAPI myOpenAPI() {
        Contact contact = new Contact();
        contact.setEmail("halaikovitalii@ukr.net");
        contact.setName("Галайко Віталій");
        contact.setUrl("https://t.me/VitaliiGalayko");

        String taskDescription = "";

        try {
            taskDescription = getTaskDescription("task_description.txt");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        Info info = new Info()
                .title("Тестове завдання")
                .version("1.0")
                .contact(contact)
                .description(taskDescription);

        return new OpenAPI().info(info);
    }

    /**
     * get task description from the file in resources package
     *
     * @param fileName - file name
     * @return text from the file
     * @throws IOException if something wrong
     */
    private String getTaskDescription(String fileName) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:" + fileName);
        InputStream inputStream = resource.getInputStream();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
            return content.toString();
        }
    }
}
