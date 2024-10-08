package org.likelion.likelion_12th_team05;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class LikeLion12thTeam05Application {

    public static void main(String[] args) {
        SpringApplication.run(LikeLion12thTeam05Application.class, args);
    }

}
