package com.prueba.demo.principal;

import com.prueba.demo.model.DayMeal;
import com.prueba.demo.model.Food;
import com.prueba.demo.repository.*;
import com.prueba.demo.service.APIConsumption;
import com.prueba.demo.service.AccountDataService;
import com.prueba.demo.service.dto.FoodPreferencesDTO;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import org.springframework.data.domain.Pageable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    private List<String> cachedSuggestions = null;


    @FXML
    private void initialize(){
        setupListViewWithDeleteButton(suggestionsListView);

        Platform.runLater(() -> {
            Stage stage = (Stage) suggestionsComboBox.getScene().getWindow();

                    stage.setOnCloseRequest(event -> {
                        cachedSuggestions = null;
                    });
                });

        suggestionsComboBox.setMaxHeight(400);

        //Buscar comidas

        // Evitar que Enter agregue elementos automáticamente
        // Buscar alimentos cuando el usuario presiona Enter y limpiar el texto
        suggestionsComboBox.getEditor().setOnKeyTyped(event -> {
            String query = suggestionsComboBox.getEditor().getText().trim(); // Eliminar espacios vacíos

            if (event.getCharacter().equals("\r")) { // Enter presionado
                if (!query.isEmpty()) {
                    // Asegurar que no haya cadenas vacías antes de agregar un nuevo valor
                    suggestionsComboBox.getItems().removeIf(String::isEmpty);

                    // Evitar que se agreguen duplicados
                    if (!suggestionsComboBox.getItems().contains(query)) {
                        suggestionsComboBox.getItems().add(query);
                    }

                    // Ejecutar la búsqueda
                    searchFood(query);
                }

                // Limpiar el campo de entrada en el siguiente ciclo de ejecución
                Platform.runLater(() -> suggestionsComboBox.getEditor().clear());
            }
        });


        // Listener para manejar la selección SOLO desde el dropdown
        suggestionsComboBox.setOnMouseClicked(event -> {

            if (cachedSuggestions == null) {
                cachedSuggestions = searchFood2(); // Fetch once and store
            }

            suggestionsComboBox.show(); // Asegura que el dropdown se muestre al hacer clic
            suggestionsComboBox.getItems().clear();
            suggestionsComboBox.getItems().addAll(cachedSuggestions); // Agregar nuevas sugerencias
            suggestionsComboBox.show();

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
                cachedSuggestions = null;
                addFood(suggestionsListView);
                closeCurrentWindow();
                refreshParentFrame();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    private void setupListViewWithDeleteButton(ListView<String> listView) {
        listView.setCellFactory(lv -> new ListCell<String>() {
            private final Button deleteButton = new Button("X");
            private final HBox hbox = new HBox(5);
            private final Label label = new Label();

            {
                // Agregar las clases CSS
                deleteButton.getStyleClass().add("delete-button");
                label.getStyleClass().add("list-item-label");
                hbox.getStyleClass().add("hbox-container");

                deleteButton.setOnAction(event -> {
                    String item = getItem();
                    if (item != null) {
                        getListView().getItems().remove(item);
                    }
                });

                hbox.getChildren().addAll(deleteButton, label);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null || item.isEmpty()) {
                    setText(null);
                    setGraphic(null);
                } else {
                    label.setText(Character.toUpperCase(item.charAt(0)) + item.substring(1));
                    setGraphic(hbox);
                }
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


    private void closeCurrentWindow() {

        Stage stage = (Stage) suggestionsListView.getScene().getWindow();

        stage.setOnCloseRequest(event -> {
            cachedSuggestions = null;  // Reset cached suggestions when the window is closed
            if (dashboardFrame != null) {
                Platform.runLater(() -> {
                    dashboardFrame.hideAll();
                    dashboardFrame.showDiet();
                });
            }
        });

        stage.setOnHidden(event -> {
            if (dashboardFrame != null) {
                Platform.runLater(() -> {
                    cachedSuggestions = null;
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
                case 5: // Optional
                    if (dayMeal.getOptional() == null) {
                        dayMeal.setOptional(new ArrayList<>());
                    }
                    dayMeal.getOptional().add(selectedFood);
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
        System.out.println("meow meow meow");

        Platform.runLater(() -> {
            suggestionsComboBox.getItems().clear();
            suggestionsComboBox.getItems().addAll(suggestions); // Agregar nuevas sugerencias
            suggestionsComboBox.show();

        });
    }

    private List<String> searchFood2() {
        AccountDataService accountDataService = new AccountDataService(accountDataRepository, accountAllergyFoodRepository, accountLikedFoodRepository, accountDislikedFoodRepository);

        double calories = accountDataService.calculateCalories(1L);

        double adjustedCalories = 0.0;
        Integer mealType = getRow();

        switch (mealType) {
            case 1:
                adjustedCalories = calories * 0.2;
                break;
            case 2:
                adjustedCalories = calories * 0.3;
                break;
            case 3:
                adjustedCalories = calories * 0.25;
                break;
            case 4:
                adjustedCalories = calories * 0.10;
                break;
            case 5:
                adjustedCalories = calories * 0.10;
                break;
        }

        if (mealType == 5){
            mealType=4;
        }

        double minCalories = adjustedCalories * 0.75;  // 25% less
        double maxCalories = adjustedCalories * 1.25;  // 25% more

        Pageable pageable = PageRequest.of(0, 10);  // Limits the result to 10 foods

        List<String> selectedFoods = foodRepository.findValidFoods(Collections.singletonList(mealType), minCalories, maxCalories, 1L, pageable);

        // Shuffle and pick 5 random suggestions
        Collections.shuffle(selectedFoods);
        List<String> suggestions = selectedFoods.stream()
                .limit(5)
                .collect(Collectors.toList());


        return suggestions;

    }
}
