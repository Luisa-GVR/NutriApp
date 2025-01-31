package com.prueba.demo.principal;

import com.prueba.demo.model.Food;
import com.prueba.demo.service.ConsumoAPI;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class Principal {

    // Referencias a los elementos del FXML
    @FXML private TextField foodSearch;
    @FXML private Button searchButton;
    @FXML private Label foodname;
    @FXML private Label calories;
    @FXML private Label protein;
    @FXML private Label totalFat;
    @FXML private Label totalCarhbohydrate;

    // Instancia del servicio para consumir la API
    private final ConsumoAPI consumoAPI = new ConsumoAPI();

    @FXML
    public void initialize() {
        // Verificar que los elementos están correctamente inicializados
        if (searchButton != null) {
            searchButton.setOnAction(event -> buscarAlimento());
        } else {
            System.out.println("Error: El botón searchButton no está inicializado.");
        }

        if (foodSearch == null || foodname == null || calories == null || protein == null || totalFat == null || totalCarhbohydrate == null) {
            System.out.println("Error: Uno o más elementos FXML no están correctamente inicializados.");
        }
    }


    private void buscarAlimento() {
        String query = foodSearch.getText();
        if (query.isEmpty()) {
            foodname.setText("Nombre: (Ingrese un alimento)");
            return;
        }

        // Obtener datos desde la API
        Food alimento = consumoAPI.obtenerInformacionDeAlimentos(query);

        // Mostrar los datos en la interfaz
        if (alimento != null) {
            foodname.setText("Nombre: " + alimento.getFoodName());
            calories.setText("Calorías: " + alimento.getCalories());
            protein.setText("Proteínas: " + alimento.getProtein() + " g");
            totalFat.setText("Grasas: " + alimento.getTotalFat() + " g");
            totalCarhbohydrate.setText("Carbohidratos: " + alimento.getTotalCarbohydrate() + " g");
        } else {
            foodname.setText("Nombre: No encontrado");
            calories.setText("Calorías: -");
            protein.setText("Proteínas: -");
            totalFat.setText("Grasas: -");
            totalCarhbohydrate.setText("Carbohidratos: -");
        }
    }
}
