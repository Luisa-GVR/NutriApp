package com.prueba.demo.principal;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;

@Component
public class CheckYourRutine {
    @FXML
    private Button nextButton;
    @FXML
    private Button backButton;
    @FXML
    private Button saveButton;
    @FXML
    private ImageView exerciseImageView;
    @FXML
    private Label exerciseNameLabel;
    @FXML
    private TextArea repetitionsTextArea;
    @FXML
    private TextArea seriesTextArea;
    @FXML
    private TextArea timeTextArea;

    @FXML
    private void initialize() {
        backButton.setVisible(false);
        nextButton.setVisible(false);


        saveButton.setOnAction(actionEvent -> {
            try {
                closeCurrentWindow();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }


    public void setTargetDate(LocalDate targetDate) {
        System.out.println(targetDate);

    }

    private void closeCurrentWindow() {
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();

    }
}
