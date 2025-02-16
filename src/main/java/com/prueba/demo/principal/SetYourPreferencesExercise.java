package com.prueba.demo.principal;

import com.prueba.demo.model.*;
import com.prueba.demo.repository.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
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


    @Autowired
    ExcerciseRepository excerciseRepository;

    @Autowired
    DayExcerciseRepository dayExcerciseRepository;




    private final ObservableList<String> allExercises = FXCollections.observableArrayList(
            "Pecho y Brazo", "Pierna Completa", "Hombro y Espalda", "Abdomen y Cardio"
    );

    // Mapa para almacenar la selección de cada día
    private final Map<ChoiceBox<String>, String> selectedExercises = new HashMap<>();

    @FXML
    private void initialize() {

        //exerciseErrorLabel.setVisible(false);

        objetiveChoiceBox.setItems(FXCollections.observableArrayList("Deficit",
                "Mantenimiento", "Volumen"));

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
                    completeProfile();
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

    private void completeProfile() {
        accountRepository.findById(1L).ifPresentOrElse(account -> {
            AccountData accountData = account.getAccountData();

            if (accountData == null) {
                accountData = new AccountData();
                account.setAccountData(accountData);
            }

            try {
                // Validar y asignar los ejercicios seleccionados
                accountData.setMonday(getValidExerciseType(mondayChoiceBox.getValue()));
                accountData.setTuesday(getValidExerciseType(tuesdayChoiceBox.getValue()));
                accountData.setWednesday(getValidExerciseType(wednesdayChoiceBox.getValue()));
                accountData.setThursday(getValidExerciseType(thursdayChoiceBox.getValue()));
                accountData.setFriday(getValidExerciseType(fridayChoiceBox.getValue()));

                String selectedGoal = objetiveChoiceBox.getValue();
                accountData.setGoal(Goal.fromString(selectedGoal));
                // Guardar cambios en la base de datos
                accountDataRepository.save(accountData);
                accountRepository.save(account);

                // Guardar estado de preferencias en un archivo
                savePreferencesState();

                closeCurrentWindow();
                dashboardFrame.hidePreferencesUI();
            } catch (IllegalArgumentException e) {
                System.out.println("Error: Opción inválida seleccionada en el ChoiceBox.");
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("Error al guardar el estado de preferencias.");
                e.printStackTrace();
            }
        }, () -> System.out.println("Error: No se encontró la cuenta con ID 1"));
    }

    /**
     * Método para validar y convertir valores del ChoiceBox en ExerciseType
     */
    private ExcerciseType getValidExerciseType(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("El valor del ChoiceBox no puede estar vacío");
        }

        // Normalizar el valor: quitar espacios y convertir a minúsculas
        String formattedValue = value.replace(" ", "").toLowerCase();

        // Convertir el valor formateado al enum
        try {
            return ExcerciseType.fromString(formattedValue); // Usar el método fromString del enum
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("El valor seleccionado no es válido para el ejercicio.");
        }
    }


    /**
     * Método para guardar el estado de preferencias en un archivo
     */
    private void savePreferencesState() throws IOException {
        Properties properties = new Properties();

        // Cargar las propiedades existentes
        try (FileInputStream in = new FileInputStream("preferencesState.properties")) {
            properties.load(in);
        } catch (IOException e) {
            // Si el archivo no existe, no pasa nada
        }

        // Verificar si ya se ha completado el ejercicio
        if (!"true".equals(properties.getProperty("preferencesExerciseCompleted"))) {
            properties.setProperty("preferencesExerciseCompleted", "true");

            // Guardar las nuevas propiedades en el archivo
            try (FileOutputStream out = new FileOutputStream("preferencesState.properties")) {
                properties.store(out, null);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Hacer invisible el HBox en DashboardFrame
            dashboardFrame.excerciseHbox.setVisible(false); // Cambiar visibilidad a false

        } else {
            System.out.println("El ejercicio ya está marcado como completado.");
        }
    }



    @Autowired
    private DashboardFrame dashboardFrame;
    public SetYourPreferencesExercise(DashboardFrame dashboardFrame) {
        this.dashboardFrame = dashboardFrame;
    }


    private void closeCurrentWindow() {
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }


}
