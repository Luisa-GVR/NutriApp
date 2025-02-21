package com.prueba.demo.principal;

import com.prueba.demo.model.*;
import com.prueba.demo.repository.AccountDataRepository;
import com.prueba.demo.repository.DayMealRepository;
import com.prueba.demo.repository.FoodRepository;
import com.prueba.demo.repository.ReportRepository;
import com.prueba.demo.service.APIConsumption;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class GoalsCheck {
    @FXML
    private Button saveButton;
    @FXML
    private ComboBox<String> breakfastComboBox;
    @FXML
    private ComboBox<String> lunchComboBox;
    @FXML
    private ComboBox<String> dinnerComboBox;
    @FXML
    private ComboBox<String> snackComboBox;
    @FXML
    private ComboBox<String> optionalComboBox;
    @FXML
    private ListView<String> breakfastListView;
    @FXML
    private ListView<String> lunchListView;
    @FXML
    private ListView<String> dinnerListView;
    @FXML
    private ListView<String> snackListView;
    @FXML
    private ListView<String> optionalListView;

    public void initialize(){
        setupListViewWithDeleteButton(breakfastListView);
        setupListViewWithDeleteButton(lunchListView);
        setupListViewWithDeleteButton(dinnerListView);
        setupListViewWithDeleteButton(snackListView);
        setupListViewWithDeleteButton(optionalListView);


    }
    public void setDate(Date reportDate) {

        configureFoodComboBox(breakfastComboBox, breakfastListView);
        configureFoodListView(breakfastListView);

        configureFoodComboBox(lunchComboBox, lunchListView);
        configureFoodListView(lunchListView);

        configureFoodComboBox(dinnerComboBox, dinnerListView);
        configureFoodListView(dinnerListView);

        configureFoodComboBox(snackComboBox, snackListView);
        configureFoodListView(snackListView);

        configureFoodComboBox(optionalComboBox, optionalListView);
        configureFoodListView(optionalListView);


        saveButton.setOnAction(actionEvent -> {
            try {
                if (validateFields()) {
                    sendReport(reportDate);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Autowired
    DayMealRepository dayMealRepository;
    @Autowired
    FoodRepository foodRepository;

    private DayMeal getDayMealForDate(Date targetDate) {
        return dayMealRepository.findByDate(targetDate);
    }
    private Food getFoodByName(String foodName) {
        Food food = foodRepository.findFirstByFoodName(foodName);
        if (food != null) {
            foodRepository.save(food);
        }
        return food;
    }

    private void sendReport(Date date) {

        DayMeal dayMeal = getDayMealForDate(date);

        if (dayMeal == null) {
            // If DayMeal does not exist for this date, create a new one
            dayMeal = new DayMeal();
            dayMeal.setDate(date);
        }

// Update breakfast
        if (!breakfastListView.getItems().contains("Cumplí mi meta")) {
            for (String selectedItem : breakfastListView.getItems()) {
                Food selectedFood = getFoodByName(selectedItem);
                if (dayMeal.getBreakfast() == null) {
                    dayMeal.setBreakfast(new ArrayList<>());
                }
                dayMeal.getBreakfast().add(selectedFood);
            }
        }

        // Update lunch
        if (!lunchListView.getItems().contains("Cumplí mi meta")) {
            for (String selectedItem : lunchListView.getItems()) {
                Food selectedFood = getFoodByName(selectedItem);
                if (dayMeal.getLunch() == null) {
                    dayMeal.setLunch(new ArrayList<>());
                }
                dayMeal.getLunch().add(selectedFood);
            }
        }

        // Update dinner
        if (!dinnerListView.getItems().contains("Cumplí mi meta")) {
            for (String selectedItem : dinnerListView.getItems()) {
                Food selectedFood = getFoodByName(selectedItem);
                if (dayMeal.getDinner() == null) {
                    dayMeal.setDinner(new ArrayList<>());
                }
                dayMeal.getDinner().add(selectedFood);
            }
        }

        // Update snacks
        if (!snackListView.getItems().contains("Cumplí mi meta")) {
            for (String selectedItem : snackListView.getItems()) {
                Food selectedFood = getFoodByName(selectedItem);
                if (dayMeal.getSnack() == null) {
                    dayMeal.setSnack(new ArrayList<>());
                }
                dayMeal.getSnack().add(selectedFood);
            }
        }

        // Update optional
        if (!optionalListView.getItems().contains("Cumplí mi meta")) {
            for (String selectedItem : optionalListView.getItems()) {
                Food selectedFood = getFoodByName(selectedItem);
                if (dayMeal.getOptional() == null) {
                    dayMeal.setOptional(new ArrayList<>());
                }
                dayMeal.getOptional().add(selectedFood);
            }
        }

        dayMealRepository.save(dayMeal);

        saveToReport(date);
        closeCurrentWindow();

    }

    @Autowired
    ReportRepository reportRepository;
    @Autowired
    AccountDataRepository accountDataRepository;

    private void saveToReport(Date reportDate) {

        System.out.println(reportDate);

        Report existingReport = reportRepository.findByDate(reportDate);

        if (existingReport == null) {
            Report report = new Report();
            DayMeal dayMeal = dayMealRepository.findByDate(reportDate);
            Optional<AccountData> accountData = accountDataRepository.findByAccountId(1L);
            boolean goalMet = true;

            report.setDayExcercise(null);
            report.setDayMeals(dayMeal);
            report.setAccountData(accountData.orElse(null));
            report.setGoalMet(goalMet);
            report.setDate(reportDate);

            reportRepository.save(report);


        } else {
            DayMeal dayMeal = dayMealRepository.findByDate(reportDate);
            Optional<AccountData> accountData = accountDataRepository.findByAccountId(1L);
            boolean goalMet = true;

            existingReport.setDayMeals(dayMeal);
            existingReport.setAccountData(accountData.orElse(null));
            existingReport.setGoalMet(goalMet);
            existingReport.setDate(reportDate);

            reportRepository.save(existingReport);
        }
    }

    private boolean validateFields() {
        boolean validInputs = true;

        if (breakfastListView.getItems().isEmpty()){
            validInputs = false;
        }
        if (lunchListView.getItems().isEmpty()){
            validInputs = false;
        }
        if (dinnerListView.getItems().isEmpty()){
            validInputs = false;
        }
        if (snackListView.getItems().isEmpty()){
            validInputs = false;
        }
        if (optionalListView.getItems().isEmpty()){
            validInputs = false;
        }

        return validInputs;

    }


    private void configureFoodComboBox(ComboBox<String> comboBox, ListView<String> listView) {

        ObservableList<String> goal = FXCollections.observableArrayList("Cumplí mi meta");
        comboBox.setItems(goal);

        // Evitar que Enter agregue elementos automáticamente
        comboBox.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                event.consume();
            }
        });

        // Asegurar que el dropdown se muestre al hacer clic
        comboBox.setOnMouseClicked(event -> comboBox.show());

        PauseTransition pauseTransition = new PauseTransition(Duration.millis(500));

        // Mostrar sugerencias de la API cuando el usuario escribe
        comboBox.setOnKeyReleased(event -> {
            pauseTransition.setOnFinished(e -> {
                String query = comboBox.getEditor().getText();
                if (!query.isEmpty()) {
                    searchFood(query, comboBox);
                }
            });
            pauseTransition.playFromStart();
        });

        comboBox.setOnAction(event -> {
            if (!comboBox.isShowing()) return; // Solo procesar si se selecciona desde el dropdown

            String selectedItem = comboBox.getSelectionModel().getSelectedItem();
            ObservableList<String> items = listView.getItems();

            if (selectedItem != null) {
                if (items.contains("Cumplí mi meta") || (items.size() >= 5) ||
                        (!items.isEmpty() && selectedItem.equals("Cumplí mi meta"))) {

                    return;
                }

                if (!items.contains(selectedItem)) {
                    items.add(selectedItem);
                }
            }

            Platform.runLater(() -> comboBox.getEditor().clear());
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

    private void configureFoodListView(ListView<String> listView) {


        listView.setOnMouseClicked(event -> {
            String selectedItem = listView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                listView.getItems().remove(selectedItem);
            }
        });
    }


    @Autowired
    private APIConsumption apiConsumption;

    private void searchFood(String query, ComboBox comboBox) {
        List<String> suggestions = apiConsumption.getFoodSuggestionsNeutral(query); //API busqueda

        Platform.runLater(() -> {
            comboBox.getItems().clear();
            comboBox.getItems().addAll(suggestions); // Agregar nuevas sugerencias
            comboBox.getItems().add("Cumplí mi meta"); // Mantener "Cumplí mi meta"
            comboBox.show();
        });
    }
    public interface CloseCallback {
        void onClosed();
    }

    private CloseCallback closeCallback;

    public void setCloseCallback(CloseCallback callback) {
        this.closeCallback = callback;
    }

    private void closeCurrentWindow() {
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
        if (closeCallback != null) {
            closeCallback.onClosed(); // Notify the parent
        }
    }

}
