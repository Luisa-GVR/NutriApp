package com.prueba.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "com.prueba.demo")
@SpringBootApplication
public class DemoApplication extends Application {

	private ConfigurableApplicationContext context;

	@Override
	public void init() throws Exception {
		context = new SpringApplicationBuilder(DemoApplication.class).run();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoginFrame.fxml"));
		loader.setControllerFactory(context::getBean);
		Scene scene = new Scene(loader.load());
		primaryStage.setTitle("Login");
		primaryStage.setScene(scene);
		primaryStage.show();



	}

	@Override
	public void stop() throws Exception { // Add throws Exception for consistency
		context.close();
	}

	public static void main(String[] args) {
		launch(args);
	}
}