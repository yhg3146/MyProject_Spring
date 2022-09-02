package com.example.schoolmanagement.comp.paperUser.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan("com.example.schoolmanagement.comp.paperUser.user")
@EnableJpaRepositories(basePackages = {
        "com.example.schoolmanagement.comp.paperUser.user.repository"
})
@EntityScan(basePackages = "com.example.schoolmanagement.comp.paperUser.user.domain")
public class UserConfiguration {

}
