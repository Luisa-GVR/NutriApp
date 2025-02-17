package com.prueba.demo.principal;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.prueba.demo.model.*;
import com.prueba.demo.repository.*;
import com.prueba.demo.service.APIConsumption;
import com.prueba.demo.service.IEmailService;
import com.prueba.demo.service.dto.EmailDTO;
import jakarta.mail.Authenticator;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import org.hibernate.annotations.Check;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.*;
import java.sql.Date;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
    private Label endWeekLabel;
    @FXML
    private Label weekStartLabel;
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
    private HBox preferencesHBox;
    @FXML
    private HBox dietPaneConfig;


    @FXML
    private ChoiceBox<String> choiceBox1;

    @FXML
    private ChoiceBox<String> choiceBox2;

    @FXML
    private ChoiceBox<String> choiceBox3;

    @FXML
    private ChoiceBox<String> choiceBox4;

    @FXML
    private ChoiceBox<String> choiceBox5;

    @FXML
    private StackPane gridPane11, gridPane12, gridPane13, gridPane14, gridPane15,
            gridPane21, gridPane22, gridPane23, gridPane24, gridPane25,
            gridPane31, gridPane32, gridPane33, gridPane34, gridPane35,
            gridPane41, gridPane42, gridPane43, gridPane44, gridPane45,
            gridPane51, gridPane52, gridPane53, gridPane54, gridPane55;
    //ExercisePane
    @FXML
    private VBox exercisePane;
    @FXML
    public HBox excerciseHbox;
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

    /*
        iniciar con datos
     */

    public void uploadFoodsFromCSV() {
        // Verificar y actualizar el estado de creación de la base de datos
        File propertiesFile = new File("preferencesState.properties");
        Properties properties = new Properties();

        try {
            if (!propertiesFile.exists()) {
                propertiesFile.createNewFile();
            }

            try (InputStream inputStream = new FileInputStream(propertiesFile)) {
                properties.load(inputStream);
            }

            String createdDatabaseValue = properties.getProperty("createdDatabase", "false");

            if ("false".equals(createdDatabaseValue)) {
                // Subir los alimentos desde el CSV
                try (CSVReader reader = new CSVReader(new FileReader("src/main/resources/foods.csv"))) {
                    // Leer el archivo CSV
                    List<String[]> rows = reader.readAll();

                    for (String[] row : rows) {
                        if (row.length >= 8) {
                            Food food = new Food();
                            food.setFoodName(row[0]);
                            food.setCalories(Double.parseDouble(row[1]));
                            food.setProtein(Double.parseDouble(row[2]));
                            food.setTotalCarbohydrate(Double.parseDouble(row[3]));
                            food.setTotalFat(Double.parseDouble(row[4]));
                            food.setPortionWeight(Double.parseDouble(row[5]));
                            List<Integer> newMealTypes = List.of(Integer.parseInt(row[6]));
                            food.setMealType(newMealTypes);
                            food.setPhoto(new Photo().setThumb(row[7]));

                            // Buscar si el alimento ya existe en la base de datos
                            Optional<Food> existingFoodOptional = foodRepository.findByFoodName(food.getFoodName());

                            if (existingFoodOptional.isPresent()) {
                                Food existingFood = existingFoodOptional.get();

                                // Verificar si el nuevo mealType ya está en la lista de mealTypes de la comida existente
                                if (!existingFood.getMealType().containsAll(newMealTypes)) {
                                    // Si no existe, agregar el nuevo mealType
                                    existingFood.getMealType().addAll(newMealTypes);
                                    foodRepository.save(existingFood); // Guardar la comida existente con el nuevo mealType
                                }
                            } else {
                                // Si no existe, guardar el nuevo alimento
                                foodRepository.save(food);
                            }
                        }
                    }

                    properties.setProperty("createdDatabase", "true");

                    // Guardamos los cambios en el archivo de propiedades
                    try (OutputStream outputStream = new FileOutputStream(propertiesFile)) {
                        properties.store(outputStream, null);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (CsvException e) {
                    throw new RuntimeException(e);
                }
            } else {
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    @FXML
    private void initialize() {
        Properties properties = new Properties();
        loadProperties(properties);

        if ("true".equals(properties.getProperty("preferencesExerciseCompleted"))) {
            // Ocultar el botón de configuración de preferencias si ya se ha completado el ejercicio
            excerciseHbox.setVisible(false);  // O usar setDisable(true) si prefieres deshabilitar el botón
        }


        rootPane.setMinWidth(900);  // Ancho mínimo
        rootPane.setMinHeight(520); // Alto mínimo

        // Asigna eventos a botones
        profilePane.setVisible(false);
        dietPane.setVisible(false);
        exercisePane.setVisible(false);
        reportsPane.setVisible(false);
        dashboardPane.setVisible(false);



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

        /*
            Quitar comentario cuando


         */



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




        showDashboard();

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

    /**
     Dashboard
     */
    @Autowired
    APIConsumption apiConsumption;

    @FXML
    private void showDashboard() {
        hideAll();
        dashboardPane.setVisible(true);
        menuVbox.setVisible(true);

        Platform.runLater(() -> {
            /**
             TODA LA INFORMACION MOSTRADA AQUI ES POR SEMANA DE LUNES A VIERNES!!!
             */

            Optional<AccountData> accountData = accountDataRepository.findByAccountId(1L);
            LocalDate today = LocalDate.now();
            Date monday = Date.valueOf(today.with(DayOfWeek.MONDAY));
            Date friday = Date.valueOf(today.with(DayOfWeek.FRIDAY));

            List<Report> weeklyReport = reportRepository.findReportsForWeek(1L, monday, friday);
            double consumedCalories = 0;
            double consumedProtein = 0;
            double consumedFat = 0;
            double consumedCarbs = 0;

            double totalCalories = calculateCalories(1L) * 5;
            double totalProtein = calculateProteins() *5;
            double totalFat = calculateFats()*5;
            double totalCarbs = calculateCarbs()*5;

            String mondayFood = "";
            String tuesdayFood = "";
            String wednesdayFood = "";
            String thursdayFood = "";
            String fridayFood = "";

            if (weeklyReport == null || weeklyReport.isEmpty()) {

                consumedCalories = 0;
                consumedProtein = 0;
                consumedFat = 0;
                consumedCarbs = 0;
            } else {
                for (int i = 0; i < weeklyReport.size(); i++) {
                    for (int j = 0; j < weeklyReport.get(i).getDayMeals().getBreakfast().size(); j++) {
                        consumedCalories += weeklyReport.get(i).getDayMeals().getBreakfast().get(j).getCalories();
                        consumedCarbs += weeklyReport.get(i).getDayMeals().getBreakfast().get(j).getTotalCarbohydrate();
                        consumedFat += weeklyReport.get(i).getDayMeals().getBreakfast().get(j).getTotalFat();
                        consumedProtein += weeklyReport.get(i).getDayMeals().getBreakfast().get(j).getProtein();
                        String breakfastFoodName = weeklyReport.get(i).getDayMeals().getBreakfast().get(j).getFoodName();
                        breakfastFoodName = capitalizeFirstLetter(breakfastFoodName);
                        switch (i) {
                            case 0:
                                mondayFood += breakfastFoodName + ", ";
                                break;
                            case 1:
                                tuesdayFood += breakfastFoodName + ", ";
                                break;
                            case 2:
                                wednesdayFood += breakfastFoodName + ", ";
                                break;
                            case 3:
                                thursdayFood += breakfastFoodName + ", ";
                                break;
                            case 4:
                                fridayFood += breakfastFoodName + ", ";
                                break;
                        }
                    }

                    for (int j = 0; j < weeklyReport.get(i).getDayMeals().getLunch().size(); j++) {
                        consumedCalories += weeklyReport.get(i).getDayMeals().getLunch().get(j).getCalories();
                        consumedCarbs += weeklyReport.get(i).getDayMeals().getLunch().get(j).getTotalCarbohydrate();
                        consumedFat += weeklyReport.get(i).getDayMeals().getLunch().get(j).getTotalFat();
                        consumedProtein += weeklyReport.get(i).getDayMeals().getLunch().get(j).getProtein();
                        String lunchFoodName = weeklyReport.get(i).getDayMeals().getLunch().get(j).getFoodName();
                        lunchFoodName = capitalizeFirstLetter(lunchFoodName);
                        switch (i) {
                            case 0:
                                mondayFood += lunchFoodName + ", ";
                                break;
                            case 1:
                                tuesdayFood += lunchFoodName + ", ";
                                break;
                            case 2:
                                wednesdayFood += lunchFoodName + ", ";
                                break;
                            case 3:
                                thursdayFood += lunchFoodName + ", ";
                                break;
                            case 4:
                                fridayFood += lunchFoodName + ", ";
                                break;
                        }
                    }

                    for (int j = 0; j < weeklyReport.get(i).getDayMeals().getDinner().size(); j++) {
                        consumedCalories += weeklyReport.get(i).getDayMeals().getDinner().get(j).getCalories();
                        consumedCarbs += weeklyReport.get(i).getDayMeals().getDinner().get(j).getTotalCarbohydrate();
                        consumedFat += weeklyReport.get(i).getDayMeals().getDinner().get(j).getTotalFat();
                        consumedProtein += weeklyReport.get(i).getDayMeals().getDinner().get(j).getProtein();
                        String dinnerFoodName = weeklyReport.get(i).getDayMeals().getDinner().get(j).getFoodName();
                        dinnerFoodName = capitalizeFirstLetter(dinnerFoodName);
                        switch (i) {
                            case 0:
                                mondayFood += dinnerFoodName + ", ";
                                break;
                            case 1:
                                tuesdayFood += dinnerFoodName + ", ";
                                break;
                            case 2:
                                wednesdayFood += dinnerFoodName + ", ";
                                break;
                            case 3:
                                thursdayFood += dinnerFoodName + ", ";
                                break;
                            case 4:
                                fridayFood += dinnerFoodName + ", ";
                                break;
                        }
                    }

                    for (int j = 0; j < weeklyReport.get(i).getDayMeals().getSnack().size(); j++) {
                        consumedCalories += weeklyReport.get(i).getDayMeals().getSnack().get(j).getCalories();
                        consumedCarbs += weeklyReport.get(i).getDayMeals().getSnack().get(j).getTotalCarbohydrate();
                        consumedFat += weeklyReport.get(i).getDayMeals().getSnack().get(j).getTotalFat();
                        consumedProtein += weeklyReport.get(i).getDayMeals().getSnack().get(j).getProtein();
                        String snackFoodName = weeklyReport.get(i).getDayMeals().getSnack().get(j).getFoodName();
                        snackFoodName = capitalizeFirstLetter(snackFoodName);
                        switch (i) {
                            case 0:
                                mondayFood += snackFoodName + ", ";
                                break;
                            case 1:
                                tuesdayFood += snackFoodName + ", ";
                                break;
                            case 2:
                                wednesdayFood += snackFoodName + ", ";
                                break;
                            case 3:
                                thursdayFood += snackFoodName + ", ";
                                break;
                            case 4:
                                fridayFood += snackFoodName + ", ";
                                break;
                        }
                    }

                    for (int j = 0; j < weeklyReport.get(i).getDayMeals().getOptional().size(); j++) {
                        consumedCalories += weeklyReport.get(i).getDayMeals().getOptional().get(j).getCalories();
                        consumedCarbs += weeklyReport.get(i).getDayMeals().getOptional().get(j).getTotalCarbohydrate();
                        consumedFat += weeklyReport.get(i).getDayMeals().getOptional().get(j).getTotalFat();
                        consumedProtein += weeklyReport.get(i).getDayMeals().getOptional().get(j).getProtein();
                        String optionalFoodName = weeklyReport.get(i).getDayMeals().getOptional().get(j).getFoodName();
                        optionalFoodName = capitalizeFirstLetter(optionalFoodName);
                        switch (i) {
                            case 0:
                                mondayFood += optionalFoodName + ", ";
                                break;
                            case 1:
                                tuesdayFood += optionalFoodName + ", ";
                                break;
                            case 2:
                                wednesdayFood += optionalFoodName + ", ";
                                break;
                            case 3:
                                thursdayFood += optionalFoodName + ", ";
                                break;
                            case 4:
                                fridayFood += optionalFoodName + ", ";
                                break;
                        }
                    }
                }
            }
            if (mondayFood.endsWith(", ")) {
                mondayFood = mondayFood.substring(0, mondayFood.length() - 2);
            }
            if (tuesdayFood.endsWith(", ")) {
                tuesdayFood = tuesdayFood.substring(0, tuesdayFood.length() - 2);
            }
            if (wednesdayFood.endsWith(", ")) {
                wednesdayFood = wednesdayFood.substring(0, wednesdayFood.length() - 2);
            }
            if (thursdayFood.endsWith(", ")) {
                thursdayFood = thursdayFood.substring(0, thursdayFood.length() - 2);
            }
            if (fridayFood.endsWith(", ")) {
                fridayFood = fridayFood.substring(0, fridayFood.length() - 2);
            }

            Label mondayLabel = new Label(mondayFood);
            Label tuesdayLabel = new Label(tuesdayFood);
            Label wednesdayLabel = new Label(wednesdayFood);
            Label thursdayLabel = new Label(thursdayFood);
            Label fridayLabel = new Label(fridayFood);

// Configurar la propiedad de truncamiento en el estilo de cada Label
            mondayLabel.setStyle("-fx-max-width: 150px; -fx-wrap-text: true; -fx-text-overflow: ellipsis;");
            tuesdayLabel.setStyle("-fx-max-width: 150px; -fx-wrap-text: true; -fx-text-overflow: ellipsis;");
            wednesdayLabel.setStyle("-fx-max-width: 150px; -fx-wrap-text: true; -fx-text-overflow: ellipsis;");
            thursdayLabel.setStyle("-fx-max-width: 150px; -fx-wrap-text: true; -fx-text-overflow: ellipsis;");
            fridayLabel.setStyle("-fx-max-width: 150px; -fx-wrap-text: true; -fx-text-overflow: ellipsis;");

// Dieta
            gridPaneDashboard.add(mondayLabel, 0, 1);     // Columna 0, Fila 1
            gridPaneDashboard.add(tuesdayLabel, 1, 1);    // Columna 1, Fila 1
            gridPaneDashboard.add(wednesdayLabel, 2, 1);  // Columna 2, Fila 1
            gridPaneDashboard.add(thursdayLabel, 3, 1);   // Columna 3, Fila 1
            gridPaneDashboard.add(fridayLabel, 4, 1);     // Columna 4, Fila 1

            Label mondayExerciseLabel = new Label(formatExerciseName(accountData.get().getMonday()));
            Label tuesdayExerciseLabel = new Label(formatExerciseName(accountData.get().getTuesday()));
            Label wednesdayExerciseLabel = new Label(formatExerciseName(accountData.get().getWednesday()));
            Label thursdayExerciseLabel = new Label(formatExerciseName(accountData.get().getThursday()));
            Label fridayExerciseLabel = new Label(formatExerciseName(accountData.get().getFriday()));

            gridPaneDashboard.add(mondayExerciseLabel, 0, 2);
            gridPaneDashboard.add(tuesdayExerciseLabel, 1, 2);
            gridPaneDashboard.add(wednesdayExerciseLabel, 2, 2);
            gridPaneDashboard.add(thursdayExerciseLabel, 3, 2);
            gridPaneDashboard.add(fridayExerciseLabel, 4, 2);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy").withLocale(new Locale("es", "MX"));


            String mondayFormatted = monday.toLocalDate().format(formatter);
            String fridayFormatted = friday.toLocalDate().format(formatter);
            weekStartLabel.setText(mondayFormatted);
            endWeekLabel.setText(fridayFormatted);

            waterLabel.setText("Hoy deberías tomar " + calculateWater(1L) + " litros de agua.");

            caloriesProgressBar.setProgress(consumedCalories/totalCalories);
            proteinsProgressBar.setProgress(consumedProtein/totalProtein);
            fatsProgressBar.setProgress(consumedFat/totalFat);
            carbohydratesProgressBar.setProgress(consumedCarbs/totalCarbs);

            // Crear tooltips para cada ProgressBar
            Tooltip caloriesTooltip = new Tooltip("Calorías\n" +
                    "⬜ Meta semanal: " + Math.floor(totalCalories) + "\n" +
                    "🟩 Consumo al día: " + Math.floor(consumedCalories));

            Tooltip proteinsTooltip = new Tooltip("Proteínas\n" +
                    "⬜ Meta semanal: " + Math.floor(totalProtein) + "\n" +
                    "🟩 Consumo al día: " + Math.floor(consumedProtein));

            Tooltip fatsTooltip = new Tooltip("Grasas\n" +
                    "⬜ Meta semanal: " + Math.floor(totalFat) + "\n" +
                    "🟩 Consumo al día: " + Math.floor(consumedFat));

            Tooltip carbohydratesTooltip = new Tooltip("Carbohidratos\n" +
                    "⬜ Meta semanal: " + Math.floor(totalCarbs) + "\n" +
                    "🟩 Consumo al día: " + Math.floor(consumedCarbs));


            Tooltip caloriesBurnedTooltip = new Tooltip("Progreso de calorías quemadas.");

            Tooltip timeActivityTooltip = new Tooltip("Tiempo total de actividad.");


            // Asociar cada Tooltip con su ProgressBar
            setTooltipForProgressBar(caloriesProgressBar, caloriesTooltip);
            setTooltipForProgressBar(proteinsProgressBar, proteinsTooltip);
            setTooltipForProgressBar(fatsProgressBar, fatsTooltip);
            setTooltipForProgressBar(carbohydratesProgressBar, carbohydratesTooltip);
            setTooltipForProgressBar(caloriesBurnedProgressBar, caloriesBurnedTooltip);
            setTooltipForProgressBar(timeActivityProgressBar, timeActivityTooltip);




        });

    }

    public String capitalizeFirstLetter(String foodName) {
        if (foodName == null || foodName.isEmpty()) {
            return foodName;
        }
        return foodName.substring(0, 1).toUpperCase() + foodName.substring(1);
    }

    public double calculateWater(Long accountId) {

        Optional<AccountData> accountDataOpt = accountDataRepository.findByAccountId(accountId);
        double totalWater = (accountDataOpt.get().getWeight() / 35);

        return Math.round(totalWater * 10.0) / 10.0;
    }


    public double calculateCalories(Long accountId) {

        Optional<AccountData> accountDataOpt = accountDataRepository.findByAccountId(accountId);

        if (accountDataOpt.isEmpty()) {
            throw new IllegalArgumentException("");
        }

        AccountData accountData = accountDataOpt.get();

        // Obtener valores de la cuenta
        boolean isMale = accountData.getGender(); // true = hombre, false = mujer
        int age = accountData.getAge();
        double weight = accountData.getWeight();
        double height = accountData.getHeight();
        Goal goal = accountData.getGoal() != null ? accountData.getGoal() : Goal.mantenimiento; // Valor por defecto

        // Cálculo del TMB según Harris-Benedict
        double tmb;


        // Ajustar según el objetivo, actividad de 5 dias por lo que se toma como moderado
        switch (goal) {
            case deficit:
                if (isMale) {
                    tmb = (((10*weight)+(6.25*height)-(5*age)+5)*1.55)*.8;
                } else {
                    tmb = (((10*weight)+(6.25*height)-(5*age)-161)*1.55)*(.8);
                }
                return  tmb;
            case volumen:
                if (isMale) {
                    tmb = (((10*weight)+(6.25*height)-(5*age)+5)*1.55)*1.2;
                } else {
                    tmb = (((10*weight)+(6.25*height)-(5*age)-161)*1.55)*1.2;
                }
                return tmb;
            default: // mantenimiento
                if (isMale) {
                    tmb = (((10*weight)+(6.25*height)-(5*age)+5)*1.55);
                } else {
                    tmb = (((10*weight)+(6.25*height)-(5*age)-161)*1.55);
                }
                return tmb;
        }
    }

    public double calculateProteins() {
        double calories = calculateCalories(1L);
        double proteinPercentage = 0.3; // 30% de proteínas

        return (calories * proteinPercentage) / 4; // 1g de proteína = 4 kcal
    }

    public double calculateFats() {
        double calories = calculateCalories(1L);
        double fatPercentage = 0.25; // 25% de grasas

        return (calories * fatPercentage) / 9; // 1g de grasa = 9 kcal
    }

    public double calculateCarbs() {
        double calories = calculateCalories(1L);
        double carbPercentage = 0.45; // 45% de carbohidratos

        return (calories * carbPercentage) / 4; // 1g de carbohidrato = 4 kcal
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

        // Validar abdomen ((40 - 170 cm)
        validInputs &= isValidNumber(abdomenTextArea, 40, 170, "Abdomen", abdomenErrorLabel, 1, false);

        // Validar cadera (50 - 170 cm)
        validInputs &= isValidNumber(hipTextArea, 50, 170, "Cadera", hipErrorLabel, 1, false);

        // Validar cintura (35 - 170 cm)
        validInputs &= isValidNumber(waistTextArea, 35, 170, "Cintura", waistErrorLabel, 1, false);

        // Validar cuello ((15 - 170 cm)
        validInputs &= isValidNumber(neckTextArea, 15, 170, "Cuello", neckErrorLabel, 1, false);

        // Validar brazo (10 - 170 cm)
        validInputs &= isValidNumber(armTextArea, 10, 170, "Brazo", armErrorLabel, 1, false);

        // Validar pecho (50 - 170 cm)
        validInputs &= isValidNumber(chestTextArea, 50, 170, "Pecho", chestErrorLabel, 1, false);

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
    public void showDiet() {
        uploadFoodsFromCSV();

        hideAll();
        dietPane.setVisible(true);
        menuVbox.setVisible(true);


        LocalDate today = LocalDate.now();
        int dayOfWeek = today.getDayOfWeek().getValue();
        Date date1 = null;
        Date date2 = null;
        Date date3 = null;
        Date date4 = null;
        Date date5 = null;


        for (int row = 1; row <= 5; row++) {
            for (int col = 1; col <= 5; col++) {
                int daysOffset = col - 1;
                LocalDate targetDate = today.minusDays(dayOfWeek - 1).plusDays(daysOffset);

                DayMeal dayMeal = getDayMealForDate(Date.valueOf(targetDate).toLocalDate());
                Food foodForCell = getFoodForCell(dayMeal, row);

                switch (col) {
                    case 1 -> date1 = Date.valueOf(targetDate);
                    case 2 -> date2 = Date.valueOf(targetDate);
                    case 3 -> date3 = Date.valueOf(targetDate);
                    case 4 -> date4 = Date.valueOf(targetDate);
                    case 5 -> date5 = Date.valueOf(targetDate);
                }

                Button button = new Button();
                button.setMaxWidth(Double.MAX_VALUE);
                button.setMaxHeight(Double.MAX_VALUE);

/**

                DESHABILITEN ESTO SI VAN A TESTEAR EN UN FIN DE SEMANA O QUIEREN
                TESTEAR, BLOQUEA LAS COLUMNAS ANTERIORES AL DIA DE HOY

                if (targetDate.isBefore(today)) {
                    button.setDisable(true); // Deshabilita el botón si la fecha es anterior a hoy
                }
*/


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

                //Quitamos la capacidad de agregar cosas si ya existe un reporte
                Report report = reportRepository.findByDate(Date.valueOf(targetDate));
                if (report != null) {
                    if (button.getGraphic() == null){
                        button.setDisable(true);
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
                    } else if (button.getGraphic() == null){
                        Report report2 = reportRepository.findByDate(Date.valueOf(targetDate));
                        if (report2 == null) {
                            handleCellClick(clickedButton,finalRow, finalCol);
                        } else{
                            button.setDisable(true);
                        }
                    }
                });
            }
        }

        ObservableList<String> options = FXCollections.observableArrayList("Sí", "No");

        choiceBox1.setItems(options);
        choiceBox2.setItems(options);
        choiceBox3.setItems(options);
        choiceBox4.setItems(options);
        choiceBox5.setItems(options);

        Date finalDate = date1;
        choiceBox1.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> handleChoiceBoxSelection(newValue, finalDate));
        if (reportRepository.findByDate(finalDate) != null) choiceBox1.setDisable(true);

        Date finalDate1 = date2;
        choiceBox2.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> handleChoiceBoxSelection(newValue, finalDate1));
        if (reportRepository.findByDate(finalDate1) != null) choiceBox2.setDisable(true);

        Date finalDate2 = date3;
        choiceBox3.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> handleChoiceBoxSelection(newValue, finalDate2));
        if (reportRepository.findByDate(finalDate2) != null) choiceBox3.setDisable(true);

        Date finalDate3 = date4;
        choiceBox4.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> handleChoiceBoxSelection(newValue, finalDate3));
        if (reportRepository.findByDate(finalDate3) != null) choiceBox4.setDisable(true);

        Date finalDate4 = date5;
        choiceBox5.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> handleChoiceBoxSelection(newValue, finalDate4));
        if (reportRepository.findByDate(finalDate4) != null) choiceBox5.setDisable(true);

        Properties properties = new Properties();
        try (FileInputStream in = new FileInputStream("preferencesState.properties")) {
            properties.load(in);
            if ("true".equals(properties.getProperty("preferencesCompleted"))) {
                setYourPreferencesButtonDiet.setVisible(false);
                dietPaneConfig.setVisible(false);
            } else {
                disableGridPane(gridPaneDiet);

            }
        } catch (IOException e) {
            disableGridPane(gridPaneDiet);

            //e.printStackTrace();
        }



    }

    @FXML
    public void resetAndLoadDiet() {

        List<StackPane> gridPanes = Arrays.asList(
                gridPane11, gridPane12, gridPane13, gridPane14, gridPane15,
                gridPane21, gridPane22, gridPane23, gridPane24, gridPane25,
                gridPane31, gridPane32, gridPane33, gridPane34, gridPane35,
                gridPane41, gridPane42, gridPane43, gridPane44, gridPane45,
                gridPane51, gridPane52, gridPane53, gridPane54, gridPane55
        );

        for (StackPane gridPane : gridPanes) {
            boolean hasImage = false;

            // Verifica si alguno de los nodos hijos es una imagen
            for (Node node : gridPane.getChildren()) {
                if (node instanceof ImageView) {
                    hasImage = true;
                    break;
                }
            }

            if (hasImage) {
                // Si tiene una imagen, limpia el contenido
                gridPane.getChildren().clear();
            } else {
                // Si no tiene una imagen, deshabilita todos los controles dentro
                for (Node node : gridPane.getChildren()) {
                    if (node instanceof Button) {
                        ((Button) node).setDisable(true);  // Deshabilita los botones específicamente
                    } else if (node instanceof Control) {
                        ((Control) node).setDisable(true);  // Deshabilita otros controles si es necesario
                    }
                }
            }
        }

        choiceBox1.getSelectionModel().clearSelection();
        choiceBox2.getSelectionModel().clearSelection();
        choiceBox3.getSelectionModel().clearSelection();
        choiceBox4.getSelectionModel().clearSelection();
        choiceBox5.getSelectionModel().clearSelection();

        showDiet();
    }

    private Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
        for (Node node : gridPane.getChildren()) {
            Integer columnIndex = GridPane.getColumnIndex(node);
            Integer rowIndex = GridPane.getRowIndex(node);

            // Si el índice de columna o fila es null, asignamos un valor por defecto
            columnIndex = (columnIndex == null) ? 0 : columnIndex; // valor por defecto 0
            rowIndex = (rowIndex == null) ? 0 : rowIndex; // valor por defecto 0

            // Verificamos si el nodo está en la celda correcta
            if (columnIndex == col && rowIndex == row) {
                return node;
            }
        }
        return null;
    }

    @Autowired
    ReportRepository reportRepository;

    private void handleChoiceBoxSelection(String selectedValue, Date localDate) {
        // Dependiendo de la opción seleccionada y del índice del ChoiceBox, ejecutar algo
        if ("Sí".equals(selectedValue)) {
            saveToReport(localDate);
            resetAndLoadDiet();
        }
        if ("No".equals(selectedValue)) {
            showGoalsCheck(localDate);

        }

    }

    private void saveToReport(Date reportDate) {

        Report existingReport = reportRepository.findByDate(reportDate);

        if (existingReport == null) {
            Report report = new Report();
            DayMeal dayMeal = dayMealRepository.findByDate(reportDate);
            Optional<AccountData> accountData = accountDataRepository.findByAccountId(1L);
            boolean goalMet = true;

            report.setDayExcercise(null);
            report.setDayMeals(dayMeal);
            report.setAccountData(accountData.orElse(null));
            report.setGoalMet(goalMet);
            report.setDate(reportDate);

            reportRepository.save(report);


        } else {
            DayMeal dayMeal = dayMealRepository.findByDate(reportDate);
            Optional<AccountData> accountData = accountDataRepository.findByAccountId(1L);
            boolean goalMet = true;

            existingReport.setDayMeals(dayMeal);
            existingReport.setAccountData(accountData.orElse(null));
            existingReport.setGoalMet(goalMet);
            existingReport.setDate(reportDate);

            reportRepository.save(existingReport);
            }

    }


    private Stage goalsCheckStage;
    private void showGoalsCheck(Date reportDate) {
        if (goalsCheckStage != null && goalsCheckStage.isShowing()) {
            goalsCheckStage.toFront(); // Bring the existing window to the front
            return;
        }
        if (goalsCheckStage == null) {
            Platform.runLater(() -> {
                try {

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/PlantillasFXML/GoalsCheck.fxml"));
                    loader.setControllerFactory(applicationContext::getBean);

                    Scene scene = new Scene(loader.load(), 600, 600); // Limitar tamaño de la escena

                    goalsCheckStage = new Stage();

                    GoalsCheck goalsCheck = loader.getController();
                    goalsCheck.setDate(reportDate);


                    goalsCheckStage.setTitle("Principal");
                    goalsCheckStage.setScene(scene);

                    // Establecer límites para la ventana
                    goalsCheckStage.setMinWidth(600);
                    goalsCheckStage.setMinHeight(600);
                    goalsCheckStage.setMaxWidth(600);
                    goalsCheckStage.setMaxHeight(600);

                    goalsCheckStage.setOnCloseRequest(event -> {
                        goalsCheckStage = null;
                    });

                    goalsCheckStage.show();
                    goalsCheck.setCloseCallback(this::goalsCheckClosed);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            resetAndLoadDiet();

        }

    }

    private void goalsCheckClosed() {
        goalsCheckStage = null; // Correctly reset the reference
        resetAndLoadDiet(); // Or any other actions you need to take
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

                NutrimentalInfo nutrimentalInfo = loader.getController();
                nutrimentalInfo.setNutritionalData(targetDate, foodType);


                nutrimentalInfoStage.setTitle("Principal");
                nutrimentalInfoStage.setScene(scene);

                // Establecer límites para la ventana
                nutrimentalInfoStage.setMinWidth(600);
                nutrimentalInfoStage.setMinHeight(600);
                nutrimentalInfoStage.setMaxWidth(600);
                nutrimentalInfoStage.setMaxHeight(600);

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
                    return dayMeal.getBreakfast().isEmpty() ? null : dayMeal.getBreakfast().get(0);
                case 2: // Lunch
                    return dayMeal.getLunch().isEmpty() ? null : dayMeal.getLunch().get(0);
                case 3: // Dinner
                    return dayMeal.getDinner().isEmpty() ? null : dayMeal.getDinner().get(0);
                case 4: // Snack
                    return dayMeal.getSnack().isEmpty() ? null : dayMeal.getSnack().get(0);
                case 5: // optional
                    return dayMeal.getOptional().isEmpty() ? null : dayMeal.getOptional().get(0);
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

                if (selectYourFoodStage == null || !selectYourFoodStage.isShowing()) {

                    selectYourFoodStage = new Stage();


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
        preferencesHBox.setVisible(false);
        hideAll();
        showDiet();
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
    public void showExercise() {
        hideAll();
        exercisePane.setVisible(true);
        menuVbox.setVisible(true);
        updateExerciseLabels();

        setYourPreferencesButtonExercise.setOnAction(e -> openSetYourPreferencesExcercise());

        // Definir grupos musculares
        AccountData accountData = new AccountData();
        String monday = String.valueOf(accountData.getMonday());
        String tuesday = String.valueOf(accountData.getTuesday());
        String wednesday = String.valueOf(accountData.getWednesday());
        String thursday = String.valueOf(accountData.getThursday());
        String friday = String.valueOf(accountData.getFriday());

        String[] muscleGroups = {monday, tuesday, wednesday, thursday, friday};

        LocalDate today = LocalDate.now();
        int dayOfWeek = today.getDayOfWeek().getValue();
        Date date1 = null;
        Date date2 = null;
        Date date3 = null;
        Date date4 = null;
        Date date5 = null;

        for (int row = 1; row <= 5; row++) {
            for (int col = 1; col <= 5; col++) {
                int daysOffset = col - 1;
                LocalDate targetDate = today.minusDays(dayOfWeek - 1).plusDays(daysOffset);

                switch (col) {
                    case 1 -> date1 = Date.valueOf(targetDate);
                    case 2 -> date2 = Date.valueOf(targetDate);
                    case 3 -> date3 = Date.valueOf(targetDate);
                    case 4 -> date4 = Date.valueOf(targetDate);
                    case 5 -> date5 = Date.valueOf(targetDate);
                }
                Button exerciseButton = new Button("");
                exerciseButton.setMaxWidth(Double.MAX_VALUE);
                exerciseButton.setMaxHeight(Double.MAX_VALUE);

                gridPaneExercise.add(exerciseButton, 2, row);

                // Hacer que el botón crezca dentro de la celda
                GridPane.setHgrow(exerciseButton, Priority.ALWAYS);


                int finalRow = row;
                int finalCol = col;
                exerciseButton.setOnMouseClicked(event -> {

                    Button clickedButton = (Button) event.getSource();

                    if (exerciseButton.getGraphic() != null) {
                        System.out.println("HOLAAAAA ENTRE A QUE CHHEQUEEESSS TU RUTINA WEIIIII");
                        showCheckYourRutine();

                    } else if (exerciseButton.getGraphic() == null){
                        Report report2 = reportRepository.findByDate(Date.valueOf(targetDate));
                        if (report2 == null) {
                            System.out.println("HOLAAAAA ENTRE A QUE SELECCIONESSS TU RUTINA WEIIIII");
                            //showSetYourRutine();
                            handleCellClickForExercise(clickedButton, finalRow, finalCol);
                            //handleCellClick(clickedButton,finalRow, finalCol);

                        } else{
                            exerciseButton.setDisable(true);
                        }
                    }
                });

            }
        }
    }
    private static Stage showSetYourRutine; // Stage global

    private void handleCellClickForExercise(Button clickedButton, int row, int col) {
        Platform.runLater(() -> {
            try {
                if (showSetYourRutine == null || !showSetYourRutine.isShowing()) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/PlantillasFXML/SetYourRutine.fxml"));
                    loader.setControllerFactory(applicationContext::getBean);

                    Parent root = loader.load(); // Cargar el FXML
                    SetYourRutine controller = loader.getController(); // Obtener el controlador

                    // Crear la ventana
                    showSetYourRutine = new Stage();
                    showSetYourRutine.setTitle("Principal");
                    showSetYourRutine.setScene(new Scene(root));
                    showSetYourRutine.setMinWidth(900);
                    showSetYourRutine.setMinHeight(520);

                    showSetYourRutine.setOnCloseRequest(event -> showSetYourRutine = null);

                    showSetYourRutine.show();
                } else {
                    showSetYourRutine.toFront();
                }

                // Lógica del botón
                if (clickedButton.getGraphic() != null) {
                    clickedButton.setGraphic(null);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    private Stage checkYourRutineStage;

    private void showCheckYourRutine() {

        // Cargar las propiedades
        Properties properties = new Properties();
        loadProperties(properties);


        // Si la ventana ya está abierta, la traemos al frente
        if (checkYourRutineStage != null && checkYourRutineStage.isShowing()) {
            checkYourRutineStage.toFront(); // Traer la ventana existente al frente
            return;
        }

        // Abrir la ventana de check si aún no se ha completado el ejercicio
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/PlantillasFXML/CheckYourRutine.fxml"));
                loader.setControllerFactory(applicationContext::getBean);

                CheckYourRutine controller = loader.getController();

                Scene scene = new Scene(loader.load(), 400, 500); // Limitar tamaño de la escena

                checkYourRutineStage = new Stage();
                checkYourRutineStage.setTitle("Principal");
                checkYourRutineStage.setScene(scene);

                // Establecer límites para la ventana
                checkYourRutineStage.setMinWidth(400);
                checkYourRutineStage.setMinHeight(500);
                checkYourRutineStage.setMaxWidth(400);
                checkYourRutineStage.setMaxHeight(500);

                checkYourRutineStage.setOnCloseRequest(event -> checkYourRutineStage = null); // Reset cuando se cierra

                checkYourRutineStage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    private Stage setYourRutineStage;

    private void showSetYourRutine() {

        Properties properties = new Properties();
        loadProperties(properties);

        if (setYourRutineStage != null && setYourRutineStage.isShowing()) {
            setYourRutineStage.toFront(); // Traer la ventana existente al frente
            return;
        }
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/PlantillasFXML/SetYourRutine.fxml"));
                loader.setControllerFactory(applicationContext::getBean);

                SetYourRutine controller = loader.getController();

                Scene scene = new Scene(loader.load(), 400, 500); // Limitar tamaño de la escena

                setYourRutineStage = new Stage();
                setYourRutineStage.setTitle("Principal");
                setYourRutineStage.setScene(scene);

                // Establecer límites para la ventana
                setYourRutineStage.setMinWidth(400);
                setYourRutineStage.setMinHeight(500);
                setYourRutineStage.setMaxWidth(400);
                setYourRutineStage.setMaxHeight(500);

                setYourRutineStage.setOnCloseRequest(event -> setYourRutineStage = null); // Reset cuando se cierra

                setYourRutineStage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }




//    @Autowired
//    DayExcerciseRepository dayExcerciseRepository;
//
//    private DayExcercise getDayExerciseForDate(LocalDate targetDate) {
//        return dayExcerciseRepository.findByDate(java.sql.Date.valueOf(targetDate));
//    }

    /*
    private Excercise getExcerciseForCell(DayExcercise dayExcercise, int row) {
        if (dayExcercise != null) {
            switch (row) {
                case 1: // Breakfast
                    return dayMeal.getBreakfast().isEmpty() ? null : dayMeal.getBreakfast().get(0);
                case 2: // Lunch
                    return dayMeal.getLunch().isEmpty() ? null : dayMeal.getLunch().get(0);
                case 3: // Dinner
                    return dayMeal.getDinner().isEmpty() ? null : dayMeal.getDinner().get(0);
                case 4: // Snack
                    return dayMeal.getSnack().isEmpty() ? null : dayMeal.getSnack().get(0);
                case 5: // optional
                    return dayMeal.getOptional().isEmpty() ? null : dayMeal.getOptional().get(0);
                default:
                    return null;
            }
        }
        return null;
    }

     */


    private Stage preferencesExcerciseStage;

    private void openSetYourPreferencesExcercise() {
        // Cargar las propiedades
        Properties properties = new Properties();
        loadProperties(properties);


        // Si la ventana ya está abierta, la traemos al frente
        if (preferencesExcerciseStage != null && preferencesExcerciseStage.isShowing()) {
            preferencesExcerciseStage.toFront(); // Traer la ventana existente al frente
            return;
        }

        // Abrir la ventana de preferencias si aún no se ha completado el ejercicio
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/PlantillasFXML/SetYourPreferencesExercise.fxml"));
                loader.setControllerFactory(applicationContext::getBean);

                SetYourPreferencesExercise controller = loader.getController();

                Scene scene = new Scene(loader.load(), 400, 500); // Limitar tamaño de la escena

                preferencesExcerciseStage = new Stage();
                preferencesExcerciseStage.setTitle("Principal");
                preferencesExcerciseStage.setScene(scene);

                // Establecer límites para la ventana
                preferencesExcerciseStage.setMinWidth(400);
                preferencesExcerciseStage.setMinHeight(500);
                preferencesExcerciseStage.setMaxWidth(400);
                preferencesExcerciseStage.setMaxHeight(500);

                preferencesExcerciseStage.setOnCloseRequest(event -> preferencesExcerciseStage = null); // Reset cuando se cierra

                preferencesExcerciseStage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void loadProperties(Properties properties) {
        try (FileInputStream in = new FileInputStream("preferencesState.properties")) {
            properties.load(in);
        } catch (IOException e) {
            // Si el archivo no existe, no pasa nada lol le quite el printstack porque luego asusta
            //e.printStackTrace();
        }
    }
    private void updateExerciseLabels() {
        // Obtén los datos de la cuenta actual
        accountRepository.findById(1L).ifPresentOrElse(account -> {
            AccountData accountData = account.getAccountData();

            if (accountData != null) {
                // Obtén los valores de los días
                String mondayExercise = accountData.getMonday() != null ? formatExerciseLabel(accountData.getMonday().toString()) : "No asignado";
                String tuesdayExercise = accountData.getTuesday() != null ? formatExerciseLabel(accountData.getTuesday().toString()) : "No asignado";
                String wednesdayExercise = accountData.getWednesday() != null ? formatExerciseLabel(accountData.getWednesday().toString()) : "No asignado";
                String thursdayExercise = accountData.getThursday() != null ? formatExerciseLabel(accountData.getThursday().toString()) : "No asignado";
                String fridayExercise = accountData.getFriday() != null ? formatExerciseLabel(accountData.getFriday().toString()) : "No asignado";

                // Recorre los nodos dentro del GridPane
                for (Node node : gridPaneExercise.getChildren()) {
                    // Verifica si el nodo es un StackPane
                    if (node instanceof StackPane) {
                        // Asigna valores predeterminados si el índice de columna o fila es null
                        Integer columnIndex = GridPane.getColumnIndex(node);
                        Integer rowIndex = GridPane.getRowIndex(node);

                        if (columnIndex == null) columnIndex = 0; // Valor predeterminado para columna
                        if (rowIndex == null) rowIndex = 0; // Valor predeterminado para fila

                        // Verifica si estamos en la segunda columna (índice 1)
                        if (columnIndex == 1) {
                            // Dentro de cada StackPane, busca el Label y actualiza el texto
                            StackPane stackPane = (StackPane) node;
                            Label label = (Label) stackPane.getChildren().get(0);  // Suponiendo que el Label es el primer hijo

                            // Actualiza el texto del Label dependiendo de la fila
                            switch (rowIndex) {
                                case 1:
                                    label.setText(mondayExercise);
                                    break;
                                case 2:
                                    label.setText(tuesdayExercise);
                                    break;
                                case 3:
                                    label.setText(wednesdayExercise);
                                    break;
                                case 4:
                                    label.setText(thursdayExercise);
                                    break;
                                case 5:
                                    label.setText(fridayExercise);
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                }
            }
        }, () -> System.out.println("Error: No se encontró la cuenta con ID 2"));
    }
    private String formatExerciseLabel(String exercise) {
        // Reemplaza los valores en minúsculas y sin espacios, añadiendo los espacios correctos
        String formatted = exercise.replaceAll("([a-z])([A-Z])", "$1 $2");

        // Reemplaza la letra "y" con espacio antes y después (sin cambiar a mayúscula)
        formatted = formatted.replaceAll("y", " y ");

        // Reemplaza la palabra "completa" y añade un espacio después
        formatted = formatted.replaceAll("completa", " completa");

        // Capitaliza la primera letra de cada palabra, excepto la "y"
        String[] words = formatted.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            if (!word.equals("y")) {
                sb.append(word.substring(0, 1).toUpperCase()).append(word.substring(1)).append(" ");
            } else {
                sb.append(word).append(" "); // Para "y", no se hace mayúscula
            }
        }

        return sb.toString().trim();
    }







    /**
     reportes
     */

    @FXML
    private void showReports() {
        hideAll();
        reportsPane.setVisible(true);
        menuVbox.setVisible(true);

        Date oldestReportDate = reportRepository.findOldestReportDate();

        // Verificar si oldestReportDate es null
        if (oldestReportDate == null) {
            // Si no hay fecha, deshabilitar completamente el startDatePicker
            startDatePicker.setDisable(true);
            oldestReportDate = reportRepository.findOldestReportDate();

        } else {
            startDatePicker.setDisable(false);

            // Convertir la fecha a LocalDate
            LocalDate oldestDate = oldestReportDate.toLocalDate();

            // Configurar el DatePicker para que no permita fechas anteriores a la más antigua
            startDatePicker.setDayCellFactory(new Callback<DatePicker, DateCell>() {
                @Override
                public DateCell call(DatePicker datePicker) {
                    return new DateCell() {
                        @Override
                        public void updateItem(LocalDate date, boolean empty) {
                            super.updateItem(date, empty);
                            if (date.isBefore(oldestDate)) {
                                setDisable(true);  // Deshabilitar la fecha si es anterior a la fecha más antigua
                                setStyle("-fx-background-color: #d3d3d3;");  // Estilo para las fechas deshabilitadas
                            }
                        }
                    };
                }
            });
        }


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
                try {
                    generateReportAndSendEmail();
                    successReportHbox.setVisible(true);
                    PauseTransition pause = new PauseTransition(Duration.seconds(3));
                    pause.setOnFinished(e -> successReportHbox.setVisible(false));  // Ocultar el HBox después de la pausa
                    pause.play();
                } catch (MessagingException e) {
                    throw new RuntimeException(e);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                startCooldown();

            }
        });

        disableVBox(reportsPane);

    }

    private static final int COOLDOWN_TIME = 10;

    private void startCooldown() {
        // Pausar por el tiempo de cooldown y luego habilitar el botón
        PauseTransition pause = new PauseTransition(Duration.seconds(COOLDOWN_TIME));
        pause.setOnFinished(event -> sendReportButton.setDisable(false));
        pause.play();
    }

    public void generateReport() throws FileNotFoundException, IOException {
        String dest = "toSendPDF.pdf";
        PdfWriter writer = new PdfWriter(dest);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf, PageSize.A4);

        PdfFont boldFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        PdfFont normalFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);

        // Datos del usuario
        String name = accountRepository.findById(1L).get().getName();
        String email = accountRepository.findById(1L).get().getEmail();

        Optional<AccountData> account = accountDataRepository.findByAccountId(1L);
        int age = account.get().getAge();
        String gender = account.get().getGender() ? "Masculino" : "Femenino";
        Double weight = account.get().getWeight();
        Double height = account.get().getHeight();
        Goal goal = account.get().getGoal();
        String goalString = (goal != null) ? goal.toString() : "Mantenimiento";

        Double abdomen = account.get().getAbdomen();
        Double hips = account.get().getHips();
        Double waist = account.get().getWaist();
        Double arm = account.get().getArm();
        Double chest = account.get().getChest();
        Double neck = account.get().getNeck();

        double imc = Math.round((weight / Math.pow(height / 100.0, 2)) * 10.0) / 10.0;

        // Encabezado
        document.add(new Paragraph("Reporte Nutricional").setFont(boldFont).setFontSize(18).setTextAlignment(TextAlignment.CENTER));
        document.add(new Paragraph("Datos del Usuario").setFont(boldFont).setFontSize(14));
        document.add(new Paragraph("Nombre: " + name));
        document.add(new Paragraph("Correo: " + email));
        document.add(new Paragraph("Edad: " + age));
        document.add(new Paragraph("Género: " + gender));
        document.add(new Paragraph("Peso: " + weight + " kg"));
        document.add(new Paragraph("Altura: " + height + " m"));
        document.add(new Paragraph("Meta: " + goalString));
        document.add(new Paragraph("IMC: " + imc));

        document.add(new Paragraph("Medidas Corporales").setFont(boldFont).setFontSize(14));
        document.add(new Paragraph("Abdomen: " + abdomen + " cm"));
        document.add(new Paragraph("Caderas: " + hips + " cm"));
        document.add(new Paragraph("Cintura: " + waist + " cm"));
        document.add(new Paragraph("Brazo: " + arm + " cm"));
        document.add(new Paragraph("Pecho: " + chest + " cm"));
        document.add(new Paragraph("Cuello: " + neck + " cm"));

        document.add(new Paragraph(" "));

        document.add(new Paragraph("Reporte de " + Date.valueOf(startDatePicker.getValue()).toString() + " a " + Date.valueOf(endDatePicker.getValue()).toString())
                .setFont(boldFont)
                .setFontSize(14)
                .setTextAlignment(TextAlignment.CENTER));

        document.add(new Paragraph(" ")); // Espacio antes de la siguiente sección


        // Información de Reportes
        List<Report> results = accountRepository.findReportsByAccountAndDateRange(1L, Date.valueOf(startDatePicker.getValue()), Date.valueOf(endDatePicker.getValue()));


        document.add(new Paragraph("Comidas").setFont(boldFont).setFontSize(14));

        Table table = new Table(new float[]{3, 3, 3, 3, 3, 3}).useAllAvailableWidth(); // 6 columnas
        table.addHeaderCell(new Cell().add(new Paragraph("Fecha").setFont(boldFont)).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        table.addHeaderCell(new Cell().add(new Paragraph("Desayuno").setFont(boldFont)).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        table.addHeaderCell(new Cell().add(new Paragraph("Comida").setFont(boldFont)).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        table.addHeaderCell(new Cell().add(new Paragraph("Cena").setFont(boldFont)).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        table.addHeaderCell(new Cell().add(new Paragraph("Snack").setFont(boldFont)).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        table.addHeaderCell(new Cell().add(new Paragraph("Opcional").setFont(boldFont)).setBackgroundColor(ColorConstants.LIGHT_GRAY));

        for (Report result : results) {
            table.addCell(new Cell().add(new Paragraph(result.getDate().toString())));

            // Agregar los nombres de los alimentos (si existen) para cada comida
            table.addCell(new Cell().add(new Paragraph(getFoodNames(result.getDayMeals().getBreakfast()))));
            table.addCell(new Cell().add(new Paragraph(getFoodNames(result.getDayMeals().getLunch()))));
            table.addCell(new Cell().add(new Paragraph(getFoodNames(result.getDayMeals().getDinner()))));
            table.addCell(new Cell().add(new Paragraph(getFoodNames(result.getDayMeals().getSnack()))));
            table.addCell(new Cell().add(new Paragraph(getFoodNames(result.getDayMeals().getOptional()))));
        }

        document.add(table);

        document.add(new Paragraph("Ejercicios Realizados").setFont(boldFont).setFontSize(14));


        Table exerciseTable = new Table(new float[]{3, 3, 3}).useAllAvailableWidth(); // 3 columnas
        exerciseTable.addHeaderCell(new Cell().add(new Paragraph("Fecha").setFont(boldFont)).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        exerciseTable.addHeaderCell(new Cell().add(new Paragraph("Ejercicio").setFont(boldFont)).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        exerciseTable.addHeaderCell(new Cell().add(new Paragraph("Duración").setFont(boldFont)).setBackgroundColor(ColorConstants.LIGHT_GRAY));

        for (Report result : results) {
            exerciseTable.addCell(new Cell().add(new Paragraph(result.getDate().toString())));

            // Obtener la lista de ejercicios de la entidad DayExcercise
            DayExcercise exercise = result.getDayExcercise();
            if (exercise != null) {
                // Mostrar el nombre del ejercicio (o ejercicios si es más de uno)
                StringBuilder exerciseNames = new StringBuilder();
                for (Excercise excercise : exercise.getExcercises()) {
                    exerciseNames.append(excercise.getExcerciseName()).append(", ");
                }

                // Agregar a la tabla el nombre del ejercicio y la duración
                exerciseTable.addCell(new Cell().add(new Paragraph(exerciseNames.length() > 0 ? exerciseNames.substring(0, exerciseNames.length() - 2) : "No disponible")));
                exerciseTable.addCell(new Cell().add(new Paragraph(exercise.getTime() + " mins")));
            } else {
                exerciseTable.addCell(new Cell().add(new Paragraph("No disponible")));
                exerciseTable.addCell(new Cell().add(new Paragraph("No disponible")));
            }
        }

        document.add(exerciseTable);

        document.close();

    }

    @Autowired
    private IEmailService iEmailService;

    private void sendEmailWithPDFAttachment() throws MessagingException, IOException {
        // Ruta del archivo PDF que queremos adjuntar
        String pdfFilePath = "toSendPDF.pdf";

        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setAddressee("nutriappunison@gmail.com");
        emailDTO.setSubject("Reporte Nutricional");
        emailDTO.setMessage("Adjunto el reporte nutricional generado.");

        File pdfFile = new File(pdfFilePath);
        iEmailService.sendMailWithAttachment(emailDTO, pdfFile);
    }

    // Método para generar el reporte y luego enviarlo
    private void generateReportAndSendEmail() throws FileNotFoundException, IOException, MessagingException {
        generateReport();
        sendEmailWithPDFAttachment();
    }



    private String getFoodNames(List<Food> foodList) {
        if (foodList == null || foodList.isEmpty()) {
            return "";
        }
        StringBuilder foodNames = new StringBuilder();
        for (Food food : foodList) {
            foodNames.append(food.getFoodName()).append(", ");  // Agregar el nombre de cada alimento
        }
        return foodNames.length() > 0 ? foodNames.substring(0, foodNames.length() - 2) : "";  // Eliminar la última coma
    }

    private String formatExerciseName(ExcerciseType exerciseType) {
        if (exerciseType == null) {
            return "";
        }
        String formattedName;
        if (exerciseType == ExcerciseType.piernacompleta) {
            formattedName = "pierna completa";
        } else {
            formattedName = exerciseType.name()
                    .replaceAll("y", " y ")  // Agrega espacios antes y después de "y"
                    .replaceAll("(?<!^)([A-Z])", " $1") // Agrega espacio antes de mayúsculas
                    .toLowerCase();
        }
        // Convertir la primera letra en mayúscula
        return formattedName.substring(0, 1).toUpperCase() + formattedName.substring(1);
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

    public void hideAll() {
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

    public void refreshContent() {
        showExercise();
        showDiet();
    }


//-----ESTO ES PARA LA TABLA DE DIETA CHECAR SI ESTA CORRECTO-----
}
