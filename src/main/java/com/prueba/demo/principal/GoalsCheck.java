package com.prueba.demo.principal;

import com.prueba.demo.service.APIConsumption;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.List;

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

    private void sendReport(Date date) {


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

    private void closeCurrentWindow() {
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }


}
