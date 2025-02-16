package com.prueba.demo.principal;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import org.springframework.stereotype.Component;

@Component
public class SetYourRutine {
    @FXML
    private Button saveButton;
    @FXML
    private ComboBox<String> suggestionsComboBox;
    @FXML
    private ImageView exerciseImageView;
    @FXML
    private ListView<String> suggestionsListView;

    @FXML
    private void initialize() {

    }


}
