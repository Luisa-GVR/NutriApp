package com.prueba.demo.principal;

import com.prueba.demo.model.*;
import com.prueba.demo.repository.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
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
    private ListView<String> mondayListView;
    @FXML
    private ListView<String> tuesdayListView;
    @FXML
    private ListView<String> wednesdayListView;
    @FXML
    private ListView<String> thursdayListView;
    @FXML
    private ListView<String> fridayListView;
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
    private final HashSet<String> selectedExercises = new HashSet<>();

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
        setupListViewWithDeleteButton(mondayListView);
        setupListViewWithDeleteButton(tuesdayListView);
        setupListViewWithDeleteButton(wednesdayListView);
        setupListViewWithDeleteButton(thursdayListView);
        setupListViewWithDeleteButton(fridayListView);


        errorLabel.setVisible(false);

        objetiveChoiceBox.setItems(FXCollections.observableArrayList("Deficit calórico",
                "Mantenimiento", "Volumen"));

        // Inicializar ComboBoxes con todos los ejercicios
        mondayChoiceBox.setItems(FXCollections.observableArrayList(allExercises));
        tuesdayChoiceBox.setItems(FXCollections.observableArrayList(allExercises));
        wednesdayChoiceBox.setItems(FXCollections.observableArrayList(allExercises));
        thursdayChoiceBox.setItems(FXCollections.observableArrayList(allExercises));
        fridayChoiceBox.setItems(FXCollections.observableArrayList(allExercises)); // Viernes tiene todos siempre


        // Agregar listeners para detectar cambios y actualizar los demás días
        setupChoiceBox(mondayChoiceBox, mondayListView);
        setupChoiceBox(tuesdayChoiceBox, tuesdayListView);
        setupChoiceBox(wednesdayChoiceBox, wednesdayListView);
        setupChoiceBox(thursdayChoiceBox, thursdayListView);
        setupChoiceBoxFriday(fridayChoiceBox, fridayListView);

        configureListView(mondayListView);
        configureListView(tuesdayListView);
        configureListView(wednesdayListView);
        configureListView(thursdayListView);
        configureListView(fridayListView);



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

            stage.setOnCloseRequest(event -> {
                event.consume(); // Previene el cierre inmediato

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmación de salida");
                alert.setGraphic(null);
                alert.setHeaderText(null);
                alert.setContentText("¿Seguro que deseas salir?");

                ButtonType yesButton = new ButtonType("Sí", ButtonBar.ButtonData.YES);
                ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);

                alert.getButtonTypes().setAll(yesButton, noButton);

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == yesButton) {
                    closeCurrentWindow(); // Aquí puedes restaurar y cerrar la ventana
                }
            });
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
                    errorLabel.setVisible(false);
                    exerciseErrorLabel.setVisible(false);

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


    private void setupChoiceBox(ChoiceBox<String> choiceBox, ListView<String> listView) {
        choiceBox.setOnAction(event -> {
            String selectedItem = choiceBox.getValue();
            ObservableList<String> items = listView.getItems();

            if (selectedItem != null && !items.contains(selectedItem)) {
                if (items.size() < 2) {
                    items.add(selectedItem);
                    selectedExercises.add(selectedItem);
                    updateAvailableExercises();

                }
            }
        });
    }


    private void setupChoiceBoxFriday(ChoiceBox<String> choiceBox, ListView<String> listView) {
        choiceBox.setOnAction(event -> {
            String selectedItem = choiceBox.getValue();
            ObservableList<String> items = listView.getItems();
            if (items.size() < 2) {
                items.add(selectedItem);
                selectedExercises.add(selectedItem);
            }

        });
    }

    private void updateAvailableExercises() {
        // Lista de todos los ChoiceBoxes a actualizar
        List<ChoiceBox<String>> allChoiceBoxes = Arrays.asList(
                mondayChoiceBox, tuesdayChoiceBox, wednesdayChoiceBox, thursdayChoiceBox);

        // Actualizar todos los ComboBoxes
        for (ChoiceBox<String> choiceBox : allChoiceBoxes) {
            updateComboBox(choiceBox, selectedExercises);
        }
    }

    private void updateComboBox(ChoiceBox<String> choiceBox, Set<String> usedExercises) {
        ObservableList<String> filteredExercises = FXCollections.observableArrayList();

        for (String exercise : allExercises) {
            if (!usedExercises.contains(exercise)) {
                filteredExercises.add(exercise);
            }
        }

        // Update the ChoiceBox with the filtered list
        choiceBox.setItems(filteredExercises);
    }

    private void configureListView(ListView<String> listView) {
        listView.setOnMouseClicked(event -> {
            String selectedItem = listView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                listView.getItems().remove(selectedItem);
                selectedExercises.remove(selectedItem);
                updateAvailableExercises();

            }
        });

        listView.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                event.consume();

            }
        });

    }
    private boolean validateFields() {
        boolean validInputs = true;

        // Lista con todos los ListView a validar
        List<ListView<String>> listViews = Arrays.asList(
                mondayListView, tuesdayListView, wednesdayListView,
                thursdayListView, fridayListView
        );

        // Validar que cada ListView tenga exactamente 2 elementos
        for (ListView<String> listView : listViews) {
            if (listView.getItems().size() != 2) { // Verifica si tiene exactamente 2 elementos
                //exerciseErrorLabel.setText("Por favor, selecciona exactamente 2 ejercicios para cada día.");
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
                mondayTypes.addAll(getValidExerciseTypes(Collections.singletonList(mondayListView.getItems().get(0))));
                mondayTypes.addAll(getValidExerciseTypes(Collections.singletonList(mondayListView.getItems().get(1))));

                List<ExcerciseType> tuesdayTypes = new ArrayList<>();
                tuesdayTypes.addAll(getValidExerciseTypes(Collections.singletonList(tuesdayListView.getItems().get(0))));
                tuesdayTypes.addAll(getValidExerciseTypes(Collections.singletonList(tuesdayListView.getItems().get(1))));


                List<ExcerciseType> wednesdayTypes = new ArrayList<>();
                wednesdayTypes.addAll(getValidExerciseTypes(Collections.singletonList(wednesdayListView.getItems().get(0))));
                wednesdayTypes.addAll(getValidExerciseTypes(Collections.singletonList(wednesdayListView.getItems().get(1))));

                List<ExcerciseType> thursdayTypes = new ArrayList<>();
                thursdayTypes.addAll(getValidExerciseTypes(Collections.singletonList(thursdayListView.getItems().get(0))));
                thursdayTypes.addAll(getValidExerciseTypes(Collections.singletonList(thursdayListView.getItems().get(1))));

                List<ExcerciseType> fridayTypes = new ArrayList<>();
                fridayTypes.addAll(getValidExerciseTypes(Collections.singletonList(fridayListView.getItems().get(0))));
                fridayTypes.addAll(getValidExerciseTypes(Collections.singletonList(fridayListView.getItems().get(1))));

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
