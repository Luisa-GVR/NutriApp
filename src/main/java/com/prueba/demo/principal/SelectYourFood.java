package com.prueba.demo.principal;

import com.prueba.demo.model.DayMeal;
import com.prueba.demo.model.Food;
import com.prueba.demo.repository.*;
import com.prueba.demo.service.APIConsumption;
import com.prueba.demo.service.AccountDataService;
import com.prueba.demo.service.dto.FoodPreferencesDTO;
import jakarta.transaction.Transactional;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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


            //List<String> getRandomFoodSuggestionsForMealType1 = APIConsumption.getRandomFoodSuggestionsForMealType1();

        });

        PauseTransition pauseTransition = new PauseTransition(Duration.millis(500));

        // Listener para mostrar las sugerencias de la API cuando el usuario escribe
        suggestionsComboBox.setOnKeyReleased(event -> {
            pauseTransition.setOnFinished(e -> {
                String query = suggestionsComboBox.getEditor().getText();
                if (!query.isEmpty()) {
                    searchFood(query);
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

            List<Food> allFoods = foodRepository.findAll();  // Get all foods

            for (Food food : allFoods) {
                String foodName = food.getFoodName();
                String thumbnailUrl = food.getPhoto().getThumb(); // Directly access the URL

                // Check if the URL is valid and try loading the image
                if (thumbnailUrl != null && !thumbnailUrl.isEmpty()) {
                    try {
                        new Image(thumbnailUrl); // Try creating an Image to check validity
                    } catch (IllegalArgumentException e) {
                        // If invalid, log or handle accordingly
                    }
                }
            }

            if (selectedItem != null) {
                items.add(selectedItem);
                Optional<String> foodURL = foodRepository.findThumbnailURLByFoodName(selectedItem);

                if (foodURL.isPresent() && foodURL.get() != null && !foodURL.get().isEmpty()) {
                    Image foodImage = new Image(foodURL.get()); // Use the actual URL here
                    foodImageView.setImage(foodImage);
                } else {
                }
            }
        });



        //Listener para manejar el borrado de suggestions
        suggestionsListView.setOnMouseClicked(event -> {
            String selectedItem = suggestionsListView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                suggestionsListView.getItems().remove(selectedItem);

            }
        });


        selectButton.setOnAction(actionEvent -> {
            try {

                addFood(suggestionsListView);
                closeCurrentWindow();
                refreshParentFrame();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    private void refreshParentFrame() {
        if (dashboardFrame != null) {
            Platform.runLater(() -> {
                // This ensures that the parent frame gets updated in the UI thread
                dashboardFrame.refreshContent();  // Assume refreshContent() is the method that refreshes the content
            });
        }
    }


    private DashboardFrame dashboardFrame;
    public SelectYourFood(DashboardFrame dashboardFrame) {
        this.dashboardFrame = dashboardFrame;
    }


    public void setDashboardFrame(DashboardFrame dashboardFrame) {
        this.dashboardFrame = dashboardFrame;
    }

    private void closeCurrentWindow() {
        Stage stage = (Stage) suggestionsListView.getScene().getWindow();

        stage.setOnHidden(event -> {
            if (dashboardFrame != null) {
                Platform.runLater(() -> {
                    dashboardFrame.hideAll();
                    dashboardFrame.showDiet();
                });
            };
        });

        stage.close();

        }
    @Autowired
    DayMealFoodRepository dayMealFoodRepository;
    @Autowired
    FoodRepository foodRepository;
    @Autowired
    private APIConsumption apiConsumption;

    @Autowired
    DayMealRepository dayMealRepository;


    private void addFood(ListView<String> suggestionsListView) {

        LocalDate today = LocalDate.now();
        DayOfWeek currentDay = today.getDayOfWeek();
        int dayOfWeek = currentDay.getValue();

        int offset = getCol() - dayOfWeek;
        LocalDate targetDate = today.plusDays(offset);

        if (targetDate.getDayOfWeek().getValue() < getCol()) {
            targetDate = targetDate.plusWeeks(1); // Adjust to next week
        }

        DayMeal dayMeal = getDayMealForDate(targetDate); // You'll need to implement this method

        if (dayMeal == null) {
            // If DayMeal does not exist for this date, create a new one
            dayMeal = new DayMeal();
            dayMeal.setDate(java.sql.Date.valueOf(targetDate));
        }

        //targetDate es el date que le debo poner al daymeal


        for (String selectedItem : suggestionsListView.getItems()) {
            Food selectedFood = getFoodByName(selectedItem);

            if (selectedFood != null) {
                foodRepository.save(selectedFood);
            } else {
                System.out.println("Food not found: " + selectedItem);
            }

            switch (getRow()) {
                case 1: // Breakfast
                    if (dayMeal.getBreakfast() == null) {
                        dayMeal.setBreakfast(new ArrayList<>());
                    }
                    dayMeal.getBreakfast().add(selectedFood);
                    break;
                case 2: // Lunch
                    if (dayMeal.getLunch() == null) {
                        dayMeal.setLunch(new ArrayList<>());
                    }
                    dayMeal.getLunch().add(selectedFood);
                    break;
                case 3: // Dinner
                    if (dayMeal.getDinner() == null) {
                        dayMeal.setDinner(new ArrayList<>());
                    }
                    dayMeal.getDinner().add(selectedFood);
                    break;
                case 4: // Snack
                    if (dayMeal.getSnack() == null) {
                        dayMeal.setSnack(new ArrayList<>());
                    }
                    dayMeal.getSnack().add(selectedFood);
                    break;
            }
        }


        dayMealRepository.save(dayMeal);
    }

    private DayMeal getDayMealForDate(LocalDate targetDate) {
        return dayMealRepository.findByDate(java.sql.Date.valueOf(targetDate));
    }

    private Food getFoodByName(String foodName) {
        Food food = foodRepository.findFirstByFoodName(foodName);
        if (food != null) {
            foodRepository.save(food);
        }
        return food;
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



    private void searchFood(String query) {
        List<String> suggestions = apiConsumption.getFoodSuggestionsNeutral(query); //API busqueda


        Platform.runLater(() -> {
            suggestionsComboBox.getItems().clear();
            suggestionsComboBox.getItems().addAll(suggestions); // Agregar nuevas sugerencias
            suggestionsComboBox.show();

        });



    }


    private void searchFood2() {
        AccountDataService accountDataService = new AccountDataService(accountDataRepository, accountAllergyFoodRepository, accountLikedFoodRepository, accountDislikedFoodRepository);

        double calories = accountDataService.calculateCalories(1L);
        FoodPreferencesDTO foodPreferencesDTO = getFoodPreferences(1L);

        double adjustedCalories = 0.0;

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


        List<String> suggestions = apiConsumption.getFoodSuggestionsRecommended(foodPreferencesDTO, adjustedCalories); //API busqueda


        Platform.runLater(() -> {
            suggestionsComboBox.getItems().clear();
            suggestionsComboBox.getItems().addAll(suggestions); // Agregar nuevas sugerencias
            suggestionsComboBox.show();
        });
    }



}
