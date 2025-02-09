package com.prueba.demo.principal;

import com.prueba.demo.model.DayMeal;
import com.prueba.demo.model.Food;
import com.prueba.demo.repository.DayMealRepository;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class NutrimentalInfo {
    @FXML
    private Button closeButton;
    @FXML
    private Button backButton;
    @FXML
    private Button nextButton;
    @FXML
    private ImageView foodImageView;
    @FXML
    private TextArea caloriesTextArea;
    @FXML
    private TextArea proteinsTextArea;
    @FXML
    private TextArea fatTextArea;
    @FXML
    private TextArea carbohydratesTextArea;
    @FXML
    private TextArea portionTextArea;
    @FXML
    private Label foodNameLabel;

    public LocalDate passedDate;
    public int passedFoodType;
    private List<Food> foodsForMeal;
    private int currentFoodIndex = 0;

    @FXML
    private void initialize() {
        backButton.setVisible(false);
        nextButton.setVisible(false);

        closeButton.setOnAction(actionEvent -> {
            try {
                closeCurrentWindow();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    public void setNutritionalData(LocalDate targetDate, int foodType) {
        if (targetDate == null) {
            return; // exit early or handle it appropriately
        }

        List <DayMeal> dayMeals = getDayMealForDate(targetDate);  // Use targetDate directly

        if (dayMeals != null) {

            switch (foodType) {
                case 1:  // Breakfast
                    foodsForMeal = dayMeals.get(0).getBreakfast();
                    break;
                case 2:  // Lunch
                    foodsForMeal = dayMeals.get(0).getLunch();
                    break;
                case 3:  // Dinner
                    foodsForMeal = dayMeals.get(0).getDinner();
                    break;
                case 4:  // Snack
                    foodsForMeal = dayMeals.get(0).getSnack();
                    break;
                default:
                    System.out.println("Invalid foodType");
                    return;
            }


            if (foodsForMeal != null && !foodsForMeal.isEmpty()) {


                // Show the first food
                showFoodDetails(currentFoodIndex, foodsForMeal.get(currentFoodIndex));
                // Show the Next button if there are more than one food
                nextButton.setVisible(foodsForMeal.size() > 1);
                backButton.setVisible(false); // Initially hide the back button
            }
        }

    }

    @FXML
    private void nextFood() {
        if (currentFoodIndex < foodsForMeal.size() - 1) {
            currentFoodIndex++;  // Avanzamos al siguiente índice
            showFoodDetails(currentFoodIndex, foodsForMeal.get(currentFoodIndex));  // Mostramos los detalles de la nueva comida
            backButton.setVisible(true);  // Mostramos el botón "Back" cuando no estamos en el primer alimento
            nextButton.setVisible(currentFoodIndex < foodsForMeal.size() - 1);  // Si no estamos en el último alimento, mostramos el botón "Next"
        }
    }

    @FXML
    private void previousFood() {
        if (currentFoodIndex > 0) {
            currentFoodIndex--;  // Retrocedemos al índice anterior
            showFoodDetails(currentFoodIndex, foodsForMeal.get(currentFoodIndex));  // Mostramos los detalles de la comida anterior
            backButton.setVisible(currentFoodIndex > 0);  // Si no estamos en el primer alimento, mostramos el botón "Back"
            nextButton.setVisible(true);  // Siempre mostramos el botón "Next"
        }
    }

    private void showFoodDetails(int index, Food currentFood) {
        // Mostrar los detalles de la comida en las etiquetas correspondientes
        foodNameLabel.setText(currentFood.getFoodName());
        caloriesTextArea.setText(String.valueOf(currentFood.getCalories()));
        proteinsTextArea.setText(String.valueOf(currentFood.getProtein()));
        fatTextArea.setText(String.valueOf(currentFood.getTotalFat()));
        carbohydratesTextArea.setText(String.valueOf(currentFood.getTotalCarbohydrate()));
        portionTextArea.setText(String.valueOf(currentFood.getPortionWeight()));

        // Mostrar la imagen de la comida si está disponible
        String imagePath = currentFood.getPhoto().getThumb();
        if (imagePath != null) {
            Image foodImage = new Image(imagePath);
            foodImageView.setImage(foodImage);
        }
    }
    @Autowired
    DayMealRepository dayMealRepository;

    private List<DayMeal> getDayMealForDate(LocalDate localDate) {
        return dayMealRepository.findAllByDate(java.sql.Date.valueOf(localDate));
    }


    private void closeCurrentWindow() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();

    }


}