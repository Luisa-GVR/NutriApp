package com.prueba.demo.principal;

import com.prueba.demo.model.AccountData;
import com.prueba.demo.model.Goal;
import com.prueba.demo.repository.AccountAllergyFoodRepository;
import com.prueba.demo.repository.AccountDataRepository;
import com.prueba.demo.repository.AccountDislikesFoodRepository;
import com.prueba.demo.repository.AccountLikesFoodRepository;
import com.prueba.demo.service.APIConsumption;
import com.prueba.demo.service.AccountDataService;
import com.prueba.demo.service.dto.FoodPreferencesDTO;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SelectYourFood {
    @FXML
    private Button selectButton;
    @FXML
    private ComboBox<String> suggestionsComboBox;
    @FXML
    private ImageView foodImageView;
    @FXML
    private ListView<String> suggestionsListView;

    private int row;
    private int col;

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    @FXML
    private void initialize(){

        //Buscar comidas

        // Evitar que Enter agregue elementos automáticamente
        suggestionsComboBox.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                event.consume(); // Evita que Enter agregue la informacioon al listview
            }
        });

        // Listener para manejar la selección SOLO desde el dropdown
        suggestionsComboBox.setOnMouseClicked(event -> {
            suggestionsComboBox.show(); // Asegura que el dropdown se muestre al hacer clic
            searchFood(); // Llamar a searchFood cuando se hace clic en el dropdown

            List<String> getRandomFoodSuggestionsForMealType1 = APIConsumption.getRandomFoodSuggestionsForMealType1();
            System.out.println("uuwuwuwuw");
            getRandomFoodSuggestionsForMealType1.forEach(System.out::println);

        });

        PauseTransition pauseTransition = new PauseTransition(Duration.millis(500));

        // Listener para mostrar las sugerencias de la API cuando el usuario escribe
        suggestionsComboBox.setOnKeyReleased(event -> {
            pauseTransition.setOnFinished(e -> {
                String query = suggestionsComboBox.getEditor().getText();
                if (!query.isEmpty()) {
                    searchFood();
                }
            });
            pauseTransition.playFromStart(); // Reiniciar el temporizador cada vez que se escribe
        });

        suggestionsComboBox.setOnAction(event -> {
            if (!suggestionsComboBox.isShowing()) { // Solo ejecuta si el usuario selecciono desde el dropdown
                return;
            }

            String selectedItem = suggestionsComboBox.getSelectionModel().getSelectedItem();
            ObservableList<String> items = suggestionsListView.getItems();

            if (selectedItem != null) {

            }
        });


        //Listener para manejar el borrado de alergias
        suggestionsListView.setOnMouseClicked(event -> {
            String selectedItem = suggestionsListView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                suggestionsListView.getItems().remove(selectedItem);

            }
        });
    }

    public FoodPreferencesDTO getFoodPreferences(Long accountId) {
        AccountDataService accountDataService = new AccountDataService(accountDataRepository, accountAllergyFoodRepository, accountLikedFoodRepository, accountDislikedFoodRepository);

        return accountDataService.getFoodPreferences(accountId);
    }
    @Autowired
    private AccountDataRepository accountDataRepository;
    @Autowired
    private AccountAllergyFoodRepository accountAllergyFoodRepository;
    @Autowired
    private AccountLikesFoodRepository accountLikedFoodRepository;
    @Autowired
    private AccountDislikesFoodRepository accountDislikedFoodRepository;

    private final APIConsumption APIConsumption = new APIConsumption();
    private void searchFood() {
        AccountDataService accountDataService = new AccountDataService(accountDataRepository, accountAllergyFoodRepository, accountLikedFoodRepository, accountDislikedFoodRepository);

        double calories = accountDataService.calculateCalories(1L);
        FoodPreferencesDTO foodPreferencesDTO = getFoodPreferences(1L);

        double adjustedCalories = 0.0;
        System.out.println("con get row: " + getRow());

        switch (getRow()) {
            case 1:
                adjustedCalories = calories * 0.2;
                break;
            case 2:
                adjustedCalories = calories * 0.3;
                break;
            case 3:
                adjustedCalories = calories * 0.30;
                break;
            case 4:
                adjustedCalories = calories * 0.15;
                break;
        }

        //System.out.println("Query: " + query);
        System.out.println("Calorías: " + adjustedCalories);

        if (foodPreferencesDTO != null) {
            //System.out.println("Alergias: " + foodPreferencesDTO.getAllergies());
            //System.out.println("Disliked Foods: " + foodPreferencesDTO.getDislikedFoods());
            //System.out.println("Liked Foods: " + foodPreferencesDTO.getLikedFoods());
        } else {
            System.out.println("FoodPreferencesDTO es null");
        }

        List<String> suggestions = APIConsumption.getFoodSuggestionsRecommended(foodPreferencesDTO, adjustedCalories); //API busqueda

        System.out.println("suggestions: ");
        suggestions.forEach(System.out::println);


        Platform.runLater(() -> {
            suggestionsComboBox.getItems().clear();
            suggestionsComboBox.getItems().addAll(suggestions); // Agregar nuevas sugerencias
            suggestionsComboBox.show();
        });
    }



}
