package com.prueba.demo.principal;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.prueba.demo.model.*;
import com.prueba.demo.modelFreeSQL.AccountDataFreeSQL;
import com.prueba.demo.modelFreeSQL.AccountFreeSQL;
import com.prueba.demo.repository.*;
import com.prueba.demo.repositoryFreeSQL.AccountDataFreeSQLRepository;
import com.prueba.demo.repositoryFreeSQL.AccountFreeSQLRepository;
import com.prueba.demo.service.APIConsumption;
import com.prueba.demo.service.DatabaseService;
import com.prueba.demo.service.IEmailService;
import com.prueba.demo.service.dto.EmailDTO;
import jakarta.mail.MessagingException;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.*;
import java.sql.Date;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class DashboardAdminFrame {

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


    //ProfilePaneEdit
    @FXML
    private VBox profilePaneEdit;
    @FXML
    private Button backButtonProfile;
    @FXML
    private Button updateButton;
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

    //ProfilePaneSelect
    @FXML
    private VBox profilePaneSelect;
    @FXML
    private TextField searchField;
    @FXML
    private ListView usersListView;

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
    private Button backButtonReports;

    /*
        iniciar con datos
     */


    @FXML
    private void handleMouseEnteredDiet(MouseEvent event) {
        Button button = (Button) event.getSource();
        button.setStyle("-fx-background-color: #A3D13C;");
    }

    @FXML
    private void handleMouseExitedDiet(MouseEvent event) {
        Button button = (Button) event.getSource();
        button.setStyle("-fx-background-color:   #7da12d;");
    }
    @FXML
    private void handleMouseEnteredConfigure(MouseEvent event) {
        Button button = (Button) event.getSource();
        button.setStyle("-fx-background-color: #595959;");
    }

    @FXML
    private void handleMouseExitedConfigure(MouseEvent event) {
        Button button = (Button) event.getSource();
        button.setStyle("-fx-background-color:  #262626;");
    }
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
                try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("foods.csv")) {
                    if (inputStream == null) {
                        throw new FileNotFoundException("foods.csv not found in classpath");
                    }

                    try (InputStreamReader reader = new InputStreamReader(inputStream);
                         CSVReader csvReader = new CSVReader(reader)) {                    // Leer el archivo CSV
                    List<String[]> rows = csvReader.readAll();

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

                }
            }catch (IOException e) {
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
        }


        rootPane.setMinWidth(900);  // Ancho mínimo
        rootPane.setMinHeight(520); // Alto mínimo

        // Asigna eventos a botones
        reportsPane.setVisible(false);



        // Asociar acciones a botones
        profileButton.setOnAction(event -> showProfile());
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



        //Profile
        //Click boton de profile


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
    private void mouseEntered(MouseEvent event) {
        // Aquí puedes cambiar el color de fondo del HBox, por ejemplo
        Node node = (Node) event.getSource();  // Obtiene la referencia al HBox clickeado

        node.setStyle("-fx-background-color: #404040;");  // Cambia el color de fondo a gris
    }
    @FXML
    private void mouseExited(MouseEvent event) {
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

    private void changeProgressBarColor(ProgressBar progressBar, double progress) {
        // Obtiene el nodo de la barra interna de progreso (la parte llena)
        Region bar = (Region) progressBar.lookup(".bar");

        // Cambiar el color de la barra de progreso según el valor
        if (bar != null) {
            if (progress <= 0.40) {
                bar.setStyle("-fx-background-color: #FF9933;");  // Naranja si el progreso es menor o igual a 45%
            } else if (progress > 0.40 && progress <= 0.80) {
                bar.setStyle("-fx-background-color: #ffd24d;");  // Amarillo si el progreso es entre 40% y 80%
            } else if (progress > 0.80 && progress <= 1.00) {
                bar.setStyle("-fx-background-color: #A7C942;");  // Verde si el progreso es entre 80% y 100%
            } else {
                bar.setStyle("-fx-background-color: #b30000;");  // Rojo si el progreso es mayor a 100%
            }
        }
    }
    private Color getProgressColor(double progress) {
        if (progress <= 0.40) {
            return Color.web("#FF9933"); // Naranja
        } else if (progress > 0.40 && progress <= 0.80) {
            return Color.web("#ffd24d"); // Amarillo
        } else if (progress > 0.80 && progress <= 1.00) {
            return Color.web("#A7C942"); // Verde
        } else {
            return Color.web("#b30000"); // Rojo
        }
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

    @Autowired
    AccountFreeSQLRepository accountFreeSQLRepository;
    @Autowired
    AccountDataFreeSQLRepository accountDataFreeSQLRepository;

    @FXML
    private void showProfile() {
        hideAll();
        profilePaneSelect.setVisible(true);
        menuVbox.setVisible(true);



        Optional<Account> account = accountRepository.findById(1L);
        AccountData accountData = account.get().getAccountData();

        userNameLabel.setText(account.get().getName());


        // Actualizar los campos del perfil
        updateProfileFields(accountData);
    }


    private void updateProfileFields(AccountData accountData) {

        Optional<AccountFreeSQL> accountFreeSQL = accountFreeSQLRepository.findByEmail(accountData.getAccount().getEmail());
        Optional<AccountDataFreeSQL> accountDataFreeSQL = accountDataFreeSQLRepository.findByAccountFreeSQL_Id(accountFreeSQL.get().getId());


        sexTextArea.setText(accountData.getGender() != null && accountData.getGender() ? "Masculino" : "Femenino");
        ageTextArea.setText(String.valueOf(accountDataFreeSQL.get().getAge()));
        heightTextArea.setText(String.valueOf(accountDataFreeSQL.get().getHeight()));
        weightTextArea.setText(String.valueOf(accountDataFreeSQL.get().getWeight()));
        abdomenTextArea.setText(String.valueOf(accountDataFreeSQL.get().getAbdomen()));
        hipTextArea.setText(String.valueOf(accountDataFreeSQL.get().getHips()));
        waistTextArea.setText(String.valueOf(accountDataFreeSQL.get().getWaist()));
        chestTextArea.setText(String.valueOf(accountDataFreeSQL.get().getChest()));
        neckTextArea.setText(String.valueOf(accountDataFreeSQL.get().getNeck()));
        armTextArea.setText(String.valueOf(accountDataFreeSQL.get().getArm()));

        // Actualizar alergias
        List<String> allergicFoodNames = new ArrayList<>();

        Long accountAllergyId = null;
        if (accountData.getAccountAllergy() != null) {
            accountAllergyId = accountData.getAccountAllergy().getId();
        }

        List<AccountAllergyFood> allergyFoods = new ArrayList<>();

        if (accountAllergyId != null) {
            allergyFoods = accountAllergyFoodRepository.findAllByAccountAllergyId(accountAllergyId);
        }

        if (!allergyFoods.isEmpty()){
            for (AccountAllergyFood allergyFood : allergyFoods) {
                allergicFoodNames.add(allergyFood.getFood().getFoodName());
            }
        }


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
    @Autowired
    DatabaseService databaseService;

    private void setupChoiceBox(ChoiceBox<String> choiceBox, Date date, LocalDate today) {
        if (date == null) return;

        LocalDate localDate = date.toLocalDate();

        choiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
                handleChoiceBoxSelection(newValue, date)
        );

        Report report = reportRepository.findByDate(date);

        boolean hasMeal = report != null && report.getDayMeal() != null;
        boolean isToday = localDate.equals(today);

        choiceBox.setDisable(hasMeal || !isToday);
    }

    @Autowired
    ReportRepository reportRepository;

    private void handleChoiceBoxSelection(String selectedValue, Date localDate) {
        // Dependiendo de la opción seleccionada y del índice del ChoiceBox, ejecutar algo
        if ("Sí".equals(selectedValue)) {
            saveToReport(localDate);
        }
        if ("No".equals(selectedValue)) {

        }

    }

    @Autowired
    DayMealRepository dayMealRepository;
    private void saveToReport(Date reportDate) {

        Report existingReport = reportRepository.findByDate(reportDate);

        if (existingReport == null) {
            Report report = new Report();
            DayMeal dayMeal = dayMealRepository.findByDate(reportDate);
            Optional<AccountData> accountData = accountDataRepository.findByAccountId(1L);

            report.setDayExcercise(null);
            report.setDayMeals(dayMeal);
            report.setAccountData(accountData.orElse(null));
            report.setDate(reportDate);

            reportRepository.save(report);


        } else {
            DayMeal dayMeal = dayMealRepository.findByDate(reportDate);
            Optional<AccountData> accountData = accountDataRepository.findByAccountId(1L);
            existingReport.setDayMeals(dayMeal);
            existingReport.setAccountData(accountData.orElse(null));
            existingReport.setDate(reportDate);

            reportRepository.save(existingReport);
            }

    }


    private static SelectYourFood selectYourFoodController; // Static instance
    private static Stage selectYourFoodStage;



    private void disableGridPane(GridPane gridPane) {
        for (Node node : gridPane.getChildren()) {
            if (node instanceof Control) {
                ((Control) node).setDisable(true);
            }
        }
    }


    private Stage preferencesDietStage;



    @Autowired
    DayExcerciseRepository dayExcerciseRepository;


    private void enableNode(Node node) {
        if (node instanceof Control) {
            ((Control) node).setDisable(false);
        } else if (node instanceof Parent) { // StackPane, VBox, etc.
            for (Node child : ((Parent) node).getChildrenUnmodifiable()) {
                enableNode(child);
            }
        }
    }


    private void disableNode(Node node) {
        if (node instanceof Control) {
            ((Control) node).setDisable(true);
        } else if (node instanceof Parent) { // Parent covers containers like StackPane, VBox, HBox, etc.
            for (Node child : ((Parent) node).getChildrenUnmodifiable()) {
                disableNode(child);
            }
        }
    }
    private static Stage showSetYourRutine; // Stage global

    private void handleCellClickForExercise(int row) {
        Platform.runLater(() -> {
            try {
                if (showSetYourRutine == null || !showSetYourRutine.isShowing()) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/PlantillasFXML/SetYourRutine.fxml"));
                    loader.setControllerFactory(applicationContext::getBean);

                    Parent root = loader.load(); // Cargar el FXML
                    root.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
                    SetYourRutine controller = loader.getController(); // Obtener el controlador
                    controller.setRow(row);

                    // Crear la ventana
                    showSetYourRutine = new Stage();
                    showSetYourRutine.setTitle("Selecciona tu rutina");
                    showSetYourRutine.setScene(new Scene(root));
                    showSetYourRutine.setMinWidth(450);
                    showSetYourRutine.setMinHeight(600);
                    showSetYourRutine.setMaxWidth(450);
                    showSetYourRutine.setMaxHeight(600);

                    showSetYourRutine.setOnCloseRequest(event -> showSetYourRutine = null);

                    showSetYourRutine.show();
                } else {
                    showSetYourRutine.toFront();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    private Stage checkYourRutineStage;

    private void showCheckYourRutine(LocalDate targetDate) {
        // Load properties
        Properties properties = new Properties();
        loadProperties(properties);

        // If window is already open, bring it to the front
        if (checkYourRutineStage != null && checkYourRutineStage.isShowing()) {
            checkYourRutineStage.toFront();
            return;
        }

        Platform.runLater(() -> {
            try {
                // Initialize FXMLLoader with the Spring context
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/PlantillasFXML/CheckYourRutine.fxml"));
                loader.setControllerFactory(applicationContext::getBean);
                Scene scene = new Scene(loader.load(), 400, 500);

                checkYourRutineStage = new Stage();


                // Load the controller
                CheckYourRutine controller = loader.getController();
                controller.setTargetDate(targetDate);  // Now the controller should not be null


                checkYourRutineStage.setTitle("Informacion de ejercicios");
                checkYourRutineStage.setScene(scene);

                // Set window size restrictions
                checkYourRutineStage.setMinWidth(450);
                checkYourRutineStage.setMinHeight(550);
                checkYourRutineStage.setMaxWidth(450);
                checkYourRutineStage.setMaxHeight(550);

                // Reset checkYourRutineStage when closed
                checkYourRutineStage.setOnCloseRequest(event -> checkYourRutineStage = null);

                checkYourRutineStage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
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

                Scene scene = new Scene(loader.load(), 400, 500); // Limitar tamaño de la escena
                scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());

                preferencesExcerciseStage = new Stage();
                preferencesExcerciseStage.setTitle("Elige tus preferencias en ejercicio");
                preferencesExcerciseStage.setScene(scene);

                // Establecer límites para la ventana
                preferencesExcerciseStage.setMinWidth(450);
                preferencesExcerciseStage.setMinHeight(550);
                preferencesExcerciseStage.setMaxWidth(450);
                preferencesExcerciseStage.setMaxHeight(550);

                preferencesExcerciseStage.setOnCloseRequest(event -> {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmación de salida");
                    alert.setGraphic(null);
                    alert.setHeaderText(null);
                    alert.setContentText("¿Seguro que deseas salir?");

                    ButtonType yesButton = new ButtonType("Sí", ButtonBar.ButtonData.YES);
                    ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);

                    alert.getButtonTypes().setAll(yesButton, noButton);

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isEmpty() || result.get() == noButton) {
                        event.consume(); // Cancela el cierre
                    } else {
                        preferencesExcerciseStage = null; // Solo se resetea si el usuario acepta salir
                    }
                });

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
            }
        }, () -> System.out.println("Error: No se encontró la cuenta con ID 2"));
    }



    private String formatExerciseLabel(String exercise) {

        if (exercise.equals("[]")){
            return "No asignado";
        }
        // Reemplaza los valores en minúsculas y sin espacios, añadiendo los espacios correctos
        String formatted = exercise.replaceAll("([a-z])([A-Z])", "$1 $2");

        // Reemplaza la letra "y" con espacio antes y después (sin cambiar a mayúscula)
        formatted = formatted.replaceAll("y", " y ");

        // Reemplaza la palabra "completa" y añade un espacio después
        formatted = formatted.replaceAll("completa", " completa");

        formatted = formatted.replaceAll("[\\[\\]]", "");

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
        List<String> allergicFoodNames = new ArrayList<>();

        Long accountAllergyId = null;
        if (accountData.getAccountAllergy() != null) {
            accountAllergyId = accountData.getAccountAllergy().getId();
        }

        List<AccountAllergyFood> allergyFoods = new ArrayList<>();

        if (accountAllergyId != null) {
            allergyFoods = accountAllergyFoodRepository.findAllByAccountAllergyId(accountAllergyId);
        }

        if (!allergyFoods.isEmpty()){
            for (AccountAllergyFood allergyFood : allergyFoods) {
                allergicFoodNames.add(allergyFood.getFood().getFoodName());
            }
        }

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

    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
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

        List<String> accountAllergies = new ArrayList<>();

        Long accountAllergyId = null;
        if (account.get().getAccountAllergy() != null) {
            accountAllergyId = account.get().getAccountAllergy().getId();
        }

        List<AccountAllergyFood> allergyFoods = new ArrayList<>();

        if (accountAllergyId != null) {
            allergyFoods = accountAllergyFoodRepository.findAllByAccountAllergyId(accountAllergyId);
        }

        if (!allergyFoods.isEmpty()){
            for (AccountAllergyFood allergyFood : allergyFoods) {
                accountAllergies.add(allergyFood.getFood().getFoodName());
            }
        }


        double imc = Math.round((weight / Math.pow(height / 100.0, 2)) * 10.0) / 10.0;

        InputStream imageStream = getClass().getClassLoader().getResourceAsStream("images/NutriApp256x256.png");

        if (imageStream == null) {
            throw new FileNotFoundException("images/NutriApp256x256.png not found in classpath");
        }

        byte[] imageBytes = imageStream.readAllBytes();
        ImageData imageData = ImageDataFactory.create(imageBytes);

        com.itextpdf.layout.element.Image logo = new com.itextpdf.layout.element.Image(imageData);
        logo.setHeight(128);
        logo.setWidth(128);
        logo.setFixedPosition(PageSize.A4.getWidth() - 140, PageSize.A4.getHeight() - 140); // Right-top corner

        document.add(logo);

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
        document.add(new Paragraph("Alergias: " + (accountAllergies.isEmpty() ? "Ninguna" : String.join(", ", accountAllergies))));



        //document.add(new Paragraph("IMC: " + imc));

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

            DayMeal dayMeals = result.getDayMeals();
            if (dayMeals != null) {
                table.addCell(new Cell().add(new Paragraph(
                        isNullOrEmpty(getFoodNames(dayMeals.getBreakfast())) ? "No comió." : getFoodNames(dayMeals.getBreakfast())
                )));
                table.addCell(new Cell().add(new Paragraph(
                        isNullOrEmpty(getFoodNames(dayMeals.getLunch())) ? "No comió." : getFoodNames(dayMeals.getLunch())
                )));
                table.addCell(new Cell().add(new Paragraph(
                        isNullOrEmpty(getFoodNames(dayMeals.getDinner())) ? "No comió." : getFoodNames(dayMeals.getDinner())
                )));
                table.addCell(new Cell().add(new Paragraph(
                        isNullOrEmpty(getFoodNames(dayMeals.getSnack())) ? "No comió." : getFoodNames(dayMeals.getSnack())
                )));
                table.addCell(new Cell().add(new Paragraph(
                        isNullOrEmpty(getFoodNames(dayMeals.getOptional())) ? "No comió." : getFoodNames(dayMeals.getOptional())
                )));
            }
            else {
                // Si dayMeals es nulo, llenar todas las celdas con "No disponible"
                for (int i = 0; i < 5; i++) {
                    table.addCell(new Cell().add(new Paragraph("No disponible")));
                }
            }
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
                exerciseTable.addCell(new Cell().add(new Paragraph(exerciseNames.length() > 0 ? exerciseNames.substring(0, exerciseNames.length() - 2) : "No realizado.")));
                exerciseTable.addCell(new Cell().add(new Paragraph(exercise.getTime() + " mins")));
            } else {
                exerciseTable.addCell(new Cell().add(new Paragraph("No realizado.")));
                exerciseTable.addCell(new Cell().add(new Paragraph("No realizado.")));
            }
        }

        document.add(exerciseTable);


        document.add(new Paragraph("Razón de incumplimiento").setFont(boldFont).setFontSize(14));


        Table reasonsTable = new Table(new float[]{3, 3}).useAllAvailableWidth(); // 2 columnas
        reasonsTable.addHeaderCell(new Cell().add(new Paragraph("Fecha").setFont(boldFont)).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        reasonsTable.addHeaderCell(new Cell().add(new Paragraph("Desafío").setFont(boldFont)).setBackgroundColor(ColorConstants.LIGHT_GRAY));

        for (Report result : results) {
            reasonsTable.addCell(new Cell().add(new Paragraph(result.getDate().toString())));
            DayMeal dayMeals = result.getDayMeals();
            String challenge = (dayMeals != null && dayMeals.getReason() != null) ? dayMeals.getReason() : "Satisfactorio.";
            reasonsTable.addCell(new Cell().add(new Paragraph(challenge)));

        }

        document.add(reasonsTable);

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

    private String formatExerciseList(List<ExcerciseType> exercises) {
        if (exercises == null || exercises.isEmpty()) {
            return "";
        }
        return exercises.stream()
                .map(exercise -> exercise.toString().replace("_", " "))  // Reemplaza el guion bajo por un espacio
                .collect(Collectors.joining(", ")); // Une con comas
    }


    private String formatExerciseName(ExcerciseType exerciseType) {
        if (exerciseType == null) {
            return "";
        }
        String formattedName;

        formattedName = exerciseType.name()
                .replaceAll("y", " y ")  // Agrega espacios antes y después de "y"
                .replaceAll("(?<!^)([A-Z])", " $1") // Agrega espacio antes de mayúsculas
                .toLowerCase();

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
        profilePaneSelect.setVisible(false);
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

}