package com.prueba.demo.principal;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import org.springframework.stereotype.Component;

@Component
public class SetYourPreferencesDiet {
    @FXML
    private ListView<String> favsFoodsListView;
    @FXML
    private ListView<String> NoFavsFoodsListView;
    @FXML
    private ComboBox<String> favsFoodsComboBox;
    @FXML
    private ComboBox<String> NoFavsFoodsComboBox;
    @FXML
    private Button saveButton;


}
