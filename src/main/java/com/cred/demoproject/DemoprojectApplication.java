package com.cred.demoproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class DemoprojectApplication {

  public static void main(String[] args) {
    SpringApplication.run(DemoprojectApplication.class, args);
  }
}
