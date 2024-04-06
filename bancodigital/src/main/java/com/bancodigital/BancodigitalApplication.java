package com.bancodigital;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableJpaRepositories("com.bancodigital.repository")
@EntityScan("com.bancodigital.entity")
public class BancodigitalApplication {

    public static void main(String[] args) {
        SpringApplication.run(BancodigitalApplication.class, args);
    }

}