package com.prueba.demo.principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import javafx.fxml.FXMLLoader;
import java.io.IOException;


//PARA LA VERSION 2, CADA QUE SE ABRA UNA SCENE DEL FXML, PARA AHORRAR LINEAS DE CODIGO
@Component
public class SpringFXMLoader {

    private final ApplicationContext applicationContext;

    @Autowired
    public SpringFXMLoader(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public FXMLLoader load(String location) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(location));
        loader.setControllerFactory(applicationContext::getBean); // Crucial for Spring injection
        loader.load();
        return loader;
    }
}