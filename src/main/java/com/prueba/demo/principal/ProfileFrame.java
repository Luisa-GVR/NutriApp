package com.prueba.demo.principal;

import com.prueba.demo.model.*;
import com.prueba.demo.repository.*;
import com.prueba.demo.service.APIConsumption;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Service
public class ProfileFrame {
    //VBox principal
    @FXML
    private VBox rootPane;
    @FXML
    private Button profileButton;
    @FXML
    private ChoiceBox sexChoiceBox;
    @FXML
    private ComboBox<String> allergiesComboBox;

    @FXML
    private TextArea ageTextArea;
    @FXML
    private TextArea heightTextArea;
    @FXML
    private TextArea weightTextArea;
    @FXML
    private TextArea abdomenTextArea;
    @FXML
    private TextArea hipTextArea;
    @FXML
    private TextArea waistTextArea;
    @FXML
    private TextArea neckTextArea;
    @FXML
    private TextArea armTextArea;
    @FXML
    private TextArea chestTextArea;

    @FXML
    private Label ageErrorLabel;
    @FXML
    private Label heightErrorLabel;
    @FXML
    private Label weightErrorLabel;
    @FXML
    private Label abdomenErrorLabel;
    @FXML
    private Label hipErrorLabel;
    @FXML
    private Label waistErrorLabel;
    @FXML
    private Label neckErrorLabel;
    @FXML
    private Label armErrorLabel;
    @FXML
    private Label chestErrorLabel;
    @FXML
    private Label sexErrorLabel;
    @FXML
    private Label allergiesErrorLabel;



    //Metodos front
    @FXML
    private void handleMouseEntered(MouseEvent event) {
        Button button = (Button) event.getSource();
        button.setStyle("-fx-background-color: #595959;");
    }

    @FXML
    private void handleMouseExited(MouseEvent event) {
        Button button = (Button) event.getSource();
        button.setStyle("-fx-background-color:  #262626;");
    }



    @FXML
    private ListView<String> allergiesListView;


    @FXML
    private void initialize() {
        setupListViewWithDeleteButton(allergiesListView);
        // Inicializar los labels de error
        ageErrorLabel.setVisible(false);
        heightErrorLabel.setVisible(false);
        weightErrorLabel.setVisible(false);
        abdomenErrorLabel.setVisible(false);
        hipErrorLabel.setVisible(false);
        waistErrorLabel.setVisible(false);
        neckErrorLabel.setVisible(false);
        armErrorLabel.setVisible(false);
        chestErrorLabel.setVisible(false);
        allergiesErrorLabel.setVisible(false);
        // Cargar opciones para sexChoiceBox
        sexErrorLabel.setVisible(false);
        sexChoiceBox.setItems(FXCollections.observableArrayList("Masculino", "Femenino"));
        allergiesComboBox.setItems(FXCollections.observableArrayList("Ninguna"));



        // Listener para manejar la selección SOLO desde el dropdown
        allergiesComboBox.setOnMouseClicked(event -> {
            allergiesComboBox.show(); // Asegura que el dropdown se muestre al hacer clic

        });


        allergiesComboBox.getEditor().setOnKeyTyped(event -> {
            String query = allergiesComboBox.getEditor().getText().trim(); // Eliminar espacios vacíos

            if (event.getCharacter().equals("\r")) { // Enter presionado
                if (!query.isEmpty()) {
                    // Asegurar que no haya cadenas vacías antes de agregar un nuevo valor
                    allergiesComboBox.getItems().removeIf(String::isEmpty);

                    // Evitar que se agreguen duplicados
                    if (!allergiesComboBox.getItems().contains(query)) {
                        allergiesComboBox.getItems().add(query);
                    }

                    // Ejecutar la búsqueda
                    searchAllergies(query);
                }

                // Limpiar el campo de entrada en el siguiente ciclo de ejecución
                Platform.runLater(() -> allergiesComboBox.getEditor().clear());
            }
        });



        allergiesComboBox.setOnShowing(event -> {
            Platform.runLater(() -> {
                allergiesComboBox.getSelectionModel().clearSelection();
                allergiesComboBox.getEditor().clear(); // Limpia cualquier texto en el editor
            });
        });



        allergiesComboBox.setOnAction(event -> {
            if (!allergiesComboBox.isShowing()) { // Solo ejecuta si el usuario selecciono desde el dropdown
                return;
            }

            String selectedItem = allergiesComboBox.getSelectionModel().getSelectedItem();
            ObservableList<String> items = allergiesListView.getItems();

            if (selectedItem != null) {

                // Verificar si "Ninguna" ya está en la lista
                if (items.contains("Ninguna")) {
                    allergiesErrorLabel.setText("Elimina 'Ninguna' para agregar más elementos.");
                    allergiesErrorLabel.setVisible(true);
                    //allergiesComboBox.setValue("");

                    return;
                }

                // Verificar si hay más de 5 elementos diferentes
                if (items.size() > 5) {
                    allergiesErrorLabel.setText("No se puede agregar mas de 5 elementos");
                    allergiesErrorLabel.setVisible(true);
                    //allergiesComboBox.setValue("");

                    return;
                }


                if (!items.isEmpty() && selectedItem.equals("Ninguna")){
                    //allergiesComboBox.setValue("");
                    return;
                }

                // Agregar solo si no está duplicado
                if (!items.contains(selectedItem)) {
                    items.add(selectedItem);
                    allergiesErrorLabel.setVisible(false); // Oculta error si la selección es válida
                    //allergiesComboBox.setValue("");
                }

            }
            Platform.runLater(() -> allergiesComboBox.getEditor().clear()); // Borra el texto después de la búsqueda

        });
        allergiesComboBox.setOnMouseClicked(event -> allergiesErrorLabel.setVisible(false));




        //Listener para manejar el borrado de alergias
        allergiesListView.setOnMouseClicked(event -> {
            String selectedItem = allergiesListView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                allergiesListView.getItems().remove(selectedItem);

                if (!allergiesListView.getItems().contains("Ninguna")) {
                    allergiesErrorLabel.setVisible(false);  // Ocultar error
                }
            }
        });



        // Evento para completar el perfil
        profileButton.setOnAction(actionEvent -> {
            try {
                if (validateFields()) {
                    completeProfile();
                }
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Error", "No se pudo completar el perfil.");
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
                    allergiesErrorLabel.setVisible(false);
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

    @Autowired
    private APIConsumption apiConsumption;
    private void searchAllergies(String query) {
        List<String> suggestions = apiConsumption.getFoodSuggestionsNeutral(query); //API busqueda

        Platform.runLater(() -> {
            allergiesComboBox.getItems().clear();
            allergiesComboBox.getItems().addAll(suggestions); // Agregar nuevas sugerencias
            allergiesComboBox.getItems().add("Ninguna"); // Mantener "Ninguna"
            allergiesComboBox.show();
        });
    }



    @Autowired
    private AccountDataRepository accountDataRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private AccountAllergyFoodRepository accountAllergyFoodRepository;
    @Autowired
    private AccountAllergyRepository accountAllergyRepository;

    private void completeProfile() {
        AccountData accountData = new AccountData();

        accountData.setAge(Integer.parseInt(ageTextArea.getText().trim()));
        accountData.setGender(!sexChoiceBox.getValue().equals("Femenino"));
        accountData.setHeight(Double.parseDouble(heightTextArea.getText().trim()));
        accountData.setWeight(Double.parseDouble(weightTextArea.getText().trim()));
        accountData.setAbdomen(parseOrDefault(abdomenTextArea, 0.0));
        accountData.setHips(parseOrDefault(hipTextArea, 0.0));
        accountData.setWaist(parseOrDefault(waistTextArea, 0.0));
        accountData.setArm(parseOrDefault(armTextArea, 0.0));
        accountData.setChest(parseOrDefault(chestTextArea, 0.0));
        accountData.setNeck(parseOrDefault(neckTextArea, 0.0));

        // Guardar AccountData
        accountDataRepository.save(accountData);


        // Obtener la lista de alergias
        ObservableList<String> allergies = allergiesListView.getItems();
        List<Food> foodList = new ArrayList<>();

        // Verificar si el único elemento en la lista es "Ninguna"
        if (allergies.size() == 1 && allergies.get(0).equals("Ninguna")) {
            accountData.setAccountAllergy(null);  // No se asigna AccountAllergy
            closeCurrentWindow();
            openDashboard();
        } else {
            // Crear AccountAllergy y asociar AccountData
            AccountAllergy accountAllergy = new AccountAllergy();
            accountAllergy.setAccountdata(accountData);
            accountAllergyRepository.save(accountAllergy);

            // Guardar alimentos relacionados con las alergias
            for (String allergy : allergies) {
                Food food = apiConsumption.getFoodInfo(allergy);

                if (food != null) {
                    Food existingFood = foodRepository.findFirstByFoodName(food.getFoodName());
                    if (existingFood != null) {
                        food = existingFood;  // Usar el alimento encontrado
                    } else {
                        foodRepository.save(food); // Guardar nuevo alimento
                    }

                    foodList.add(food);

                    // Crear relación AccountAllergyFood
                    AccountAllergyFood accountAllergyFood = new AccountAllergyFood();
                    accountAllergyFood.setFood(food);
                    accountAllergyFood.setAccountAllergy(accountAllergy);
                    accountAllergyFoodRepository.save(accountAllergyFood);
                }
            }

            // Asignar AccountAllergy a AccountData
            accountData.setAccountAllergy(accountAllergy);

            closeCurrentWindow();
            openDashboard();
        }

        // Obtener cuenta asociada y actualizar la relación
        Optional<Account> optionalAccount = accountRepository.findAll().stream().findFirst();

        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();

            if (account.getAccountData() != null) {
                accountDataRepository.delete(account.getAccountData());  // Eliminar datos previos
            }

            accountData.setAccount(account);  // Asociar AccountData con Account
            accountData = accountDataRepository.save(accountData);  // Guardar AccountData

            account.setAccountData(accountData);  // Actualizar relación en Account
            accountRepository.save(account);  // Guardar Account
        }
    }

    private double parseOrDefault(TextArea textArea, double defaultValue) {
        if (textArea.getText() != null && !textArea.getText().trim().isEmpty()) {
            try {
                return Double.parseDouble(textArea.getText().trim());
            } catch (NumberFormatException e) {
            }
        }
        return defaultValue;
    }

    private boolean validateFields() {

        boolean validInputs = true;

        //Validacion datos generaciones
        // Validar edad (13 - 120)
        validInputs &= isValidNumber(ageTextArea, 13, 120, "Edad", ageErrorLabel, 0);

        // Validar sexo
        validInputs &= isChoiceBoxSelected(sexChoiceBox, "Sexo", sexErrorLabel);
        sexChoiceBox.setOnMouseClicked(event -> sexErrorLabel.setVisible(false));


        // Validar estatura (90 - 300 cm)
        validInputs &= isValidNumber(heightTextArea, 90, 300, "Estatura", heightErrorLabel, 0);

        // Validar peso inicial (30 - 300 kg)
        validInputs &= isValidNumber(weightTextArea, 30, 300, "Peso inicial", weightErrorLabel, 0);

        if (allergiesListView.getItems().isEmpty()){
            allergiesErrorLabel.setText("Por favor, intrduce un valor válido.");
            allergiesErrorLabel.setVisible(true);
            allergiesComboBox.setOnMouseClicked(event -> allergiesErrorLabel.setVisible(false));

            validInputs = false;
        }


        //Validar medidas corporales

        // Validar abdomen ((40 - 170 cm)
        validInputs &= isValidNumber(abdomenTextArea, 40, 170, "Abdomen", abdomenErrorLabel, 1);

        // Validar cadera (50 - 170 cm)
        validInputs &= isValidNumber(hipTextArea, 50, 170, "Cadera", hipErrorLabel, 1);

        // Validar cintura (35 - 170 cm)
        validInputs &= isValidNumber(waistTextArea, 35, 170, "Cintura", waistErrorLabel, 1);

        // Validar cuello ((15 - 170 cm)
        validInputs &= isValidNumber(neckTextArea, 15, 170, "Cuello", neckErrorLabel, 1);

        // Validar brazo (10 - 170 cm)
        validInputs &= isValidNumber(armTextArea, 10, 170, "Brazo", armErrorLabel, 1);

        // Validar pecho (50 - 170 cm)
        validInputs &= isValidNumber(chestTextArea, 50, 170, "Pecho", chestErrorLabel, 1);


        return validInputs;
    }

    private boolean isValidNumber(TextArea textArea, int min, int max, String fieldName, Label label, int type) { //type 0 = debe estar lleno, 1= puede estar vacio
        if (type == 1 && (textArea.getText() == null || textArea.getText().trim().isEmpty())){
            label.setVisible(false);
            return true;
        }

        if (textArea.getText() == null || textArea.getText().trim().isEmpty()) {
            label.setText("El campo es obligatorio");
            label.setVisible(true);
            return false;

        }
        textArea.setOnMouseClicked(event -> label.setVisible(false));

        try {
            int value = Integer.parseInt(textArea.getText().trim());
            if (value < min || value > max) {
                label.setText(fieldName + " debe estar entre " + min + " y " + max + ".");
                label.setVisible(true);

                return false;
            }
            label.setVisible(false);

            return true;
        } catch (NumberFormatException e) {
            label.setText("Por favor, introduce un valor válido.");
            label.setVisible(true);

            return false;
        }
    }

    private boolean isChoiceBoxSelected(ChoiceBox<String> choiceBox, String fieldName, Label label) {
        if (choiceBox.getValue() == null) {
            label.setText("El campo es obligatorio");
            label.setVisible(true);
            return false;
        }
        label.setVisible(false);
        return true;
    }

    private void showAlert(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @Autowired
    private ApplicationContext applicationContext;
    private void openDashboard() {
        Platform.runLater(() -> {
            try {

                // Cargar la nueva ventana
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Dashboard.fxml"));
                loader.setControllerFactory(applicationContext::getBean); // *** Crucial Line ***


                Scene scene = new Scene(loader.load());

                // Crear un nuevo Stage para la ventana principal
                Stage newStage = new Stage();
                newStage.setTitle("Principal");
                newStage.setScene(scene);

                // Establecer el tamaño mínimo de la ventana principal
                newStage.setMinWidth(900);  // Ancho mínimo de la ventana
                newStage.setMinHeight(520); // Alto mínimo de la ventana

                // Mostrar la nueva ventana
                newStage.show();

            } catch (Exception e) {
                e.printStackTrace();  // Para obtener más detalles sobre el error
                showAlert("Error", "No se pudo abrir la ventana principal.");
            }
        });
    }


    private void closeCurrentWindow() {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }
}