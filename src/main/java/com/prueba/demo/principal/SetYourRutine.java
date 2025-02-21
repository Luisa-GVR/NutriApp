package com.prueba.demo.principal;

import com.prueba.demo.model.*;
import com.prueba.demo.repository.AccountDataRepository;
import com.prueba.demo.repository.AccountRepository;
import com.prueba.demo.repository.DayExcerciseRepository;
import com.prueba.demo.repository.ExcerciseRepository;
import com.prueba.demo.service.APIConsumption;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class SetYourRutine {

    private int row;
    private int col;

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    @FXML
    private Button saveButton;
    @FXML
    private ComboBox<String> suggestionsComboBox;
    @FXML
    private ImageView exerciseImageView;
    @FXML
    private ListView<String> suggestionsListView;

    private List<String> cachedSuggestions = null;

    @Autowired
    private APIConsumption apiConsumption;
    @Autowired
    private ExcerciseRepository excerciseRepository;
    @Autowired
    private DayExcerciseRepository dayExcerciseRepository;

    @Autowired
    private AccountDataRepository accountDataRepository;
    @Autowired
    private AccountRepository accountRepository;


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
        setupListViewWithDeleteButton(suggestionsListView);

        Platform.runLater(() -> {
            suggestionsComboBox.setEditable(false);

            Stage stage = (Stage) suggestionsComboBox.getScene().getWindow();
            stage.setOnCloseRequest(event -> {
                cachedSuggestions = null;
            });

            int row = getRow();
            ExcerciseType dayType = null;

            Optional<Account> account = accountRepository.findById(1L);
            AccountData accountData = account.get().getAccountData();

            switch (row){
                case 1:
                    dayType = accountData.getMonday();
                    break;
                case 2:
                    dayType = accountData.getTuesday();
                    break;
                case 3:
                    dayType = accountData.getWednesday();
                    break;
                case 4:
                    dayType = accountData.getThursday();
                    break;
                case 5:
                    dayType = accountData.getFriday();
                    break;
            }

            List<String> exercises = new ArrayList<>();

            if (dayType != null) {

                switch (dayType) {
                    case pechoybrazo:
                        exercises.add("chest");
                        exercises.add("lower%20arms");
                        exercises.add("upper%20arms");
                        break;
                    case piernacompleta:
                        exercises.add("upper%20legs");
                        exercises.add("lower%20legs");
                        break;
                    case hombroyespalda:
                        exercises.add("back");
                        exercises.add("shoulders");
                        break;
                    case abdomenycardio:
                        exercises.add("cardio");
                        exercises.add("waist");
                        break;
                    default:
                        break;
                }
            }

            List<String> allSuggestions = new ArrayList<>();

            if (cachedSuggestions == null){
                for (String muscleGroup : exercises) {
                    Goal goal = accountData.getGoal();
                    List<String> suggestions = apiConsumption.getExerciseSuggestionsByMuscleGroup(muscleGroup, dayType, goal);
                    allSuggestions.addAll(suggestions); // Añadimos todas las sugerencias al mismo list
                }

                cachedSuggestions = allSuggestions;
            }

            suggestionsComboBox.getItems().addAll(cachedSuggestions);

            //Listener para manejar el borrado de suggestions
            suggestionsListView.setOnMouseClicked(event -> {
                String selectedItem = suggestionsListView.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    suggestionsListView.getItems().remove(selectedItem);
                }
            });
            //agregar suggestions

            suggestionsComboBox.setOnAction(event -> {
                String selectedItem = suggestionsComboBox.getSelectionModel().getSelectedItem();
                if (selectedItem != null && !suggestionsListView.getItems().contains(selectedItem)) {
                    suggestionsListView.getItems().add(selectedItem);
                    String excerciseGifURL = excerciseRepository.findFirstByExcerciseName(selectedItem).getGifURL();
                    if (excerciseGifURL != null) {
                        Image exerciseImage = new Image(excerciseGifURL);
                        exerciseImageView.setImage(exerciseImage);
                    }
                }
            });


            saveButton.setOnAction(actionEvent -> {
                try {
                    if (suggestionsListView.getItems().size()>= 3 && suggestionsListView.getItems().size() <= 5){
                        cachedSuggestions = null;
                        addExcercise(suggestionsListView);
                        closeCurrentWindow();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
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
    private void addExcercise(ListView<String> suggestionsListView) {

        LocalDate today = LocalDate.now();
        DayOfWeek currentDay = today.getDayOfWeek();
        int dayOfWeek = currentDay.getValue();

        int offset = getRow() - dayOfWeek;
        LocalDate targetDate = today.plusDays(offset);

        if (targetDate.getDayOfWeek().getValue() < getCol()) {
            targetDate = targetDate.plusWeeks(1); // Adjust to next week
        }

        //fecha
        DayExcercise dayExcercise = new DayExcercise();
        dayExcercise.setDate(java.sql.Date.valueOf(targetDate));

        //Agregar ejercicios con los nombres
        List<Excercise> exercisesToAdd = new ArrayList<>();

        // Iterar sobre los nombres de los ejercicios en suggestionsListView
        for (String exerciseName : suggestionsListView.getItems()) {
            // Buscar cada ejercicio en la base de datos por nombre
            Excercise exercise = excerciseRepository.findByExcerciseName(exerciseName);
            exercisesToAdd.add(exercise);
        }
        dayExcercise.setExcercises(exercisesToAdd);


        Optional<Account> account = accountRepository.findById(1L);
        AccountData accountData = account.get().getAccountData();

        dayExcerciseRepository.save(dayExcercise);
    }


    private void closeCurrentWindow() {

        Stage stage = (Stage) suggestionsListView.getScene().getWindow();

        stage.setOnCloseRequest(event -> {
            cachedSuggestions = null;  // Reset cached suggestions when the window is closed
            if (dashboardFrame != null) {
                Platform.runLater(() -> {
                    dashboardFrame.hideAll();
                    dashboardFrame.showExercise();
                });
            }
        });

        stage.setOnHidden(event -> {
            if (dashboardFrame != null) {
                Platform.runLater(() -> {
                    cachedSuggestions = null;
                    dashboardFrame.hideAll();
                    dashboardFrame.showExercise();
                });
            };
        });

        stage.close();

    }
    private DashboardFrame dashboardFrame;
    public SetYourRutine(DashboardFrame dashboardFrame) {
        this.dashboardFrame = dashboardFrame;
    }

    private void refreshParentFrame() {
        if (dashboardFrame != null) {
            Platform.runLater(() -> {
                // This ensures that the parent frame gets updated in the UI thread
                dashboardFrame.refreshExcerciseContent();  // Assume refreshContent() is the method that refreshes the content
            });
        }
    }

}
