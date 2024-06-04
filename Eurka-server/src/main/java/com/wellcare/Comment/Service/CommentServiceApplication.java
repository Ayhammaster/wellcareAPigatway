package com.wellcare.Comment.Service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class CommentServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommentServiceApplication.class, args);
	}

}
