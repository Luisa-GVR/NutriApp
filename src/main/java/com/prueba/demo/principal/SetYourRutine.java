package com.prueba.demo.principal;

import com.prueba.demo.model.DayExcercise;
import com.prueba.demo.model.DayMeal;
import com.prueba.demo.model.Excercise;
import com.prueba.demo.model.Food;
import com.prueba.demo.repository.DayExcerciseRepository;
import com.prueba.demo.repository.ExcerciseRepository;
import com.prueba.demo.service.APIConsumption;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.util.Duration;
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


    @FXML
    private void initialize() {

        Platform.runLater(() -> {
            Stage stage = (Stage) suggestionsComboBox.getScene().getWindow();

            stage.setOnCloseRequest(event -> {
                cachedSuggestions = null;
            });
        });

        suggestionsComboBox.setMaxHeight(400);

        //Buscar ejercicios

        // Evitar que Enter agregue elementos automáticamente
        suggestionsComboBox.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                event.consume(); // Evita que Enter agregue la informacioon al listview
            }
        });



        // Listener para manejar la selección SOLO desde el dropdown
        suggestionsComboBox.setOnMouseClicked(event -> {

            suggestionsComboBox.show(); // Asegura que el dropdown se muestre al hacer clic
            suggestionsComboBox.getItems().clear();
            suggestionsComboBox.getItems().addAll(cachedSuggestions); // Agregar nuevas sugerencias
            suggestionsComboBox.show();

        });

        PauseTransition pauseTransition = new PauseTransition(Duration.millis(500));

        // Listener para mostrar las sugerencias de la API cuando el usuario escribe
        suggestionsComboBox.setOnKeyReleased(event -> {
            pauseTransition.setOnFinished(e -> {
                String query = suggestionsComboBox.getEditor().getText();
                if (!query.isEmpty()) {

                    searchExercise(query);
                }
            });
            pauseTransition.playFromStart(); // Reiniciar el temporizador cada vez que se escribe
        });

        suggestionsComboBox.setOnAction(event -> {
            if (!suggestionsComboBox.isShowing()) { // Solo ejecuta si el usuario selecciono desde el dropdown
                return;
            }

            String selectedItem = suggestionsComboBox.getSelectionModel().getSelectedItem();
            ObservableList<String> items = suggestionsListView.getItems();

            if (selectedItem != null) {

                items.add(selectedItem);

                Optional<String> excerciseURL = apiConsumption.searchExerciseImage(selectedItem);

                if (excerciseURL.isPresent() && excerciseURL.get() != null && !excerciseURL.get().isEmpty()) {
                    Image excerciseImage = new Image(excerciseURL.get()); // Use the actual URL here
                    exerciseImageView.setImage(excerciseImage);
                } else {

                }
            }
        });



        //Listener para manejar el borrado de suggestions
        suggestionsListView.setOnMouseClicked(event -> {
            String selectedItem = suggestionsListView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                suggestionsListView.getItems().remove(selectedItem);

            }
        });


        saveButton.setOnAction(actionEvent -> {
            try {
                cachedSuggestions = null;
                addExcercise(suggestionsListView);
                closeCurrentWindow();
                refreshParentFrame();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    private void addExcercise(ListView<String> suggestionsListView) {

        LocalDate today = LocalDate.now();
        DayOfWeek currentDay = today.getDayOfWeek();
        int dayOfWeek = currentDay.getValue();

        int offset = getCol() - dayOfWeek;
        LocalDate targetDate = today.plusDays(offset);

        if (targetDate.getDayOfWeek().getValue() < getCol()) {
            targetDate = targetDate.plusWeeks(1); // Adjust to next week
        }

        //DayExcercise dayExcercise = getDayExcerciseForDate(targetDate);

        /*
        if (dayExcercise == null) {
            // If DayMeal does not exist for this date, create a new one
            dayExcercise = new DayExcercise();
            dayExcercise.setDate(java.sql.Date.valueOf(targetDate));
        }

         */

        //targetDate es el date que le debo poner al daymeal

        for (String selectedItem : suggestionsListView.getItems()) {
            Excercise selectedExcercise = getExcerciseByName(selectedItem);

            if (selectedExcercise != null) {
                //excerciseRepository.save(selectedExcercise);
            } else {
                System.out.println("Excercise not found: " + selectedItem);
            }


        }
        //dayExcerciseRepository.save(dayExcercise);
    }

    private Excercise getExcerciseByName(String excerciseName) {
        /*
        Excercise excercise = excerciseRepository.findFirstByExcerciseName(excerciseName);
        if (excercise != null) {
            excerciseRepository.save(excercise);
        }
        return excercise;

         */
        return null;
    }

    /*
    private DayExcercise getDayExcerciseForDate(LocalDate targetDate) {
        return dayExcerciseRepository.findByDate(java.sql.Date.valueOf(targetDate));
    }

     */

    private void searchExercise(String query) {
        List<String> suggestions = apiConsumption.getExerciseSuggestionsByMuscleGroup(query); //API busqueda

        Platform.runLater(() -> {
            suggestionsComboBox.getItems().clear();
            suggestionsComboBox.getItems().addAll(suggestions); // Agregar nuevas sugerencias
            suggestionsComboBox.show();

        });

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
                dashboardFrame.refreshContent();  // Assume refreshContent() is the method that refreshes the content
            });
        }
    }

}
