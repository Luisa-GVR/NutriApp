package com.prueba.demo.principal;

import com.prueba.demo.model.*;
import com.prueba.demo.repository.*;
import com.prueba.demo.service.APIConsumption;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Component
public class SetYourPreferencesDiet {
    @FXML
    private Label favsFoodsErrorLabel;
    @FXML
    private Label noFavsFoodsErrorLabel;
    @FXML
    private ListView<String> favsFoodsListView;
    @FXML
    private ListView<String> noFavsFoodsListView;
    @FXML
    private ComboBox<String> favsFoodsComboBox;
    @FXML
    private ComboBox<String> noFavsFoodsComboBox;
    @FXML
    private Button saveButton;

    @FXML
    private void initialize(){

        setupListViewWithDeleteButton(favsFoodsListView);
        setupListViewWithDeleteButton(noFavsFoodsListView);

        favsFoodsErrorLabel.setVisible(false);
        noFavsFoodsErrorLabel.setVisible(false);

        favsFoodsComboBox.setItems(FXCollections.observableArrayList("Ninguna"));
        noFavsFoodsComboBox.setItems(FXCollections.observableArrayList("Ninguna"));

        configureFoodComboBox(favsFoodsComboBox, favsFoodsListView, favsFoodsErrorLabel);
        configureFoodListView(favsFoodsListView);

        configureFoodComboBox(noFavsFoodsComboBox, noFavsFoodsListView, noFavsFoodsErrorLabel);
        configureFoodListView(noFavsFoodsListView);

        saveButton.setOnAction(actionEvent -> {
            try {
                if (validateFields()) {
                    completeProfile();
                    closeCurrentWindow();
                }
            } catch (Exception e) {
                e.printStackTrace();
                //showAlert("Error", "No se pudo completar el perfil.");
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
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    label.setText(Character.toUpperCase(item.charAt(0)) + item.substring(1));
                    setGraphic(hbox);
                }
            }
        });
    }


    @Autowired
    private AccountDataRepository accountDataRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private FoodRepository foodRepository;
    @Autowired
    private AccountLikesFoodRepository accountLikesFoodRepository;
    @Autowired
    private AccountLikesRepository accountLikesRepository;

    @Autowired
    private AccountDislikesRepository accountDislikesRepository;
    @Autowired
    private AccountDislikesFoodRepository accountDislikesFoodRepository;


    private void completeProfile() {
        AccountData accountData = accountDataRepository.findById(2L).orElse(new AccountData());


        // Obtener la lista de alimentos que gustan
        ObservableList<String> favFoods = favsFoodsListView.getItems();
        List<Food> likeFoodList = new ArrayList<>();

        // Obtener la lista de alimentos que no gustan
        ObservableList<String> dislikeFods = noFavsFoodsListView.getItems();
        List<Food> dislikeFoodList = new ArrayList<>();

        boolean bothNull = false;


        // Procesar alimentos que gustan
        if (favFoods.isEmpty() || (favFoods.size() == 1 && favFoods.get(0).equals("Ninguna"))) {

            bothNull = true;
        } else {

            AccountLikes accountLikes = new AccountLikes();
            accountLikes.setAccountData(accountData);
            accountLikesRepository.save(accountLikes);

            for (String allergy : favFoods) {
                Food food = apiConsumption.getFoodInfo(allergy);

                if (food != null) {
                    Food existingFood = foodRepository.findFirstByFoodName(food.getFoodName());
                    if (existingFood != null) {
                        food = existingFood;  // Usar el alimento encontrado
                    } else {
                        foodRepository.save(food); // Guardar nuevo alimento
                    }

                    likeFoodList.add(food);

                    AccountLikesFood accountLikesFood = new AccountLikesFood();
                    accountLikesFood.setFood(food);
                    accountLikesFood.setAccountLikes(accountLikes);
                    accountLikesFoodRepository.save(accountLikesFood);
                }
            }

        }


        String filePath = "preferencesState.properties";
        Properties properties = new Properties();
        File propertiesFile = new File(filePath);

        // Verificar si el archivo existe
        if (propertiesFile.exists()) {
            try (FileInputStream in = new FileInputStream(propertiesFile)) {
                properties.load(in);
                String preferenceValue = properties.getProperty("preferencesCompleted");

                // Verificar el valor de preferencesCompleted
                if (!"true".equals(preferenceValue)) {
                    properties.setProperty("preferencesCompleted", "true");
                    try (FileOutputStream out = new FileOutputStream(propertiesFile)) {
                        properties.store(out, null);
                    }
                } else {
                    System.out.println("El archivo ya contiene preferencesCompleted=true");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Crear el archivo con preferencesCompleted=true
            properties.setProperty("preferencesCompleted", "true");
            try (FileOutputStream out = new FileOutputStream(propertiesFile)) {
                properties.store(out, null);
                System.out.println("Archivo creado con preferencesCompleted=true");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Procesar alimentos que no gustan
        if (dislikeFods.size() == 1 && dislikeFods.get(0).equals("Ninguna")) {
            if (bothNull) {
                closeCurrentWindow();
                dashboardFrame.hidePreferencesUI();
                dashboardFrame.refreshContent();
            }
        } else {
            AccountDislikes accountDislikes = new AccountDislikes();
            accountDislikes.setAccountData(accountData);
            accountDislikesRepository.save(accountDislikes);

            for (String dislike : dislikeFods) {
                Food food = apiConsumption.getFoodInfo(dislike);

                if (food != null) {
                    Food existingFood = foodRepository.findFirstByFoodName(food.getFoodName());
                    if (existingFood != null) {
                        food = existingFood;  // Usar el alimento encontrado
                    } else {
                        foodRepository.save(food); // Guardar nuevo alimento
                    }

                    dislikeFoodList.add(food);

                    AccountDislikesFood accountDislikesFood = new AccountDislikesFood();
                    accountDislikesFood.setFood(food);
                    accountDislikesFood.setAccountDislikes(accountDislikes);
                    accountDislikesFoodRepository.save(accountDislikesFood);
                }
            }

        }

    }

    private DashboardFrame dashboardFrame;
    public SetYourPreferencesDiet(DashboardFrame dashboardFrame) {
        this.dashboardFrame = dashboardFrame;
    }

    private boolean validateFields() {
        boolean validInputs = true;

        if (favsFoodsListView.getItems().isEmpty()){
            favsFoodsErrorLabel.setText("Por favor, intrduce un valor válido.");
            favsFoodsErrorLabel.setVisible(true);
            validInputs = false;
        }
        if (noFavsFoodsListView.getItems().isEmpty()){
            noFavsFoodsErrorLabel.setText("Por favor, intrduce un valor válido.");
            noFavsFoodsErrorLabel.setVisible(true);
            validInputs = false;
        }

        return validInputs;

    }

    private void configureFoodComboBox(ComboBox<String> comboBox, ListView<String> listView, Label errorLabel) {
        // Asegurar que el dropdown se muestre al hacer clic
        comboBox.setOnMouseClicked(event -> comboBox.show());

        // Buscar alimentos cuando el usuario presiona Enter y limpiar el texto
        comboBox.getEditor().setOnKeyTyped(event -> {
            if (event.getCharacter().equals("\r")) { // Enter presionado
                String query = comboBox.getEditor().getText();
                if (!query.isEmpty()) {
                    searchFood(query, comboBox);
                    Platform.runLater(() -> comboBox.getEditor().clear()); // Borra el texto después de la búsqueda
                }
            }
        });

        comboBox.setOnAction(event -> {
            Platform.runLater(() -> {
                String selectedItem = comboBox.getSelectionModel().getSelectedItem();
                ObservableList<String> items = listView.getItems();
                ObservableList<String> comboItems = comboBox.getItems(); // Opciones válidas

                if (selectedItem == null || !comboItems.contains(selectedItem)) {
                    return;
                }

                if (items.contains("Ninguna")) {
                    errorLabel.setText("Elimina 'Ninguna' para agregar más elementos.");
                    errorLabel.setVisible(true);
                    return;
                }
                if (items.size() >= 5) {
                    errorLabel.setText("No se pueden agregar más de 5 elementos.");
                    errorLabel.setVisible(true);
                    return;
                }
                if (!items.isEmpty() && selectedItem.equals("Ninguna")) {
                    errorLabel.setText("'Ninguna' solo puede agregarse si la lista está vacía.");
                    errorLabel.setVisible(true);
                    return;
                }

                if (!items.contains(selectedItem)) {
                    items.add(selectedItem);
                    errorLabel.setVisible(false); // Oculta error si la selección es válida
                }

                // Limpiar la selección después de agregar el elemento
                comboBox.getSelectionModel().clearSelection();
            });
        });
    }

    // Método para agregar elementos a la lista con validaciones
    private void addItemToList(String selectedItem, ListView<String> listView, Label errorLabel) {
        ObservableList<String> items = listView.getItems();

        if (items.contains("Ninguna") || (items.size() >= 5) ||
                (!items.isEmpty() && selectedItem.equals("Ninguna"))) {
            errorLabel.setText("Verificar valores ingresados");
            errorLabel.setVisible(true);
            return;
        }

        if (!items.contains(selectedItem)) {
            items.add(selectedItem);
            System.out.println("Added item: " + selectedItem);
        }
    }

    private void configureFoodListView(ListView<String> listView) {
        listView.setOnMouseClicked(event -> {
            String selectedItem = listView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                listView.getItems().remove(selectedItem);
            }
        });

        listView.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                event.consume();

            }
        });

    }

    @Autowired
    private APIConsumption apiConsumption;

    private void searchFood(String query, ComboBox comboBox) {
        TextField editor = comboBox.getEditor();

        List<String> suggestions = apiConsumption.getFoodSuggestionsNeutral(query); //API busqueda

        Platform.runLater(() -> {

            editor.requestFocus();


            comboBox.getItems().clear();
            comboBox.getItems().addAll(suggestions); // Agregar nuevas sugerencias
            comboBox.getItems().add("Ninguna"); // Mantener "Ninguna"

            comboBox.show();

        });


    }

    private void closeCurrentWindow() {
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }
}
