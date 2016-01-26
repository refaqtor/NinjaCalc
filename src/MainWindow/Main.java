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

        MainWindowController controller = loader.getController();

        Calculator calculator = new Calculators.Electronics.Basic.OhmsLaw.OhmsLawCalcModel();
        controller.addCalculatorTemplate(calculator);

        primaryStage.setTitle("NinjaCalc");
        primaryStage.setScene(new Scene(root, 1000, 800));
        primaryStage.show();

    }



    public static void main(String[] args) {
        launch(args);
    }


}
