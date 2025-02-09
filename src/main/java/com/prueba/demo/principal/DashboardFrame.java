package com.prueba.demo.principal;

import com.prueba.demo.model.*;
import com.prueba.demo.repository.*;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

@Component
public class DashboardFrame {

    //VBox Principal
    @FXML
    private VBox rootPane;

    //Side Menu
    @FXML
    private VBox menuVbox;
    @FXML
    private Label nutriappLabel;
    @FXML
    private ImageView nutriappImage;
    @FXML
    private Button dashboardButton;
    @FXML
    private Button profileButton;
    @FXML
    private Button dietButton;
    @FXML
    private Button exerciseButton;
    @FXML
    private Button reportsButton;

    //Dashboard Pane
    @FXML
    private VBox dashboardPane;
    @FXML
    private ProgressBar caloriesProgressBar;
    @FXML
    private ProgressBar proteinsProgressBar;
    @FXML
    private ProgressBar fatsProgressBar;
    @FXML
    private ProgressBar carbohydratesProgressBar;
    @FXML
    private ProgressBar caloriesBurnedProgressBar;
    @FXML
    private ProgressBar timeActivityProgressBar;
    @FXML
    private Tooltip caloriesTooltip;
    @FXML
    private Tooltip proteinsTooltip;
    @FXML
    private Tooltip fatsTooltip;
    @FXML
    private Tooltip carbohydratesTooltip;
    @FXML
    private Tooltip caloriesBurnedTooltip;
    @FXML
    private Tooltip timeActivityTooltip;

    @FXML
    private GridPane gridPaneDashboard;
    @FXML
    private Label waterLabel;

    //ProfilePane
    @FXML
    private VBox profilePane;
    @FXML
    private Label userNameLabel;
    @FXML
    private TextArea ageTextArea;
    @FXML
    private TextArea sexTextArea;
    @FXML
    private TextArea heightTextArea;
    @FXML
    private TextArea weightTextArea;
    @FXML
    private TextArea allergiesTextArea;
    @FXML
    private TextArea abdomenTextArea;
    @FXML
    private TextArea hipTextArea;
    @FXML
    private TextArea waistTextArea;
    @FXML
    private TextArea neckTextArea;
    @FXML
    private TextArea armTextArea;
    @FXML
    private TextArea chestTextArea;

    @FXML
    private Label ageErrorLabel;
    @FXML
    private Label heightErrorLabel;
    @FXML
    private Label weightErrorLabel;
    @FXML
    private Label abdomenErrorLabel;
    @FXML
    private Label hipErrorLabel;
    @FXML
    private Label waistErrorLabel;
    @FXML
    private Label neckErrorLabel;
    @FXML
    private Label armErrorLabel;
    @FXML
    private Label chestErrorLabel;
    @FXML
    private Label updateLabel;
    @FXML
    private Button updateButton;

    //DietPane
    @FXML
    private VBox dietPane;
    @FXML
    private Button setYourPreferencesButtonDiet;
    @FXML
    private GridPane gridPaneDiet;
    @FXML
    private Label preferencesLabel;

    @FXML
    private HBox dietPaneConfig;

    //ExercisePane
    @FXML
    private VBox exercisePane;
    @FXML
    private Button setYourPreferencesButtonExercise;
    @FXML
    private GridPane gridPaneExercise;
    @FXML
    private GridPane gridPaneRutine;  //<------ Este GridPane es el que debe de aparecer y desaparecer

    //Reports Pane
    @FXML
    private VBox reportsPane;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private HBox infoReportHbox;
    @FXML
    private HBox errorReportHbox;
    @FXML
    private HBox successReportHbox;
    @FXML
    private TextArea ageReportTextArea;
    @FXML
    private TextArea sexReportTextArea;
    @FXML
    private TextArea heightReportTextArea;
    @FXML
    private TextArea weightReportTextArea;
    @FXML
    private TextArea allergiesReportTextArea;
    @FXML
    private TextArea abdomenReportTextArea;
    @FXML
    private TextArea hipReportTextArea;
    @FXML
    private TextArea waistReportTextArea;
    @FXML
    private TextArea neckReportTextArea;
    @FXML
    private TextArea armReportTextArea;
    @FXML
    private TextArea chestReportTextArea;
    @FXML
    private Button sendReportButton;


    @FXML
    private void initialize() {
        rootPane.setMinWidth(900);  // Ancho mínimo
        rootPane.setMinHeight(520); // Alto mínimo

        // Asigna eventos a botones
        profilePane.setVisible(false);
        dietPane.setVisible(false);
        exercisePane.setVisible(false);
        reportsPane.setVisible(false);

        // Crear tooltips para cada ProgressBar
        Tooltip caloriesTooltip = new Tooltip("Progreso de calorías consumidas.");
        Tooltip proteinsTooltip = new Tooltip("Progreso de proteínas consumidas.");
        Tooltip fatsTooltip = new Tooltip("Progreso de grasas consumidas.");
        Tooltip carbohydratesTooltip = new Tooltip("Progreso de carbohidratos consumidos.");
        Tooltip caloriesBurnedTooltip = new Tooltip("Progreso de calorías quemadas.");
        Tooltip timeActivityTooltip = new Tooltip("Tiempo total de actividad.");

        // Asociar cada Tooltip con su ProgressBar
        setTooltipForProgressBar(caloriesProgressBar, caloriesTooltip);
        setTooltipForProgressBar(proteinsProgressBar, proteinsTooltip);
        setTooltipForProgressBar(fatsProgressBar, fatsTooltip);
        setTooltipForProgressBar(carbohydratesProgressBar, carbohydratesTooltip);
        setTooltipForProgressBar(caloriesBurnedProgressBar, caloriesBurnedTooltip);
        setTooltipForProgressBar(timeActivityProgressBar, timeActivityTooltip);

        // Asociar acciones a botones
        profileButton.setOnAction(event -> showProfile());
        dashboardButton.setOnAction(event -> showDashboard());
        dietButton.setOnAction(event -> showDiet());
        exerciseButton.setOnAction(event -> showExercise());
        reportsButton.setOnAction(event -> showReports());



        // Ajustar tamaño de fuente basado en el tamaño de la ventana
        List<Button> buttons = Arrays.asList(dashboardButton, exerciseButton, dietButton, reportsButton, profileButton);

        for (Button button : buttons) {
            button.sceneProperty().addListener((obs, oldScene, newScene) -> {
                if (newScene != null) {
                    newScene.widthProperty().addListener((obs2, oldVal, newVal) -> {
                        double newSize = newVal.doubleValue() * 0.012; // Ajusta el tamaño relativo al ancho
                        Font currentFont = button.getFont();
                        button.setFont(Font.font(currentFont.getFamily(), FontWeight.BOLD, newSize));
                    });
                }
            });
        }




        nutriappLabel.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.widthProperty().addListener((obs2, oldVal, newVal) -> {
                    double newSize = newVal.doubleValue() * 0.012; // Ajusta el porcentaje según necesites
                    String existingStyle = nutriappLabel.getStyle(); // Mantiene los estilos anteriores

                    // Asegura que el estilo tenga font-weight: bold sin duplicarlo
                    if (!existingStyle.contains("-fx-font-weight: bold")) {
                        existingStyle += "; -fx-font-weight: bold";
                    }
                    nutriappLabel.setStyle(existingStyle + "; -fx-font-size: " + newSize + "px;");
                });
            }
        });
        nutriappImage.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                // Listener para el cambio de tamaño de la escena (ancho)
                newScene.widthProperty().addListener((obs2, oldVal, newVal) -> {
                    // Actualiza la imagen según el tamaño de la ventana
                    updateImage(newVal.doubleValue(), newScene.getHeight());
                });

                // Listener para el cambio de tamaño de la escena (alto)
                newScene.heightProperty().addListener((obs2, oldVal, newVal) -> {
                    // Actualiza la imagen según el tamaño de la ventana
                    updateImage(newScene.getWidth(), newVal.doubleValue());
                });
            }
        });


        //Dieta
        setYourPreferencesButtonDiet.setOnAction(event -> openSetYourPreferencesDiet());



        //Profile
        //Click boton de profile

        updateButton.setOnAction(event -> {
            try {
                if (validateFields()) {
                    completeProfile();
                } else{
                    return;
                }
                updateLabel.setStyle("-fx-text-fill: #7DA12D;");
                updateLabel.setText("Su perfil ha sido actualizado con éxito");
                updateLabel.setVisible(true);

                PauseTransition pause = new PauseTransition(Duration.seconds(3));
                pause.setOnFinished(eventPause -> {
                    FadeTransition fadeOut = new FadeTransition(Duration.seconds(3), updateLabel);
                    fadeOut.setFromValue(1.0);
                    fadeOut.setToValue(0.0);
                    fadeOut.setOnFinished(e -> updateLabel.setVisible(false));
                    fadeOut.play();
                });
                pause.play();
            } catch (Exception ex) {
                updateLabel.setStyle("-fx-text-fill: #b30000;");
                updateLabel.setText("Ha ocurrido un error al actualizar datos. Por favor, inténtelo nuevamente");
                updateLabel.setVisible(true);
                PauseTransition pause = new PauseTransition(Duration.seconds(3));
                pause.setOnFinished(eventPause -> {
                    FadeTransition fadeOut = new FadeTransition(Duration.seconds(3), updateLabel);
                    fadeOut.setFromValue(1.0);
                    fadeOut.setToValue(0.0);
                    fadeOut.setOnFinished(e -> updateLabel.setVisible(false));
                    fadeOut.play();
                });
                pause.play();

            }
        });

        //report

        // Restringir que endDatePicker no pueda seleccionar fechas futuras
        endDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (date.isAfter(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #d3d3d3;");
                }
            }
        });
    }


    //Funcionalidades visuales
    @FXML
    private void mouseEntered(javafx.scene.input.MouseEvent event) {
        // Aquí puedes cambiar el color de fondo del HBox, por ejemplo
        Node node = (Node) event.getSource();  // Obtiene la referencia al HBox clickeado

        node.setStyle("-fx-background-color: #404040;");  // Cambia el color de fondo a gris
    }
    @FXML
    private void mouseExited(javafx.scene.input.MouseEvent event) {
        // Aquí puedes cambiar el color de fondo del HBox, por ejemplo
        Node node = (Node) event.getSource();  // Obtiene la referencia al HBox clickeado
        node.setStyle("-fx-background-color: #262626;");  // Cambia el color de fondo a gris
    }
    private void setTooltipForProgressBar(ProgressBar progressBar, Tooltip tooltip) {
        Tooltip.install(progressBar, tooltip); // Instalar el Tooltip en la ProgressBar

        progressBar.setOnMouseEntered(event -> {
            if (!tooltip.isShowing()) {
                tooltip.show(progressBar, event.getScreenX(), event.getScreenY() + 10);
            }
        });

        progressBar.setOnMouseExited(event -> {
            if (tooltip.isShowing()) {
                tooltip.hide();
            }
        });
    }


    @FXML
    private void showDashboard() {
        hideAll();
        dashboardPane.setVisible(true);
        menuVbox.setVisible(true);
    }


    /**
     profile
     */
    @Autowired
    AccountDataRepository accountDataRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    FoodRepository foodRepository;

    @Autowired
    AccountAllergyFoodRepository accountAllergyFoodRepository;

    @FXML
    private void showProfile() {
        hideAll();
        profilePane.setVisible(true);
        menuVbox.setVisible(true);

        Optional<Account> account = accountRepository.findById(1L);
        AccountData accountData = account.get().getAccountData();

        // Hacer invisible las cosas
        updateLabel.setVisible(false);
        userNameLabel.setText(account.get().getName());
        ageErrorLabel.setVisible(false);
        heightErrorLabel.setVisible(false);
        weightErrorLabel.setVisible(false);
        abdomenErrorLabel.setVisible(false);
        hipErrorLabel.setVisible(false);
        waistErrorLabel.setVisible(false);
        neckErrorLabel.setVisible(false);
        armErrorLabel.setVisible(false);
        chestErrorLabel.setVisible(false);

        // Actualizar los campos del perfil
        updateProfileFields(accountData);
    }


    private void updateProfileFields(AccountData accountData) {
        sexTextArea.setText(accountData.getGender() != null && accountData.getGender() ? "Masculino" : "Femenino");
        ageTextArea.setText(String.valueOf(accountData.getAge()));
        heightTextArea.setText(String.valueOf(accountData.getHeight()));
        weightTextArea.setText(String.valueOf(accountData.getWeight()));
        abdomenTextArea.setText(String.valueOf(accountData.getAbdomen()));
        hipTextArea.setText(String.valueOf(accountData.getHips()));
        waistTextArea.setText(String.valueOf(accountData.getWaist()));
        chestTextArea.setText(String.valueOf(accountData.getChest()));
        neckTextArea.setText(String.valueOf(accountData.getNeck()));
        armTextArea.setText(String.valueOf(accountData.getArm()));

        // Actualizar alergias
        List<String> allergicFoodNames = accountAllergyFoodRepository.findFoodNamesByAccountDataId(accountData.getId());
        if (allergicFoodNames.isEmpty()){
            allergiesTextArea.setText("Ninguna");
        } else {
            allergiesTextArea.setText(String.join(", ", allergicFoodNames));
        }

        // Hacer que los campos de sexo y alergias sean de solo lectura
        sexTextArea.setEditable(false);
        allergiesTextArea.setEditable(false);
    }

    //Lo mismo que hay en ProfileFrame, ligeramente cambiado

    private void completeProfile() {
        Optional<Account> accountOpt = accountRepository.findById(1L);

        if (accountOpt.isPresent()) {
            Account account = accountOpt.get();

            AccountData accountData = account.getAccountData();

            if (accountData == null) {
                accountData = new AccountData();
                accountData.setAccount(account);
            }

            accountData.setGender(!sexTextArea.getText().equals("Femenino"));
            accountData.setAge(Integer.parseInt(ageTextArea.getText().trim()));
            accountData.setHeight(Double.parseDouble(heightTextArea.getText().trim()));
            accountData.setWeight(Double.parseDouble(weightTextArea.getText().trim()));
            accountData.setAbdomen(parseOrDefault(abdomenTextArea, 0.0));
            accountData.setHips(parseOrDefault(hipTextArea, 0.0));
            accountData.setWaist(parseOrDefault(waistTextArea, 0.0));
            accountData.setArm(parseOrDefault(armTextArea, 0.0));
            accountData.setChest(parseOrDefault(chestTextArea, 0.0));
            accountData.setNeck(parseOrDefault(neckTextArea, 0.0));

            accountDataRepository.save(accountData);

            account.setAccountData(accountData);
            accountRepository.save(account);

        }
    }

    private double parseOrDefault(TextArea textArea, double defaultValue) {
        if (textArea.getText() != null && !textArea.getText().trim().isEmpty()) {
            try {
                return Double.parseDouble(textArea.getText().trim());
            } catch (NumberFormatException e) {
            }
        }
        return defaultValue;
    }

    private boolean validateFields() {
        boolean validInputs = true;

        // Validar edad (13 - 120) (Solo enteros)
        validInputs &= isValidNumber(ageTextArea, 13, 120, "Edad", ageErrorLabel, 0, true);

        // Validar estatura (90 - 300 cm)
        validInputs &= isValidNumber(heightTextArea, 90, 300, "Estatura", heightErrorLabel, 0, false);

        // Validar peso inicial (30 - 300 kg)
        validInputs &= isValidNumber(weightTextArea, 30, 300, "Peso inicial", weightErrorLabel, 0, false);

        // Validar abdomen (90 - 300 cm)
        validInputs &= isValidNumber(abdomenTextArea, 90, 300, "Abdomen", abdomenErrorLabel, 1, false);

        // Validar cadera (30 - 300 cm)
        validInputs &= isValidNumber(hipTextArea, 30, 300, "Cadera", hipErrorLabel, 1, false);

        // Validar cintura (90 - 300 cm)
        validInputs &= isValidNumber(waistTextArea, 90, 300, "Cintura", waistErrorLabel, 1, false);

        // Validar cuello (90 - 300 cm)
        validInputs &= isValidNumber(neckTextArea, 90, 300, "Cuello", neckErrorLabel, 1, false);

        // Validar brazo (30 - 300 cm)
        validInputs &= isValidNumber(armTextArea, 30, 300, "Brazo", armErrorLabel, 1, false);

        // Validar pecho (90 - 300 cm)
        validInputs &= isValidNumber(chestTextArea, 90, 300, "Pecho", chestErrorLabel, 1, false);

        return validInputs;
    }

    private boolean isValidNumber(TextArea textArea, double min, double max, String fieldName, Label label, int type, boolean isInteger) {
        String text = textArea.getText().trim();

        if (type == 1 && (text.isEmpty() || text.equals("0.0"))) {
            label.setVisible(false);
            return true;
        }

        if (text.isEmpty()) {
            label.setText("El campo es obligatorio");
            label.setVisible(true);
            return false;
        }

        try {
            // Validar como entero o como double según el parámetro `isInteger`
            double value = isInteger ? Integer.parseInt(text) : Double.parseDouble(text);

            // Verificar si está en el rango permitido
            if (value < min || value > max) {
                label.setText(fieldName + " debe estar entre " + min + " y " + max + ".");
                label.setVisible(true);
                return false;
            }

            label.setVisible(false);
            return true;

        } catch (NumberFormatException e) {
            label.setText("Por favor, introduce un valor válido.");
            label.setVisible(true);
            return false;
        }
    }

    /**
     dieta
     */


    @FXML
    private void showDiet() {
        hideAll();
        dietPane.setVisible(true);
        menuVbox.setVisible(true);

        LocalDate today = LocalDate.now();
        int dayOfWeek = today.getDayOfWeek().getValue();

        for (int row = 1; row <= 4; row++) {
            for (int col = 1; col <= 5; col++) {
                int daysOffset = col - 1;
                LocalDate targetDate = today.minusDays(dayOfWeek - 1).plusDays(daysOffset);



                DayMeal dayMeal = getDayMealForDate(Date.valueOf(targetDate).toLocalDate());
                Food foodForCell = getFoodForCell(dayMeal, row);


                Button button = new Button();
                button.setMaxWidth(Double.MAX_VALUE);
                button.setMaxHeight(Double.MAX_VALUE);

                if (foodForCell != null) {
                    String imagePath = foodForCell.getPhoto().getThumb();


                    if (imagePath != null && !imagePath.isEmpty()) {
                        Image foodImage = new Image(imagePath, true); // Load in background if needed
                        ImageView imageView = new ImageView(foodImage);

                        // *** KEY CHANGE 1:  Fit to button size, NO preserve ratio ***
                        imageView.setFitWidth(60);
                        imageView.setFitHeight(60);
                        imageView.setPreserveRatio(false); // <--- Crucial change


                        button.setGraphic(imageView);
                        button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY); // Important for correct sizing

                    } else {
                        System.out.println("Invalid or missing image path: " + imagePath);
                    }
                }

                gridPaneDiet.add(button, col, row);

                // Key Change 2: Ensure GridPane constraints resize buttons
                GridPane.setHgrow(button, Priority.ALWAYS);
                GridPane.setVgrow(button, Priority.ALWAYS);


                int finalRow = row;
                int finalCol = col;
                button.setOnMouseClicked(event -> {
                    Button clickedButton = (Button) event.getSource();
                    if (button.getGraphic() != null) {
                        showNutrimentalInfo(targetDate, finalRow);
                    } else {
                        handleCellClick(clickedButton,finalRow, finalCol);
                    }
                });
            }
        }

        Properties properties = new Properties();
        try (FileInputStream in = new FileInputStream("preferencesState.properties")) {
            properties.load(in);
            if ("true".equals(properties.getProperty("preferencesCompleted"))) {
                setYourPreferencesButtonDiet.setVisible(false);
                dietPaneConfig.setVisible(false);
            }
        } catch (IOException e) {
            disableGridPane(gridPaneDiet);
            //e.printStackTrace();
        }



    }

    private Stage nutrimentalInfoStage;
    private void showNutrimentalInfo(LocalDate targetDate, int foodType) {
        if (nutrimentalInfoStage != null && nutrimentalInfoStage.isShowing()) {
            nutrimentalInfoStage.toFront(); // Bring the existing window to the front
            return;
        }



        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/PlantillasFXML/NutrimentalInfo.fxml"));
                loader.setControllerFactory(applicationContext::getBean);

                Scene scene = new Scene(loader.load(), 600, 600); // Limitar tamaño de la escena

                nutrimentalInfoStage = new Stage();
                nutrimentalInfoStage.setTitle("Principal");
                nutrimentalInfoStage.setScene(scene);

                NutrimentalInfo nutrimentalInfo = loader.getController();

                nutrimentalInfo.setInfo(targetDate, foodType);


                nutrimentalInfoStage.setOnCloseRequest(event -> nutrimentalInfoStage = null); // Reset when closed

                nutrimentalInfoStage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


    }


    @Autowired
    DayMealRepository dayMealRepository;
    private DayMeal getDayMealForDate(LocalDate targetDate) {
        return dayMealRepository.findByDate(java.sql.Date.valueOf(targetDate));
    }

    private Food getFoodForCell(DayMeal dayMeal, int row) {
        if (dayMeal != null) {
            switch (row) {
                case 1: // Breakfast
                    if (!dayMeal.getBreakfast().isEmpty()) {
                        System.out.println("Breakfast Found: " + dayMeal.getBreakfast().get(0).getFoodName()); // Log breakfast info
                    }
                    return dayMeal.getBreakfast().isEmpty() ? null : dayMeal.getBreakfast().get(0);
                case 2: // Lunch
                    return dayMeal.getLunch().isEmpty() ? null : dayMeal.getLunch().get(0);
                case 3: // Dinner
                    if (!dayMeal.getDinner().isEmpty()) {
                        System.out.println("Dinner Found: " + dayMeal.getDinner().get(0).getFoodName()); // Log dinner info
                    }
                    return dayMeal.getDinner().isEmpty() ? null : dayMeal.getDinner().get(0);
                case 4: // Snack
                    return dayMeal.getSnack().isEmpty() ? null : dayMeal.getSnack().get(0);
                default:
                    return null;
            }
        }
        return null;
    }


    private static SelectYourFood selectYourFoodController; // Static instance
    private static Stage selectYourFoodStage;

    private void handleCellClick(Button clickedButton, int row, int col) {
        Platform.runLater(() -> {
            try {
                if (selectYourFoodStage == null) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/PlantillasFXML/SelectYourFood.fxml"));
                    loader.setControllerFactory(applicationContext::getBean);

                    Scene scene = new Scene(loader.load());
                    selectYourFoodController = loader.getController();
                    selectYourFoodController.setCol(col);
                    selectYourFoodController.setRow(row);

                    selectYourFoodStage = new Stage();
                    selectYourFoodStage.setTitle("Principal");
                    selectYourFoodStage.setScene(scene);
                    selectYourFoodStage.setMinWidth(900);
                    selectYourFoodStage.setMinHeight(520);
                    selectYourFoodStage.setOnCloseRequest(event -> {
                        selectYourFoodStage = null;
                        selectYourFoodController = null;
                    });

                    selectYourFoodStage.show();

                } else {
                    selectYourFoodStage.toFront();
                    selectYourFoodController.setCol(col);
                    selectYourFoodController.setRow(row);
                }

                // Handle the button logic here
                if (clickedButton.getGraphic() != null) {
                    // Reset the graphic if needed
                    clickedButton.setGraphic(null);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }




    private void disableGridPane(GridPane gridPane) {
        for (Node node : gridPane.getChildren()) {
            if (node instanceof Control) {
                ((Control) node).setDisable(true);
            }
        }
    }

    public void hidePreferencesUI() {
        setYourPreferencesButtonDiet.setVisible(false);
        preferencesLabel.setVisible(false);
    }

    private Stage preferencesDietStage;

    private void openSetYourPreferencesDiet() {
        if (preferencesDietStage != null && preferencesDietStage.isShowing()) {
            preferencesDietStage.toFront(); // Bring the existing window to the front
            return;
        }

        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/PlantillasFXML/SetYourPreferencesDiet.fxml"));
                loader.setControllerFactory(applicationContext::getBean);

                Scene scene = new Scene(loader.load(), 600, 360); // Limitar tamaño de la escena

                preferencesDietStage = new Stage();
                preferencesDietStage.setTitle("Principal");
                preferencesDietStage.setScene(scene);

                // Establecer límites para la ventana
                preferencesDietStage.setMinWidth(600);
                preferencesDietStage.setMinHeight(360);
                preferencesDietStage.setMaxWidth(600);
                preferencesDietStage.setMaxHeight(360);

                preferencesDietStage.setOnCloseRequest(event -> preferencesDietStage = null); // Reset when closed

                preferencesDietStage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }










    /**
     ejercicio
     */
    @FXML
    private void showExercise() {
        hideAll();
        exercisePane.setVisible(true);
        menuVbox.setVisible(true);

    }

    /**
     reportes
     */

    @FXML
    private void showReports() {
        hideAll();
        reportsPane.setVisible(true);
        menuVbox.setVisible(true);

        Optional<Account> account = accountRepository.findById(1L);
        AccountData accountData = account.get().getAccountData();

        sexReportTextArea.setText(accountData.getGender() != null && accountData.getGender() ? "Masculino" : "Femenino");
        ageReportTextArea.setText(String.valueOf(accountData.getAge()));
        heightReportTextArea.setText(String.valueOf(accountData.getHeight()));
        weightReportTextArea.setText(String.valueOf(accountData.getWeight()));
        abdomenReportTextArea.setText(String.valueOf(accountData.getAbdomen()));
        hipReportTextArea.setText(String.valueOf(accountData.getHips()));
        waistReportTextArea.setText(String.valueOf(accountData.getWaist()));
        chestReportTextArea.setText(String.valueOf(accountData.getChest()));
        neckReportTextArea.setText(String.valueOf(accountData.getNeck()));
        armReportTextArea.setText(String.valueOf(accountData.getArm()));

        // Actualizar alergias
        List<String> allergicFoodNames = accountAllergyFoodRepository.findFoodNamesByAccountDataId(accountData.getId());
        if (allergicFoodNames.isEmpty()){
            allergiesReportTextArea.setText("Ninguna");
        } else {
            allergiesReportTextArea.setText(String.join(", ", allergicFoodNames));
        }

        sendReportButton.setOnAction(event -> {
            if (validateFieldsReport()) {
                sendReport();
            }
        });

        disableVBox(reportsPane);


    }

    private void sendReport() {
    }

    private boolean validateFieldsReport() {
        if (startDatePicker.getValue() == null) {
            errorReportHbox.setVisible(true);
            return false;
        }
        if (endDatePicker.getValue() == null) {
            errorReportHbox.setVisible(true);
            return false;
        }

        if (endDatePicker.getValue().isBefore(startDatePicker.getValue())) {
            errorReportHbox.setVisible(true);
            return false;
        }

        errorReportHbox.setVisible(false);
        return true;

    }

    private void disableVBox(VBox reportsPane) {
        for (Node node : reportsPane.getChildren()) {
            if (node instanceof Control) {
                ((Control) node).setDisable(true);
            }
        }

    }

    private void hideAll() {
        dashboardPane.setVisible(false);
        profilePane.setVisible(false);
        dietPane.setVisible(false);
        exercisePane.setVisible(false);
        reportsPane.setVisible(false);
    }

    private void updateImage(double width, double height) {
        String imagePath;
        if (width < 400) {
            imagePath = "images/NutriApp64x64.png";
        } else if (width < 800) {
            imagePath = "images/NutriApp64x64.png";
        } else if (width < 1200) {
            imagePath = "images/NutriApp128x128.png";
        } else {
            imagePath = "images/NutriApp256x256.png";
        }

        Image image = new Image(imagePath);
        nutriappImage.setImage(image);

        // Ajustar el tamaño de la imagen según el ancho y alto de la ventana
        double newWidth = width * 0.12; // Ajuste proporcional según el ancho
        double newHeight = height * 0.12; // Ajuste proporcional según la altura

        nutriappImage.setFitWidth(newWidth);
        nutriappImage.setFitHeight(newHeight);
    }

    @Autowired
    private ApplicationContext applicationContext;



//-----ESTO ES PARA LA TABLA DE DIETA CHECAR SI ESTA CORRECTO-----


}


