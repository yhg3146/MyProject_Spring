package com.example.schoolmanagement.comp.paper.config;


import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan("com.example.schoolmanagement.comp.paper.paper")
@EntityScan(basePackages = {
        "com.example.schoolmanagement.comp.paper.paper.domain"
})
@EnableJpaRepositories(basePackages = {
        "com.example.schoolmanagement.comp.paper.paper.repository"
})
public class PaperConfiguration {


}
