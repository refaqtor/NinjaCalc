package MainWindow;

import Core.Calculator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import CalculatorGridElement.CalculatorGridElementController;

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

    @FXML
    private VBox noCalculatorIsOpenPane;

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

    public void handleCalcSelectionBorderMouseClicked(MouseEvent event) {
        System.out.println("handleCalcSelectionBorderMouseClicked() called.");
        calcGridOverlay.setVisible(false);
        event.consume();
    }

    public void handleCalcSelectionMouseClicked(MouseEvent event) {
        // This is to stop the mouse click event for the border from capturing this event
        // and hiding the selection grid
        event.consume();
    }

    public void addCalculatorTemplate(Calculator calculator) {
        System.out.println("addCalculatorTemplate() called.");

        // Save to internal list
        this.calculatorTemplates.add(calculator);

        CalculatorGridElementController gridElement =
                new CalculatorGridElementController(calculator.IconImagePath, calculator.Name, calculator.Description, this::openCalculatorButtonPressed);

        // Add new tile to tile pane in main window
        calculatorGridTilePane.getChildren().add(gridElement);

    }

    @FXML
    private void onMenuButtonClick(MouseEvent mouseEvent) {
        calcGridOverlay.setVisible(true);
        mouseEvent.consume();
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
        this.gridPaneCalculatorTabsContainer.setVisible(true);

        // Hide the selection grid overlay
        this.calcGridOverlay.setVisible(false);

        // Hide the "No calculator is open" pane
        this.noCalculatorIsOpenPane.setVisible(false);

        // Add new tab to tab pane
        Tab newTab = new Tab();
        newTab.setText(foundCalculator.Name);


        try {
            // Create a new instance of this calculator
            Calculator newInstance = foundCalculator.getClass().newInstance();
            newTab.setContent(newInstance.view);
            this.tabPaneCalculatorInstances.getTabs().add(newTab);
            // Set this newly created tab to be the active one
            this.tabPaneCalculatorInstances.getSelectionModel().selectLast();
        }  catch (InstantiationException x) {
            x.printStackTrace();
        } catch (IllegalAccessException x) {
            x.printStackTrace();
        }

    }


}
