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

        rootPane.setMinWidth(900);  // Ancho mínimo
        rootPane.setMinHeight(520); // Alto mínimo

        // Asigna eventos a botones
        reportsPane.setVisible(false);

        // Asociar acciones a botones
        profileButton.setOnAction(event -> showProfile());
        reportsButton.setOnAction(event -> showReports());

        //llenar de datos los usuarios
        if (userList == null){
            userList = getAccountWithDataMap();
        }



        showProfile();



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
                            System.out.println("Edad: " + selectedAccountData.getAge());
                        }

                        showProfileEdit();
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

    private AccountFreeSQL selectedAccount;
    private AccountDataFreeSQL selectedAccountData;




    @FXML
    private void showProfile() {

        if (selectedAccount != null){
            showProfileEdit();
            return;
        }

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
                System.out.println(data.getAge());
            }

            showProfileEdit();
        });
    }




    @FXML
    private void showProfileEdit() {
        hideAll();
        profilePaneEdit.setVisible(true);
        menuVbox.setVisible(true);

        //regresar a elegir usuario + borrar la info que se tenia adentro
        backButtonProfile.setOnAction(event -> {
            // Clear the selected values
            selectedAccount = null;
            selectedAccountData = null;

            // Go back to profile view
            showProfile();
        });

        Platform.runLater(() -> {
            if (selectedAccount != null) {
                userNameLabel.setText(selectedAccount.getName());
            }

            if (selectedAccountData != null) {
                ageTextArea.setText(String.valueOf(selectedAccountData.getAge()));
                heightTextArea.setText(String.valueOf(selectedAccountData.getHeight()));
                weightTextArea.setText(String.valueOf(selectedAccountData.getWeight()));
                abdomenTextArea.setText(String.valueOf(selectedAccountData.getAbdomen()));
                hipTextArea.setText(String.valueOf(selectedAccountData.getHips()));
                waistTextArea.setText(String.valueOf(selectedAccountData.getWaist()));
                neckTextArea.setText(String.valueOf(selectedAccountData.getNeck()));
                armTextArea.setText(String.valueOf(selectedAccountData.getArm()));
                chestTextArea.setText(String.valueOf(selectedAccountData.getChest()));
            }
        });

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

        // Hacer que los campos de sexo y alergias sean de solo lectura
        sexTextArea.setEditable(false);
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
     reportes
     */

    @FXML
    private void showReports() {
        hideAll();
        reportsPane.setVisible(true);
        menuVbox.setVisible(true);


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