package com.prueba.demo;

import com.prueba.demo.principal.LoginFrame;
import com.prueba.demo.principal.Principal;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.commons.logging.Log;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@EnableAutoConfiguration
@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = new SpringApplicationBuilder(DemoApplication.class)
				.headless(false)
				.run(args);

		// Get the LoginFrame bean
		LoginFrame loginFrame = context.getBean(LoginFrame.class);

		// The visibility is now handled within LoginFrame after the DB check
	}

	@Bean
	public LoginFrame loginFrame() {
		return new LoginFrame();
	}
}
