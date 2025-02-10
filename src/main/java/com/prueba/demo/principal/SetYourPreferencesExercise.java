package com.prueba.demo.principal;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import org.springframework.stereotype.Component;

@Component
public class SetYourPreferencesExercise {
    @FXML
    private Button saveButton;
    @FXML
    private ChoiceBox<String> objetiveChoiceBox;
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
    private void initialize(){



    }

}
