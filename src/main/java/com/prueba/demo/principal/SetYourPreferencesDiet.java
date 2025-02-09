package com.prueba.demo.principal;

import com.prueba.demo.model.*;
import com.prueba.demo.repository.*;
import com.prueba.demo.service.APIConsumption;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
                }
            } catch (Exception e) {
                e.printStackTrace();
                //showAlert("Error", "No se pudo completar el perfil.");
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
                    Optional<Food> existingFood = foodRepository.findFirstByFoodName(food.getFoodName());
                    if (existingFood.isPresent()) {
                        food = existingFood.get();
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

        // Procesar alimentos que no gustan
        if (dislikeFods.size() == 1 && dislikeFods.get(0).equals("Ninguna")) {
            if (bothNull) {
                closeCurrentWindow();
                dashboardFrame.hidePreferencesUI();
            }
        } else {
            AccountDislikes accountDislikes = new AccountDislikes();
            accountDislikes.setAccountData(accountData);
            accountDislikesRepository.save(accountDislikes);

            for (String dislike : dislikeFods) {
                Food food = apiConsumption.getFoodInfo(dislike);

                if (food != null) {
                    Optional<Food> existingFood = foodRepository.findFirstByFoodName(food.getFoodName());
                    if (existingFood.isPresent()) {
                        food = existingFood.get();
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

            Properties properties = new Properties();
            properties.setProperty("preferencesCompleted", "true");
            try (FileOutputStream out = new FileOutputStream("preferencesState.properties")) {
                properties.store(out, null);
            } catch (IOException e) {
                e.printStackTrace();
            }

            closeCurrentWindow();
            dashboardFrame.hidePreferencesUI();


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
                if (items.contains("Ninguna") || (items.size() >= 5) ||
                        (!items.isEmpty() && selectedItem.equals("Ninguna"))) {
                        errorLabel.setText("Verificar valores ingresados");
                        errorLabel.setVisible(true);
                    return;
                }

                if (!items.contains(selectedItem)) {
                    items.add(selectedItem);
                }
            }

            // Borra lo escrito en el ComboBox sin alterar las sugerencias
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
            comboBox.getItems().add("Ninguna"); // Mantener "Ninguna"
            comboBox.show();
        });
    }

    private void closeCurrentWindow() {
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }


}
