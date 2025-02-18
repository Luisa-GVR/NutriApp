package com.prueba.demo.principal;

import com.prueba.demo.model.DayExcercise;
import com.prueba.demo.model.Excercise;
import com.prueba.demo.model.Food;
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

    private int currentExcercise = 0;


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
            return; // exit early or handle it appropriately
        }

        List <DayExcercise> dayExcercises = getDayExerciseForDate(targetDate);

        currentExcercise = 0;

        if (dayExcercises != null && !dayExcercises.isEmpty()) {
            // Ensure currentExcercise is within bounds
            if (currentExcercise < dayExcercises.size()) {
                // Show the first exercise
                List<Excercise> exercises = dayExcercises.get(currentExcercise).getExcercises();
                if (!exercises.isEmpty()) {
                    showExerciseDetails(exercises.get(0));
                    // Show the Next button if there are more exercises
                    nextButton.setVisible(exercises.size() > 1);
                    backButton.setVisible(false); // Initially hide the back button
                }
            }
        }

    }


    private void showExerciseDetails(Excercise exercise) {
        exerciseNameLabel.setText(exercise.getExcerciseName());
        repetitionsTextArea.setText(String.valueOf(exercise.getReps()));
        seriesTextArea.setText(String.valueOf(exercise.getSeries()));
        timeTextArea.setText(String.valueOf(exercise.getTime()));

        // Set the image (assuming gifURL points to an image)
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
        List<DayExcercise> dayExcercises = getDayExerciseForDate(LocalDate.now());
        if (currentExcercise < dayExcercises.size() - 1) {
            currentExcercise++;
            List<Excercise> exercises = dayExcercises.get(currentExcercise).getExcercises();
            if (!exercises.isEmpty()) {
                showExerciseDetails(exercises.get(0));
            }
        }

        // Hide next button if there's no next exercise
        nextButton.setVisible(currentExcercise < dayExcercises.size() - 1);
        backButton.setVisible(currentExcercise > 0);  // Show the back button if we're not at the first exercise
    }

    @FXML
    private void onBackButtonClicked() {
        List<DayExcercise> dayExcercises = getDayExerciseForDate(LocalDate.now());

        if (currentExcercise > 0) {
            currentExcercise--;
            List<Excercise> exercises = dayExcercises.get(currentExcercise).getExcercises();
            if (!exercises.isEmpty()) {
                showExerciseDetails(exercises.get(0));
            }
        }

        // Show the next button if there's another exercise
        nextButton.setVisible(currentExcercise < dayExcercises.size() - 1);
        backButton.setVisible(currentExcercise > 0);  // Show the back button if we're not at the first exercise
    }
}
