package com.prueba.demo.principal;

import com.prueba.demo.model.DayMeal;
import com.prueba.demo.model.Food;
import com.prueba.demo.repository.DayMealRepository;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class NutrimentalInfo {
    @FXML
    private Button closeButton;
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
    private LocalDate targetDate;
    private int foodType;

    public void setInfo(LocalDate targetDate, int foodType) {
        this.targetDate = targetDate;
        this.foodType = foodType;
    }

    @FXML
    private void initialize() {
        // Use targetDate and foodType to retrieve the corresponding meal information
        DayMeal dayMeal = getDayMealForDate(targetDate);
        if (dayMeal != null) {
            Food foodForMeal = null;
            switch (foodType) {
                case 1:  // Breakfast
                    foodForMeal = dayMeal.getBreakfast().isEmpty() ? null : dayMeal.getBreakfast().get(0);
                    break;
                case 2:  // Lunch
                    foodForMeal = dayMeal.getLunch().isEmpty() ? null : dayMeal.getLunch().get(0);
                    break;
                case 3:  // Dinner
                    foodForMeal = dayMeal.getDinner().isEmpty() ? null : dayMeal.getDinner().get(0);
                    break;
                case 4:  // Snack
                    foodForMeal = dayMeal.getSnack().isEmpty() ? null : dayMeal.getSnack().get(0);
                    break;
                default:
                    System.out.println("Invalid foodType");
                    return;
            }

            // If foodForMeal is not null, populate the UI with meal details
            if (foodForMeal != null) {
                foodNameLabel.setText(foodForMeal.getFoodName());
                caloriesTextArea.setText(String.valueOf(foodForMeal.getCalories()));
                proteinsTextArea.setText(String.valueOf(foodForMeal.getProtein()));
                //fatTextArea.setText(String.valueOf(foodForMeal.getTotalFat()));
                carbohydratesTextArea.setText(String.valueOf(foodForMeal.getTotalCarbohydrate()));
                //portionTextArea.setText(String.valueOf(foodForMeal.getPortionWeight()));

                // Set the food image
                String imagePath = foodForMeal.getPhoto().getThumb();
                if (imagePath != null && !imagePath.isEmpty()) {
                    foodImageView.setImage(new Image(imagePath));
                } else {
                    System.out.println("Invalid or missing image path: " + imagePath);
                }
            }
        }
    }

    @Autowired
    DayMealRepository dayMealRepository;    private DayMeal getDayMealForDate(LocalDate date) {
        return dayMealRepository.findByDate(java.sql.Date.valueOf(targetDate));
    }


}
