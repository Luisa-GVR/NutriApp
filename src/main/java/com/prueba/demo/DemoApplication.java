package com.prueba.demo;

import com.prueba.demo.principal.LoginFrame;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication extends Application {

	private ConfigurableApplicationContext context;

	@Override
	public void init() {
		context = new SpringApplicationBuilder(DemoApplication.class)
				.run();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoginFrame.fxml"));
		loader.setControllerFactory(context::getBean); // Permite que Spring maneje los controladores
		Scene scene = new Scene(loader.load());

		primaryStage.setTitle("Login");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	@Override
	public void stop() {
		context.close(); // Cierra el contexto de Spring al cerrar la app
	}

	public static void main(String[] args) {
		launch(args); // Inicia JavaFX
	}
}
