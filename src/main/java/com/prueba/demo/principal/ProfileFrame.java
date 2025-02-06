package com.prueba.demo.principal;

import com.prueba.demo.model.Account;
import com.prueba.demo.model.AccountAllergy;
import com.prueba.demo.model.AccountData;
import com.prueba.demo.repository.AccountDataRepository;
import com.prueba.demo.repository.AccountRepository;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
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
        button.setStyle("-fx-background-color: #A3D13C;");
    }

    @FXML
    private void handleMouseExited(MouseEvent event) {
        Button button = (Button) event.getSource();
        button.setStyle("-fx-background-color: #7DA12D;");
    }

    @FXML
    private void initialize() {

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
        sexErrorLabel.setVisible(false);


        allergiesComboBox = new ComboBox<>();
        allergiesComboBox.getItems().addAll("Maní", "Lácteos", "Gluten");

        sexChoiceBox.setItems(FXCollections.observableArrayList("Masculino", "Femenino"));

        //Editar el allergies

        //Evento dde click en completar perfil
        profileButton.setOnAction(actionEvent -> {
                try{
                    if(validateFields()){
                        completeProfile();
                    }


                }catch (Exception e){
                    e.printStackTrace();  // Para obtener más detalles sobre el error
                    showAlert("Error", "No se pudo completar el perfil.");
                }
            }
        );


        //Variables de estilos originales



    }





    @Autowired
    private AccountDataRepository accountDataRepository;

    @Autowired
    private AccountRepository accountRepository;

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

        allergiesComboBox.getValue();

/*
        AccountAllergy.
        accountData.setAccountAllergy(allergiesComboBox.getValue());
*/

        Optional<Account> optionalAccount = accountRepository.findAll().stream().findFirst();

        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            if (account.getAccountData() != null) {
                accountDataRepository.delete(account.getAccountData());
            }
            accountData.setAccount(account);
            accountData = accountDataRepository.save(accountData);

            account.setAccountData(accountData);
            accountRepository.save(account);
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
        //validInputs &= isChoiceBoxSelected(sexChoiceBox, "Sexo", , 0);

        // Validar estatura (90 - 300 cm)
        validInputs &= isValidNumber(heightTextArea, 90, 300, "Estatura", heightErrorLabel, 0);

        // Validar peso inicial (30 - 300 kg)
        validInputs &= isValidNumber(weightTextArea, 30, 300, "Peso inicial", weightErrorLabel, 0);


        //Validar medidas corporales

        // Validar abdomen ((90 - 300 cm)
        validInputs &= isValidNumber(abdomenTextArea, 90, 300, "Abdomen", abdomenErrorLabel, 1);

        // Validar cadera (90 - 300 cm)
        validInputs &= isValidNumber(hipTextArea, 30, 300, "Cadera", hipErrorLabel, 1);

        // Validar cintura (90 - 300 cm)
        validInputs &= isValidNumber(waistTextArea, 90, 300, "Cintura", waistErrorLabel, 1);

        // Validar cuello ((90 - 300 cm)
        validInputs &= isValidNumber(neckTextArea, 90, 300, "Cuello", neckErrorLabel, 1);

        // Validar brazo (90 - 300 cm)
        validInputs &= isValidNumber(armTextArea, 30, 300, "Brazo", armErrorLabel, 1);

        // Validar pecho (90 - 300 cm)
        validInputs &= isValidNumber(chestTextArea, 90, 300, "Pecho", chestErrorLabel, 1);


        return validInputs;
    }

    private boolean isValidNumber(TextArea textArea, int min, int max, String fieldName, Label label, int type) { //type 0 = debe estar lleno, 1= puede estar vacio
        if (type == 1 && (textArea.getText() == null || textArea.getText().trim().isEmpty())){
            label.setVisible(false);
            return true;
        }

        if (textArea.getText() == null || textArea.getText().trim().isEmpty()) {
            label.setText(fieldName + " está vacío. Ingresa un valor.");
            label.setVisible(true);
            return false;
        }
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
            label.setText(fieldName + " debe ser un número válido.");
            label.setVisible(true);

            return false;
        }
    }

    private boolean isChoiceBoxSelected(ChoiceBox<String> choiceBox, String fieldName, Label label) {
        if (choiceBox.getValue() == null) {
            label.setText("Selecciona una opción para " + fieldName + ".");
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

}



