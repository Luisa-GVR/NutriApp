package com.prueba.demo.principal;

import com.prueba.demo.model.*;
import com.prueba.demo.repository.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Component
public class SetYourPreferencesExercise {
    @FXML
    private Button saveButton;
    @FXML
    private Label exerciseErrorLabel;
    @FXML
    private ChoiceBox<String> objetiveChoiceBox;
    @FXML
    private ListView<String> exerciseListView;
    @FXML
    private ChoiceBox<String> mondayChoiceBox;
    @FXML
    private ChoiceBox<String> tuesdayChoiceBox;
    @FXML
    private ChoiceBox<String> wednesdayChoiceBox;
    @FXML
    private ChoiceBox<String> thursdayChoiceBox;
    @FXML
    private ChoiceBox<String> fridayChoiceBox;

    private final ObservableList<String> allExercises = FXCollections.observableArrayList(
            "Pecho y Brazo", "Pierna Completa", "Hombro y Espalda", "Abdomen y Cardio"
    );

    // Mapa para almacenar la selección de cada día
    private final Map<ChoiceBox<String>, String> selectedExercises = new HashMap<>();

    @FXML
    private void initialize() {

        //exerciseErrorLabel.setVisible(false);

        objetiveChoiceBox.setItems(FXCollections.observableArrayList("Pérdida de peso",
                "Mantener la forma", "Aumentar masa muscular"));

        // Inicializar ComboBoxes con todos los ejercicios
        mondayChoiceBox.setItems(FXCollections.observableArrayList(allExercises));
        tuesdayChoiceBox.setItems(FXCollections.observableArrayList(allExercises));
        wednesdayChoiceBox.setItems(FXCollections.observableArrayList(allExercises));
        thursdayChoiceBox.setItems(FXCollections.observableArrayList(allExercises));
        fridayChoiceBox.setItems(FXCollections.observableArrayList(allExercises)); // Viernes tiene todos siempre

        // Agregar listeners para detectar cambios y actualizar los demás días
        setupChoiceBox(mondayChoiceBox);
        setupChoiceBox(tuesdayChoiceBox);
        setupChoiceBox(wednesdayChoiceBox);
        setupChoiceBox(thursdayChoiceBox);

        saveButton.setOnAction(actionEvent -> {
            try {
                if (validateFields()) {
                    //completeProfile();
                    System.out.println("entre a save y llegue a que si valido");
                }else {
                    System.out.println("entre a save y no valido desde else");
                }
            } catch (Exception e) {
                e.printStackTrace();
                //showAlert("Error", "No se pudo completar el perfil.");
                System.out.println("entre a save y no valido desde catch");

            }
        });
    }

    private void setupChoiceBox(ChoiceBox<String> choiceBox) {
        choiceBox.setOnAction(event -> {
            selectedExercises.put(choiceBox, choiceBox.getValue());
            updateAvailableExercises();
        });
    }

    private void updateAvailableExercises() {
        // Obtener los ejercicios seleccionados
        Set<String> usedExercises = new HashSet<>(selectedExercises.values());

        // Actualizar todos los ComboBoxes excepto el viernes
        updateComboBox(mondayChoiceBox, usedExercises);
        updateComboBox(tuesdayChoiceBox, usedExercises);
        updateComboBox(wednesdayChoiceBox, usedExercises);
        updateComboBox(thursdayChoiceBox, usedExercises);
    }

    private void updateComboBox(ChoiceBox<String> choiceBox, Set<String> usedExercises) {
        String selected = selectedExercises.get(choiceBox);
        ObservableList<String> updatedList = FXCollections.observableArrayList(allExercises);

        // Eliminar ejercicios ya usados, excepto si es el seleccionado actualmente
        updatedList.removeIf(exercise -> usedExercises.contains(exercise) && !exercise.equals(selected));

        // Actualizar los items disponibles
        choiceBox.setItems(updatedList);
        choiceBox.setValue(selected); // Mantener la selección
    }


    private boolean validateFields() {
        boolean validInputs = true;

        // Lista con todos los ComboBox a validar
        List<ChoiceBox<String>> choiceBoxes = Arrays.asList(
                mondayChoiceBox, tuesdayChoiceBox, wednesdayChoiceBox,
                thursdayChoiceBox, fridayChoiceBox
        );

        // Validar que todos los ComboBox tengan una selección
        for (ChoiceBox<String> box : choiceBoxes) {
            if (box.getValue() == null) { // Verifica si el usuario seleccionó algo
                //exerciseErrorLabel.setText("Por favor, selecciona un grupo muscular para cada día.");
                //exerciseErrorLabel.setVisible(true);
                validInputs = false;
            }
        }

        // Validar que el objetivo tenga una selección
        if (objetiveChoiceBox.getValue() == null) {
            //exerciseErrorLabel.setText("Por favor, selecciona un objetivo.");
            //exerciseErrorLabel.setVisible(true);
            validInputs = false;
        }

        return validInputs;
    }

    @Autowired
    private AccountDataRepository accountDataRepository;
    @Autowired
    private AccountRepository accountRepository;

    /*
    @Autowired
    private AccountExerciseDayRepository AccountExerciseDayRepository;
*/

    /*
    private void completeProfile() {
        AccountData accountData = accountDataRepository.findById(1L).orElse(new AccountData());


        // Obtener la lista de ejercicios que gustan
        ObservableList<String> Exercises = exerciseListView.getItems();
        List<Excercise> exerciseList = new ArrayList<>();

        boolean exerciseNull = false;


        // Procesar ejercicios que gustan
        if (Exercises.isEmpty() || (Exercises.size() == 1)) {

            exerciseNull = true;
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

        Properties properties = new Properties();
        properties.setProperty("preferencesExerciseCompleted", "true");
        try (FileOutputStream out = new FileOutputStream("preferencesState.properties")) {
            properties.store(out, null);
        } catch (IOException e) {
            e.printStackTrace();
        }

        closeCurrentWindow();
        dashboardFrame.hidePreferencesUI();


    }

    private void closeCurrentWindow() {
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }


     */

}
