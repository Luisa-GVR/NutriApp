package com.prueba.demo.principal;

import com.prueba.demo.model.DayExcercise;
import com.prueba.demo.model.Excercise;
import com.prueba.demo.repository.DayExcerciseRepository;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

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

    private int currentExerciseIndex;
    private List<Excercise> currentExercises;


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
        if (targetDate == null) {
            return;
        }

        List<DayExcercise> dayExcercises = dayExcerciseRepository.findAllByDate(java.sql.Date.valueOf(targetDate));

        if (dayExcercises != null && !dayExcercises.isEmpty()) {
            currentExercises = dayExcercises.get(0).getExcercises(); // Tomar la lista de ejercicios del primer día
            currentExerciseIndex = 0;

            if (!currentExercises.isEmpty()) {
                showExerciseDetails(currentExercises.get(currentExerciseIndex));
                nextButton.setVisible(currentExercises.size() > 1);
                backButton.setVisible(false);
            }
        }
    }


    private void showExerciseDetails(Excercise exercise) {
        exerciseNameLabel.setText(exercise.getExcerciseName());
        repetitionsTextArea.setText(String.valueOf(exercise.getReps()));
        seriesTextArea.setText(String.valueOf(exercise.getSeries()));
        timeTextArea.setText(String.valueOf(exercise.getTime()));

        repetitionsTextArea.setEditable(false);
        seriesTextArea.setEditable(false);
        timeTextArea.setEditable(false);

        Image image = new Image(exercise.getGifURL());
        exerciseImageView.setImage(image);
    }



    @Autowired
    DayExcerciseRepository dayExcerciseRepository;
    private List<DayExcercise> getDayExerciseForDate(LocalDate localDate) {
        return dayExcerciseRepository.findAllByDate(java.sql.Date.valueOf(localDate));
    }


    private void closeCurrentWindow() {
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();

    }

    @FXML
    private void onNextButtonClicked() {


        if (currentExercises != null && currentExerciseIndex < currentExercises.size() - 1) {
            currentExerciseIndex++;
            showExerciseDetails(currentExercises.get(currentExerciseIndex));

            backButton.setVisible(true);
            nextButton.setVisible(currentExerciseIndex < currentExercises.size() - 1);
        }
    }

    @FXML
    private void onBackButtonClicked() {
        if (currentExercises != null && currentExerciseIndex > 0) {
            currentExerciseIndex--;
            showExerciseDetails(currentExercises.get(currentExerciseIndex));

            nextButton.setVisible(true);
            backButton.setVisible(currentExerciseIndex > 0);
        }
    }

}
