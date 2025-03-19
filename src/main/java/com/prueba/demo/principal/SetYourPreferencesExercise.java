package com.prueba.demo.principal;

import com.prueba.demo.model.*;
import com.prueba.demo.repository.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


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

    @FXML
    private ChoiceBox<String> mondayChoiceBox2;
    @FXML
    private ChoiceBox<String> tuesdayChoiceBox2;
    @FXML
    private ChoiceBox<String> wednesdayChoiceBox2;
    @FXML
    private ChoiceBox<String> thursdayChoiceBox2;
    @FXML
    private ChoiceBox<String> fridayChoiceBox2;
    @FXML
    private Label errorLabel;


    @Autowired
    ExcerciseRepository excerciseRepository;

    @Autowired
    DayExcerciseRepository dayExcerciseRepository;


    public static final ObservableList<String> allExercises = FXCollections.observableArrayList(
            "Back", "Cardio", "Chest", "Lower Arms", "Lower Legs", "Neck", "Shoulders", "Upper Arms", "Upper Legs", "Waist"
    );

    private final List<String> originalExercises = new ArrayList<>(allExercises); // Copia de la lista original

    private void restoreExercises() {
        // Restaurar los ejercicios originales en la lista
        allExercises.clear();
        allExercises.addAll(originalExercises);
        selectedExercises.clear();
        // Actualizar los ChoiceBoxes con los ejercicios restaurados
        updateAvailableExercises();
    }


    // Mapa para almacenar la selección de cada día
    private final Map<ChoiceBox<String>, String> selectedExercises = new HashMap<>();

    @FXML
    private void handleMouseEntered(MouseEvent event) {
        Button button = (Button) event.getSource();
        button.setStyle("-fx-background-color: #A3D13C;");
    }

    @FXML
    private void handleMouseExited(MouseEvent event) {
        Button button = (Button) event.getSource();
        button.setStyle("-fx-background-color:   #7da12d;");
    }
    @FXML
    private void initialize() {
        errorLabel.setVisible(false);

        objetiveChoiceBox.setItems(FXCollections.observableArrayList("Deficit calórico",
                "Mantenimiento", "Volumen"));

        // Inicializar ComboBoxes con todos los ejercicios
        mondayChoiceBox.setItems(FXCollections.observableArrayList(allExercises));
        tuesdayChoiceBox.setItems(FXCollections.observableArrayList(allExercises));
        wednesdayChoiceBox.setItems(FXCollections.observableArrayList(allExercises));
        thursdayChoiceBox.setItems(FXCollections.observableArrayList(allExercises));
        fridayChoiceBox.setItems(FXCollections.observableArrayList(allExercises)); // Viernes tiene todos siempre

        mondayChoiceBox2.setItems(FXCollections.observableArrayList(allExercises));
        tuesdayChoiceBox2.setItems(FXCollections.observableArrayList(allExercises));
        wednesdayChoiceBox2.setItems(FXCollections.observableArrayList(allExercises));
        thursdayChoiceBox2.setItems(FXCollections.observableArrayList(allExercises));
        fridayChoiceBox2.setItems(FXCollections.observableArrayList(allExercises));


        // Agregar listeners para detectar cambios y actualizar los demás días
        setupChoiceBox(mondayChoiceBox);
        setupChoiceBox(tuesdayChoiceBox);
        setupChoiceBox(wednesdayChoiceBox);
        setupChoiceBox(thursdayChoiceBox);

        setupChoiceBox(mondayChoiceBox2);
        setupChoiceBox(tuesdayChoiceBox2);
        setupChoiceBox(wednesdayChoiceBox2);
        setupChoiceBox(thursdayChoiceBox2);

        saveButton.setOnAction(actionEvent -> {
            try {
                if (validateFields()) {
                    completeProfile();
                }else {
                    System.out.println("entre a save y no valido desde else");
                    errorLabel.setVisible(true);

                }
            } catch (Exception e) {
                e.printStackTrace();
                //showAlert("Error", "No se pudo completar el perfil.");
            }
        });

        Platform.runLater(() -> {
            Stage stage = (Stage) saveButton.getScene().getWindow();

            // Añadir el manejador para el evento de cierre de la ventana (X)
            stage.setOnCloseRequest(event -> {
                event.consume();  // Evitar que la ventana se cierre inmediatamente
                closeCurrentWindow();  // Llamar a tu método para restaurar ejercicios y cerrar la ventana
            });
        });

    }


    private void setupChoiceBox(ChoiceBox<String> choiceBox) {
        choiceBox.setOnAction(event -> {
            selectedExercises.put(choiceBox, choiceBox.getValue());
            updateAvailableExercises();
        });
    }

    private void updateAvailableExercises() {
        System.out.println("lol3");

        // Obtener los ejercicios seleccionados
        Set<String> usedExercises = new HashSet<>(selectedExercises.values());

        // Lista de todos los ChoiceBoxes a actualizar
        List<ChoiceBox<String>> allChoiceBoxes = Arrays.asList(
                mondayChoiceBox, tuesdayChoiceBox, wednesdayChoiceBox, thursdayChoiceBox,
                mondayChoiceBox2, tuesdayChoiceBox2, wednesdayChoiceBox2, thursdayChoiceBox2
        );

        // Actualizar todos los ComboBoxes
        for (ChoiceBox<String> choiceBox : allChoiceBoxes) {
            updateComboBox(choiceBox, usedExercises);
        }
    }

    private void updateComboBox(ChoiceBox<String> choiceBox, Set<String> usedExercises) {
        System.out.println("lol2");

        // Verificar si realmente es necesario actualizar el ComboBox
        String selected = selectedExercises.get(choiceBox);
        ObservableList<String> updatedList = FXCollections.observableArrayList(allExercises);

        // Eliminar ejercicios ya usados, excepto si es el seleccionado actualmente
        updatedList.removeIf(exercise -> usedExercises.contains(exercise) && !exercise.equals(selected));

        // Solo actualizar si la lista ha cambiado
        if (!choiceBox.getItems().equals(updatedList)) {
            // Actualizar los items disponibles
            choiceBox.setItems(updatedList);
            choiceBox.setValue(selected); // Mantener la selección
        }
    }


    private boolean validateFields() {

        System.out.println("lol1");

        boolean validInputs = true;

        // Lista con todos los ComboBox a validar
        List<ChoiceBox<String>> choiceBoxes = Arrays.asList(
                mondayChoiceBox, tuesdayChoiceBox, wednesdayChoiceBox,
                thursdayChoiceBox, fridayChoiceBox, mondayChoiceBox2, tuesdayChoiceBox2, wednesdayChoiceBox2,
                thursdayChoiceBox2, fridayChoiceBox2
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

                List<ExcerciseType> mondayTypes = new ArrayList<>();
                mondayTypes.addAll(getValidExerciseTypes(Collections.singletonList(mondayChoiceBox.getValue())));
                mondayTypes.addAll(getValidExerciseTypes(Collections.singletonList(mondayChoiceBox2.getValue())));

                List<ExcerciseType> tuesdayTypes = new ArrayList<>();
                tuesdayTypes.addAll(getValidExerciseTypes(Collections.singletonList(tuesdayChoiceBox.getValue())));
                tuesdayTypes.addAll(getValidExerciseTypes(Collections.singletonList(tuesdayChoiceBox2.getValue())));

                List<ExcerciseType> wednesdayTypes = new ArrayList<>();
                wednesdayTypes.addAll(getValidExerciseTypes(Collections.singletonList(wednesdayChoiceBox.getValue())));
                wednesdayTypes.addAll(getValidExerciseTypes(Collections.singletonList(wednesdayChoiceBox2.getValue())));

                List<ExcerciseType> thursdayTypes = new ArrayList<>();
                thursdayTypes.addAll(getValidExerciseTypes(Collections.singletonList(thursdayChoiceBox.getValue())));
                thursdayTypes.addAll(getValidExerciseTypes(Collections.singletonList(thursdayChoiceBox2.getValue())));

                List<ExcerciseType> fridayTypes = new ArrayList<>();
                fridayTypes.addAll(getValidExerciseTypes(Collections.singletonList(fridayChoiceBox.getValue())));
                fridayTypes.addAll(getValidExerciseTypes(Collections.singletonList(fridayChoiceBox2.getValue())));

                accountData.setMonday(mondayTypes);
                accountData.setTuesday(tuesdayTypes);
                accountData.setWednesday(wednesdayTypes);
                accountData.setThursday(thursdayTypes);
                accountData.setFriday(fridayTypes);


                String selectedGoal = objetiveChoiceBox.getValue();
                if (selectedGoal.equals("Deficit calórico")){
                    selectedGoal = "Deficit";
                }

                accountData.setGoal(Goal.fromString(selectedGoal));
                // Guardar cambios en la base de datos
                accountDataRepository.save(accountData);
                accountRepository.save(account);
                // Guardar estado de preferencias en un archivo
                savePreferencesState();

                closeCurrentWindow();
                dashboardFrame.refreshContentExcercise();

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
    private List<ExcerciseType> getValidExerciseTypes(List<String> values) {
        if (values == null || values.isEmpty()) {
            return Collections.emptyList(); // Devuelve una lista vacía en lugar de lanzar excepción
        }

        return values.stream()
                .map(value -> {
                    if (value == null || value.trim().isEmpty()) {
                        throw new IllegalArgumentException("El valor del ChoiceBox no puede estar vacío");
                    }

                    // Reemplazar los espacios por guiones bajos y convertir a minúsculas
                    String formattedValue = value.replace(" ", "_").toUpperCase(); // Cambiar a mayúsculas y guión bajo

                    try {
                        // Intentar convertir el valor a un valor del enum ExcerciseType
                        return ExcerciseType.valueOf(formattedValue);
                    } catch (IllegalArgumentException e) {
                        // Si no es válido, lanzar una excepción personalizada
                        throw new IllegalArgumentException("El valor '" + value + "' no es válido para el ejercicio.");
                    }
                })
                .collect(Collectors.toList());
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

        }
    }

    @Autowired
    private DashboardFrame dashboardFrame;
    public SetYourPreferencesExercise(DashboardFrame dashboardFrame) {
        this.dashboardFrame = dashboardFrame;
    }


    private void closeCurrentWindow() {
        restoreExercises(); // Restaurar los ejercicios al cerrar la ventana
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }

}
