package com.prueba.demo.principal;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import org.springframework.stereotype.Controller;

@Controller
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
}
