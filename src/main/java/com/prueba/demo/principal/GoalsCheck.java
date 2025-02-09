package com.prueba.demo.principal;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import org.springframework.stereotype.Component;

@Component
public class GoalsCheck {
    @FXML
    private Button saveButton;
    @FXML
    private ComboBox<String> breakfastComboBox;
    @FXML
    private ComboBox<String> lunchComboBox;
    @FXML
    private ComboBox<String> dinnerComboBox;
    @FXML
    private ComboBox<String> snackComboBox;
    @FXML
    private ComboBox<String> optionalComboBox;
    @FXML
    private ListView<String> breakfastListView;
    @FXML
    private ListView<String> lunchListView;
    @FXML
    private ListView<String> dinnerListView;
    @FXML
    private ListView<String> snackListView;
    @FXML
    private ListView<String> optionalListView;



    public void setBoxIndex(int choiceBoxIndex) {




    }
}
