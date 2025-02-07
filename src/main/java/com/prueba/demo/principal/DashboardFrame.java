package com.prueba.demo.principal;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

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
    private TableView<Event> calendarTable;
    @FXML
    private TableColumn<Event, String> mondayColumn;
    @FXML
    private TableColumn<Event, String> tuesdayColumn;
    @FXML
    private TableColumn<Event, String> wednesdayColumn;
    @FXML
    private TableColumn<Event, String> thursdayColumn;
    @FXML
    private TableColumn<Event, String> fridayColumn;

    //ProfilePane
    @FXML
    private VBox profilePane;
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



    //ExercisePane
    @FXML
    private VBox exercisePane;

    //Reports Pane
    @FXML
    private VBox reportsPane;


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
        setTooltipForProgressBar(caloriesProgressBar, carbohydratesTooltip);
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

    @FXML
    private void showProfile() {
        hideAll();
        profilePane.setVisible(true);
        menuVbox.setVisible(true);

    }

    @FXML
    private void showDiet() {
        hideAll();
        dietPane.setVisible(true);
        menuVbox.setVisible(true);

    }

    @FXML
    private void showExercise() {
        hideAll();
        exercisePane.setVisible(true);
        menuVbox.setVisible(true);

    }

    @FXML
    private void showReports() {
        hideAll();
        reportsPane.setVisible(true);
        menuVbox.setVisible(true);

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

//-----ESTO ES PARA LA TABLA DE DIETA CHECAR SI ESTA CORRECTO-----


}


