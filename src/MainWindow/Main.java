package MainWindow;

import Core.Calculator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        /*
        Parent root = FXMLLoader.load(getClass().getResource("MainWindow.fxml"));

        // primaryStage is the main window
        primaryStage.setTitle("NinjaCalc");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();*/

        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainWindow.fxml"));
        Parent root = loader.load();

        // Load the default style sheet
        root.getStylesheets().add("/Core/StyleSheets/default.css");

        MainWindowController controller = loader.getController();

        controller.addCalculatorTemplate(new Calculators.Electronics.Basic.OhmsLaw.OhmsLawCalcModel());
        controller.addCalculatorTemplate(new Calculators.Electronics.Basic.ResistorDivider.ResistorDividerCalcModel());
        controller.addCalculatorTemplate(new Calculators.Electronics.Pcb.TrackCurrentIpc2221A.TrackCurrentIpc2221ACalcModel());

        primaryStage.setTitle("NinjaCalc");
        primaryStage.setScene(new Scene(root, 1000, 800));
        primaryStage.setMaximized(true);

        primaryStage.show();

    }



    public static void main(String[] args) {
        launch(args);
    }


}
