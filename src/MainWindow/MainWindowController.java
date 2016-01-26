package MainWindow;

import Core.Calculator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.*;

import CalculatorGridElement.CalculatorGridElementController;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.util.ArrayList;

public class MainWindowController {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private GridPane calcGridOverlay;

    @FXML
    private TilePane calculatorGridTilePane;

    @FXML
    private GridPane gridPaneCalculatorTabsContainer;

    @FXML
    private TabPane tabPaneCalculatorInstances;

    private ArrayList<Calculator> calculatorTemplates;

    //===============================================================================================//
    //======================================== CONSTRUCTORS =========================================//
    //===============================================================================================//

    public MainWindowController() {
        this.calculatorTemplates = new ArrayList<Calculator>();
    }

    public void handleButtonOnAction(ActionEvent actionEvent) {
        //Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "blah");
        //alert.showAndWait();

        // Show the calculator grid overlay
        calcGridOverlay.setVisible(true);

    }

    public void addCalculatorTemplate(Calculator calculator) {
        System.out.println("addCalculatorTemplate() called.");

        // Save to internal list
        this.calculatorTemplates.add(calculator);

        CalculatorGridElementController gridElement =
                new CalculatorGridElementController(calculator.Name, this::openCalculatorButtonPressed);

        // Add new tile to tile pane in main window
        calculatorGridTilePane.getChildren().add(gridElement);

    }

    public void openCalculatorButtonPressed(String calculatorName) {
        System.out.println("openCalculatorButtonPressed() called, with calculatorName = \"" + calculatorName +  "\" .");


        Calculator foundCalculator = null;
        // Search for calculator in list of calculator templates
        for(Calculator calculator : this.calculatorTemplates) {
            if(calculator.Name == calculatorName) {
                foundCalculator = calculator;
            }
        }

        assert foundCalculator != null;

        System.out.println("Opening new calculator instance...");

        // Make calculator tabs visible
        gridPaneCalculatorTabsContainer.setVisible(true);

        // Hide the selection grid overlay
        calcGridOverlay.setVisible(false);

        // Add new tab to tab pane
        Tab newTab = new Tab();
        newTab.setText(foundCalculator.Name);
        newTab.setContent(foundCalculator.View);
        //newTab.setContent(new Rectangle(200,200, Color.LIGHTSTEELBLUE));
        //newTab.getContent().setStyle("-fx-background-color: purple");

        this.tabPaneCalculatorInstances.getTabs().add(newTab);

        //calculatorTemplates.add(calculator);
        // Create a new FXML loader, pointing to the provided fxml path
        /*FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxmlPath));
        try {
            GridPane root = loader.load();
            //calculatorGridTilePane.getChildren().add(root);
        }
        catch (IOException e) {
            e.printStackTrace();
        }*/
    }


    /*private Pane getOverlay() {
        StackPane p = new StackPane();
        Rectangle r = RectangleBuilder.create()
                .height(100).width(100)
                .arcHeight(40).arcWidth(40)
                .stroke(Color.RED)
                .fill(Color.web("red", 0.1))
                .build();

        Text txt= TextBuilder.create().text("Overlay")
                .font(Font.font("Arial", FontWeight.BOLD, 18))
                .fill(Color.BLUE)
                .build();
        p.getChildren().addAll(r, txt);
        return p;
    }*/
}
