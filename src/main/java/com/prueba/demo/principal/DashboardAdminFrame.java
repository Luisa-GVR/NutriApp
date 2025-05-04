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
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.AreaBreakType;
import com.itextpdf.layout.properties.TextAlignment;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.prueba.demo.model.*;
import com.prueba.demo.modelFreeSQL.AccountDataFreeSQL;
import com.prueba.demo.modelFreeSQL.AccountDataFreeSQLHistory;
import com.prueba.demo.modelFreeSQL.AccountFreeSQL;
import com.prueba.demo.repository.*;
import com.prueba.demo.repositoryFreeSQL.AccountDataFreeSQLHistoryRepository;
import com.prueba.demo.repositoryFreeSQL.AccountDataFreeSQLRepository;
import com.prueba.demo.repositoryFreeSQL.AccountFreeSQLRepository;
import com.prueba.demo.service.APIConsumption;
import com.prueba.demo.service.DatabaseService;
import com.prueba.demo.service.IEmailService;
import com.prueba.demo.service.dto.EmailDTO;
import jakarta.mail.MessagingException;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
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
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
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
    private Button profileButton;

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
    private HBox infoHboxProfile;
    @FXML
    private HBox infoHbox;
    @FXML
    private HBox infoHboxReport;
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

    public Map<AccountFreeSQL, AccountDataFreeSQL> getAccountWithDataMap() {
        Map<AccountFreeSQL, AccountDataFreeSQL> result = new HashMap<>();

        List<AccountFreeSQL> accounts = accountFreeSQLRepository.findAll();

        for (AccountFreeSQL account : accounts) {
            AccountDataFreeSQL accountData = account.getAccountDataFreeSQL();
            if (accountData != null) {
                result.put(account, accountData);
            }
        }

        // Remover el usuario con el correo "nutriappunison@gmail.com"
        result.entrySet().removeIf(entry ->
                entry.getKey().getEmail().equalsIgnoreCase("nutriappunison@gmail.com")
        );

        return result;
    }


    Map<AccountFreeSQL, AccountDataFreeSQL> userList;

    @FXML
    private void initialize() {


        infoHboxReport.setVisible(false);
        infoHboxProfile.setVisible(true);

        rootPane.setMinWidth(900);  // Ancho mínimo
        rootPane.setMinHeight(520); // Alto mínimo

        // Asigna eventos a botones
        reportsPane.setVisible(false);

        // Asociar acciones a botones
        profileButton.setOnAction(event -> showProfileEdit());
        reportsButton.setOnAction(event -> showReports());

        //llenar de datos los usuarios
        if (userList == null){
            userList = getAccountWithDataMap();
        }

        showProfilePaneSelect();


        // Ajustar tamaño de fuente basado en el tamaño de la ventana
        List<Button> buttons = Arrays.asList(reportsButton, profileButton);

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
    private void setupListViewWithDesign(ListView<String> listView) {
        listView.setCellFactory(lv -> new ListCell<String>() {
            private final Button nameButton = new Button();
            private final HBox hbox = new HBox(nameButton); // HBox como contenedor

            {
                nameButton.getStyleClass().add("name-button");
                nameButton.setMaxWidth(Double.MAX_VALUE); // Que se expanda

                // Permitir que el botón crezca dentro del HBox
                HBox.setHgrow(nameButton, Priority.ALWAYS);

                nameButton.setOnAction(event -> {
                    String item = getItem();
                    if (item != null) {
                        selectedAccount = userList.keySet().stream()
                                .filter(acc -> acc.getName().equals(item))
                                .findFirst()
                                .orElse(null);

                        selectedAccountData = userList.get(selectedAccount);

                        if (selectedAccount != null) {
                        }

                        goToProfileOrReport();
                    }
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    nameButton.setText(item);
                    setGraphic(hbox); // Usamos el contenedor como graphic
                }
            }
        });
    }


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


    /**
     profile
     */
    @Autowired
    AccountDataRepository accountDataRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountFreeSQLRepository accountFreeSQLRepository;
    @Autowired
    AccountDataFreeSQLRepository accountDataFreeSQLRepository;

    @Autowired
    AccountDataFreeSQLHistoryRepository accountDataFreeSQLHistoryRepository;

    private AccountFreeSQL selectedAccount;
    private AccountDataFreeSQL selectedAccountData;


    @FXML
    private void showProfilePaneSelect(){

        hideAll();
        profilePaneSelect.setVisible(true);
        menuVbox.setVisible(true);


        // Agregar nombres a ListView
        ObservableList<String> userNames = FXCollections.observableArrayList();

        for (AccountFreeSQL account : userList.keySet()) {
            userNames.add(account.getName());
        }

        usersListView.setItems(userNames);

        // Aplicar el cell factory para que se muestre el botón con diseño
        setupListViewWithDesign(usersListView);

        // Filtrar con el TextField
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            ObservableList<String> filteredNames = FXCollections.observableArrayList();

            for (AccountFreeSQL account : userList.keySet()) {
                if (account.getName().toLowerCase().startsWith(newValue.toLowerCase())) {
                    filteredNames.add(account.getName());
                }
            }

            usersListView.setItems(filteredNames);

            // Reaplicar el diseño después del filtro
            setupListViewWithDesign(usersListView);
        });

        // Al dar click en un usuario
        usersListView.setOnMouseClicked(event -> {

            Object selectedName = usersListView.getSelectionModel().getSelectedItem();

            selectedAccount = userList.keySet().stream()
                    .filter(acc -> acc.getName().equals(selectedName))
                    .findFirst()
                    .orElse(null);

            selectedAccountData = userList.get(selectedAccount);

            if (selectedAccount != null) {
                AccountDataFreeSQL data = userList.get(selectedAccount);
            }

        });

    }


    boolean insideReport;
    private void goToProfileOrReport(){

        if (!insideReport){
            showProfileEdit();
            insideReport = false;
        }else {
            showReports();
        }
    }


    @FXML
    private void showProfileEdit() {
        infoHboxReport.setVisible(false);
        infoHboxProfile.setVisible(true);

        if (selectedAccount == null){
            showProfilePaneSelect();
            return;
        }

        hideAll();
        profilePaneEdit.setVisible(true);
        menuVbox.setVisible(true);

        insideReport = false;

        //regresar a elegir usuario + borrar la info que se tenia adentro
        backButtonProfile.setOnAction(event -> {
            // Clear the selected values
            selectedAccount = null;
            selectedAccountData = null;
            startDatePicker.setValue(null);
            endDatePicker.setValue(null);
            startDatePicker.getEditor().clear();
            endDatePicker.getEditor().clear();

            // Go back to profile view
            showProfilePaneSelect();
        });

        Platform.runLater(() -> {

            sexTextArea.setEditable(false);

            Optional<AccountFreeSQL> account = accountFreeSQLRepository.findByEmail(selectedAccount.getEmail());
            AccountDataFreeSQL accountData = account.get().getAccountDataFreeSQL();

            userNameLabel.setText(selectedAccount.getName());


            if (selectedAccountData != null) {
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
            }

            updateButton.setOnAction(event -> {
                try {
                    if (validateFields()) {
                        completeProfile();
                        // Show the info HBox
                        infoHbox.setVisible(true);
                        updateButton.setVisible(false); // Hide the button immediately

                        PauseTransition pause = new PauseTransition(Duration.seconds(3));
                        pause.setOnFinished(e -> {
                            infoHbox.setVisible(false);
                            updateButton.setVisible(true);
                        });
                        pause.play();


                    }
                } catch (Exception ex) {
                    ex.printStackTrace(); // Always a good idea to log the error
                }

            });


        });

    }

    private boolean validateFields() {
        boolean validInputs = true;

        // Validar edad (13 - 120) (Solo enteros)
        validInputs &= isValidNumber(ageTextArea, 13, 120, "Edad", ageErrorLabel, 0, true);
        ageTextArea.setOnMouseClicked(event -> ageErrorLabel.setVisible(false));


        // Validar estatura (90 - 300 cm)
        validInputs &= isValidNumber(heightTextArea, 90, 300, "Estatura", heightErrorLabel, 0, false);
        heightTextArea.setOnMouseClicked(event -> heightErrorLabel.setVisible(false));


        // Validar peso inicial (30 - 300 kg)
        validInputs &= isValidNumber(weightTextArea, 30, 300, "Peso inicial", weightErrorLabel, 0, false);
        weightTextArea.setOnMouseClicked(event -> weightErrorLabel.setVisible(false));


        // Validar abdomen ((40 - 170 cm)
        validInputs &= isValidNumber(abdomenTextArea, 40, 170, "Abdomen", abdomenErrorLabel, 1, false);
        abdomenTextArea.setOnMouseClicked(event -> abdomenErrorLabel.setVisible(false));


        // Validar cadera (50 - 170 cm)
        validInputs &= isValidNumber(hipTextArea, 50, 170, "Cadera", hipErrorLabel, 1, false);
        hipTextArea.setOnMouseClicked(event -> hipErrorLabel.setVisible(false));


        // Validar cintura (35 - 170 cm)
        validInputs &= isValidNumber(waistTextArea, 35, 170, "Cintura", waistErrorLabel, 1, false);
        waistTextArea.setOnMouseClicked(event -> waistErrorLabel.setVisible(false));


        // Validar cuello ((15 - 170 cm)
        validInputs &= isValidNumber(neckTextArea, 15, 170, "Cuello", neckErrorLabel, 1, false);
        neckTextArea.setOnMouseClicked(event -> neckErrorLabel.setVisible(false));


        // Validar brazo (10 - 170 cm)
        validInputs &= isValidNumber(armTextArea, 10, 170, "Brazo", armErrorLabel, 1, false);
        armTextArea.setOnMouseClicked(event -> armErrorLabel.setVisible(false));


        // Validar pecho (50 - 170 cm)
        validInputs &= isValidNumber(chestTextArea, 50, 170, "Pecho", chestErrorLabel, 1, false);
        chestTextArea.setOnMouseClicked(event -> chestErrorLabel.setVisible(false));

        return validInputs;
    }


    //Lo mismo que hay en ProfileFrame, ligeramente cambiado

    private void completeProfile() {
        Optional<AccountFreeSQL> accountOpt = accountFreeSQLRepository.findByEmail(selectedAccount.getEmail());

        //Crear la informacion del usuario con la info nueva
        if (accountOpt.isPresent()) {
            //
            AccountFreeSQL account = accountOpt.get();

            AccountDataFreeSQL accountData = account.getAccountDataFreeSQL();

            if (accountData == null) {
                accountData = new AccountDataFreeSQL();
                accountData.setAccountFreeSQL(account);
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

            accountDataFreeSQLRepository.save(accountData);

            account.setAccountDataFreeSQL(accountData);
            accountFreeSQLRepository.save(account);

            //crear el historial
            AccountDataFreeSQLHistory accountDataFreeSQLHistory = new AccountDataFreeSQLHistory();
            accountDataFreeSQLHistory.setAccountFreeSQL(account);

            // Actualizar valores
            accountDataFreeSQLHistory.setGender(accountData.getGender());
            accountDataFreeSQLHistory.setAge(accountData.getAge());
            accountDataFreeSQLHistory.setHeight(accountData.getHeight() != null ? accountData.getHeight() : 0);
            accountDataFreeSQLHistory.setWeight(accountData.getWeight() != null ? accountData.getWeight() : 0);
            accountDataFreeSQLHistory.setAbdomen(accountData.getAbdomen() != null ? accountData.getAbdomen() : 0);
            accountDataFreeSQLHistory.setHips(accountData.getHips() != null ? accountData.getHips() : 0);
            accountDataFreeSQLHistory.setWaist(accountData.getWaist() != null ? accountData.getWaist() : 0);
            accountDataFreeSQLHistory.setArm(accountData.getArm() != null ? accountData.getArm() : 0);
            accountDataFreeSQLHistory.setChest(accountData.getChest() != null ? accountData.getChest() : 0);
            accountDataFreeSQLHistory.setNeck(accountData.getNeck() != null ? accountData.getNeck() : 0);
            java.util.Date now = new java.util.Date();
            java.sql.Date sqlDate = new java.sql.Date(now.getTime());

            accountDataFreeSQLHistory.setDate(sqlDate);

            accountDataFreeSQLHistoryRepository.save(accountDataFreeSQLHistory);

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
     reportes
     */

    @FXML
    private void showReports() {
        infoHboxReport.setVisible(true);
        infoHboxProfile.setVisible(false);

        insideReport = true;

        if (selectedAccount == null){
            showProfilePaneSelect();
            return;
        }



        hideAll();
        reportsPane.setVisible(true);
        menuVbox.setVisible(true);

        disableVBox(reportsPane);

        backButtonReports.setOnAction(event -> {
            // Clear the selected values
            selectedAccount = null;
            selectedAccountData = null;

            startDatePicker.setValue(null);
            endDatePicker.setValue(null);
            startDatePicker.getEditor().clear();
            endDatePicker.getEditor().clear();


            // Go back to profile view
            showProfilePaneSelect();
        });


        Date oldestReportDate = accountDataFreeSQLHistoryRepository.findOldestDateByAccountId(selectedAccount.getId());

        if (oldestReportDate == null) {
            // Si no hay fecha, deshabilitar completamente el startDatePicker
            startDatePicker.setDisable(true);

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

        Optional<AccountFreeSQL> account = accountFreeSQLRepository.findByEmail(selectedAccount.getEmail());
        AccountDataFreeSQL accountData = account.get().getAccountDataFreeSQL();

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

        Platform.runLater(() -> {
            startDatePicker.getEditor().setEditable(false);
            endDatePicker.getEditor().setEditable(false);
        });

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
        String dest = "toSendPDFNutriologa.pdf";
        PdfWriter writer = new PdfWriter(dest);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf, PageSize.A4);

        PdfFont boldFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        PdfFont normalFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);

        // Datos del usuario
        String name = selectedAccount.getName();
        String email = selectedAccount.getEmail();

        Optional<AccountFreeSQL> account = accountFreeSQLRepository.findByEmail(email);

        AccountDataFreeSQL accountData = account.get().getAccountDataFreeSQL();
        int age = accountData.getAge();
        String gender = accountData.getGender() ? "Masculino" : "Femenino";
        Double weight = accountData.getWeight();
        Double height = accountData.getHeight();

        Double abdomen = accountData.getAbdomen();
        Double hips = accountData.getHips();
        Double waist = accountData.getWaist();
        Double arm = accountData.getArm();
        Double chest = accountData.getChest();
        Double neck = accountData.getNeck();

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
        document.add(new Paragraph("Datos del Usuario actuales").setFont(boldFont).setFontSize(14));
        document.add(new Paragraph("Nombre: " + name));
        document.add(new Paragraph("Correo: " + email));
        document.add(new Paragraph("Edad: " + age));
        document.add(new Paragraph("Género: " + gender));
        document.add(new Paragraph("Peso: " + weight + " kg"));
        document.add(new Paragraph("Altura: " + height + " m"));


        //document.add(new Paragraph("IMC: " + imc));

        document.add(new Paragraph("Medidas Corporales actuales").setFont(boldFont).setFontSize(14));
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

        List<AccountDataFreeSQLHistory> historyList = accountDataFreeSQLHistoryRepository
                .findByAccountFreeSQL(account.get());

        historyList.sort(Comparator.comparing(AccountDataFreeSQLHistory::getDate));

        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        LocalDate current = startDate;

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        Double lastAbdomen = null, lastHips = null, lastWaist = null;
        Double lastArm = null, lastChest = null, lastNeck = null;

        Map<LocalDate, AccountDataFreeSQLHistory> historyMap = new HashMap<>();
        for (AccountDataFreeSQLHistory record : historyList) {
            historyMap.put(record.getDate().toLocalDate(), record);
        }

        while (!current.isAfter(endDate)) {
            AccountDataFreeSQLHistory record = historyMap.get(current);
            String dateStr = current.toString();

            boolean shouldAdd = false;

            // Actualizar los "últimos conocidos" si hay nuevos datos
            if (record != null) {
                if (record.getAbdomen() != null && !record.getAbdomen().equals(lastAbdomen)) {
                    lastAbdomen = record.getAbdomen(); shouldAdd = true;
                }
                if (record.getHips() != null && !record.getHips().equals(lastHips)) {
                    lastHips = record.getHips(); shouldAdd = true;
                }
                if (record.getWaist() != null && !record.getWaist().equals(lastWaist)) {
                    lastWaist = record.getWaist(); shouldAdd = true;
                }
                if (record.getArm() != null && !record.getArm().equals(lastArm)) {
                    lastArm = record.getArm(); shouldAdd = true;
                }
                if (record.getChest() != null && !record.getChest().equals(lastChest)) {
                    lastChest = record.getChest(); shouldAdd = true;
                }
                if (record.getNeck() != null && !record.getNeck().equals(lastNeck)) {
                    lastNeck = record.getNeck(); shouldAdd = true;
                }
            }

            // Asegura incluir la primera y última fecha
            if (current.equals(startDate) || current.equals(endDate)) {
                shouldAdd = true;
            }

            if (shouldAdd) {
                if (lastAbdomen != null) dataset.addValue(lastAbdomen, "Abdomen", dateStr);
                if (lastHips != null) dataset.addValue(lastHips, "Caderas", dateStr);
                if (lastWaist != null) dataset.addValue(lastWaist, "Cintura", dateStr);
                if (lastArm != null) dataset.addValue(lastArm, "Brazo", dateStr);
                if (lastChest != null) dataset.addValue(lastChest, "Pecho", dateStr);
                if (lastNeck != null) dataset.addValue(lastNeck, "Cuello", dateStr);
            }

            current = current.plusDays(1);
        }

        if (dataset.getColumnCount() == 1) {
            String onlyDate = (String) dataset.getColumnKeys().get(0);
            LocalDate date = LocalDate.parse(onlyDate);
            String nextDate = date.plusDays(1).toString();

            // Repeat the same values on nextDate
            if (lastAbdomen != null) dataset.addValue(lastAbdomen, "Abdomen", nextDate);
            if (lastHips != null) dataset.addValue(lastHips, "Caderas", nextDate);
            if (lastWaist != null) dataset.addValue(lastWaist, "Cintura", nextDate);
            if (lastArm != null) dataset.addValue(lastArm, "Brazo", nextDate);
            if (lastChest != null) dataset.addValue(lastChest, "Pecho", nextDate);
            if (lastNeck != null) dataset.addValue(lastNeck, "Cuello", nextDate);
        }



        JFreeChart lineChart = ChartFactory.createLineChart(
                "Histórico de Medidas Corporales",
                "Fecha",
                "Medida (cm)",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);


        ByteArrayOutputStream chartOut = new ByteArrayOutputStream();
        ChartUtils.writeChartAsPNG(chartOut, lineChart, 600, 400);
        ImageData chartImage = ImageDataFactory.create(chartOut.toByteArray());

        com.itextpdf.layout.element.Image chart = new com.itextpdf.layout.element.Image(chartImage);
        chart.setAutoScale(true);
        document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));

        document.add(chart);

        document.close();

    }

    @Autowired
    private IEmailService iEmailService;

    private void sendEmailWithPDFAttachment() throws MessagingException, IOException {
        // Ruta del archivo PDF que queremos adjuntar
        String pdfFilePath = "toSendPDFNutriologa.pdf";

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