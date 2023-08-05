package com.example.wanted;

import com.example.wanted.config.jwt.JwtConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(JwtConfig.class)
@SpringBootApplication
public class WantedApplication {

	public static void main(String[] args) {
		SpringApplication.run(WantedApplication.class, args);
	}

}
