package com.prueba.demo.principal;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import org.springframework.stereotype.Component;

@Component
public class SelectYourFood {
    @FXML
    private Button selectButton;
    @FXML
    private ComboBox<String> suggestionsComboBox;
    @FXML
    private ImageView foodImageView;
    @FXML
    private ListView<String> suggestionsListView;






}
