package Calculators.Electronics.Pcb.ViaCurrentIpc2221A;


import Core.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.IOException;
import java.net.URL;

/**
 * A via current calculator based of the IPC-2221A standard.
 *
 * @author          gbmhunter <gbmhunter@gmail.com> (www.mbedded.ninja)
 * @since           2016-04-24
 * @last-modified   2016-04-24
 */
public class ViaCurrentIpc2221ACalcModel extends Calculator {

    //===============================================================================================//
    //========================================= FXML BINDINGS =======================================//
    //===============================================================================================//

    @FXML
    private WebView infoWebView;

    @FXML
    private TextField finishedHoleDiameterValue;
    @FXML
    private ComboBox finishedHoleDiameterUnits;

    @FXML
    private TextField platingThicknessValue;
    @FXML
    private ComboBox platingThicknessUnits;

    @FXML
    private TextField viaLengthValue;
    @FXML
    private ComboBox viaLengthUnits;

    @FXML
    private TextField platedCopperResistivityValue;
    @FXML
    private ComboBox platedCopperResistivityUnits;

    @FXML
    private TextField viaCrossSectionalAreaValue;
    @FXML
    private ComboBox viaCrossSectionalAreaUnits;

    @FXML
    private TextField currentLimitValue;
    @FXML
    private ComboBox currentLimitUnits;

    //===============================================================================================//
    //====================================== CALCULATOR VARIABLES ===================================//
    //===============================================================================================//

    public CalcVarNumericalInput finishedHoleDiameter_M = new CalcVarNumericalInput();
    public CalcVarNumericalInput platingThickness_M = new CalcVarNumericalInput();
    public CalcVarNumericalInput viaLength_M = new CalcVarNumericalInput();

    public CalcVarNumericalInput platedCopperResistivity_OhmMeter = new CalcVarNumericalInput();

    public CalcVarNumericalOutput viaCrossSectionalArea_M2 = new CalcVarNumericalOutput();
    public CalcVarNumericalOutput currentLimit = new CalcVarNumericalOutput();

    //===============================================================================================//
    //========================================== CONSTRUCTORS =======================================//
    //===============================================================================================//

    public ViaCurrentIpc2221ACalcModel() {

        super(
                "Via Current (IPC-2221A)",
                "PCB via current carrying capability calculator, using the IPC-2221A standard.",
                new String[]{"Electronics", "PCB"},
                new String[]{"pcb", "via", "current", "width", "carry", "heat", "hot", "temperature", "ipc", "ipc2221a", "ipc-2221a"});

        super.setIconImagePath(getClass().getResource("img/grid-icon.png"));

        //===============================================================================================//
        //======================================== LOAD .FXML FILE ======================================//
        //===============================================================================================//

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ViaCurrentIpc2221ACalcView.fxml"));
        //fxmlLoader.setRoot(this.view);
        fxmlLoader.setController(this);
        try {
            // Create a UI node from the FXML file, and save it to the view variable.
            // This will be used by the main window to create a new instance of this calculator when
            // the "Open" button is clicked.
            this.view = fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        //===============================================================================================//
        //================================ LOAD WEB VIEW FOR INFO SECTION ===============================//
        //===============================================================================================//

        WebEngine engine = this.infoWebView.getEngine();
        final String htmlFile = "info.html";
        URL url = getClass().getResource(htmlFile);
        engine.load(url.toExternalForm());

        //===============================================================================================//
        //================================ FINISHED HOLE DIAMETER (input) ===============================//
        //===============================================================================================//

        this.finishedHoleDiameter_M.setName("finishedHoleDiameter_M");
        this.finishedHoleDiameter_M.setValueTextField(this.finishedHoleDiameterValue);
        this.finishedHoleDiameter_M.setUnitsComboBox(this.finishedHoleDiameterUnits);
        this.finishedHoleDiameter_M.setUnits(new NumberUnit[]{
                new NumberUnitMultiplier("um", 1e-6),
                new NumberUnitMultiplier("mm", 1e-3, NumberPreference.DEFAULT),
        });
        this.finishedHoleDiameter_M.setNumDigitsToRound(4);
        this.finishedHoleDiameter_M.setHelpText("The finished hole diameter of the via. This is not the same as the drilled hole diameter, as the via is then plated.");
        this.finishedHoleDiameter_M.setIsEngineeringNotationEnabled(false);

        //===== VALIDATORS =====//
        this.finishedHoleDiameter_M.addValidator(Validator.IsNumber(CalcValidationLevels.Error));
        this.finishedHoleDiameter_M.addValidator(Validator.IsGreaterThanZero(CalcValidationLevels.Error));

        this.calcVars.add(this.finishedHoleDiameter_M);

        //===============================================================================================//
        //================================== PLATING THICKNESS (input) ==================================//
        //===============================================================================================//

        this.platingThickness_M.setName("platingThickness_M");
        this.platingThickness_M.setValueTextField(this.platingThicknessValue);
        this.platingThickness_M.setUnitsComboBox(this.platingThicknessUnits);
        this.platingThickness_M.setUnits(new NumberUnit[]{
                new NumberUnitMultiplier("um", 1e-6, NumberPreference.DEFAULT),
                new NumberUnitMultiplier("oz", UnitConversionConstants.COPPER_THICKNESS_M_PER_OZ),
                new NumberUnitMultiplier("mils", UnitConversionConstants.METERS_PER_MILS),
        });
        this.platingThickness_M.setNumDigitsToRound(4);
        this.platingThickness_M.setHelpText("The plating thickness of the via walls. This is usually the same as the thickness of the start and end copper layers that the via connects to.");
        this.platingThickness_M.setIsEngineeringNotationEnabled(false);

        //===== VALIDATORS =====//
        this.platingThickness_M.addValidator(Validator.IsNumber(CalcValidationLevels.Error));
        this.platingThickness_M.addValidator(Validator.IsGreaterThanZero(CalcValidationLevels.Error));

        this.calcVars.add(this.platingThickness_M);

        //===============================================================================================//
        //======================================== VIA LENGTH (input) ===================================//
        //===============================================================================================//

        this.viaLength_M.setName("viaLength_M");
        this.viaLength_M.setValueTextField(this.viaLengthValue);
        this.viaLength_M.setUnitsComboBox(this.viaLengthUnits);
        this.viaLength_M.setUnits(new NumberUnit[]{
                new NumberUnitMultiplier("um", 1e-6),
                new NumberUnitMultiplier("mm", 1e-3, NumberPreference.DEFAULT),
                new NumberUnitMultiplier("mils", UnitConversionConstants.METERS_PER_MILS),
        });
        this.viaLength_M.setNumDigitsToRound(4);
        this.viaLength_M.setHelpText("The length of the via. This is equal to the distance between the copper planes the via starts and ends on. For a simple 2-layer 1.6mm thick PCB, the via height is also 1.6mm. This could also be called the height of the via.");
        this.viaLength_M.setIsEngineeringNotationEnabled(false);

        //===== VALIDATORS =====//
        this.viaLength_M.addValidator(Validator.IsNumber(CalcValidationLevels.Error));
        this.viaLength_M.addValidator(Validator.IsGreaterThanZero(CalcValidationLevels.Error));

        this.calcVars.add(this.viaLength_M);

        //===============================================================================================//
        //================================ PLATED COPPER RESISTIVITY (input) ============================//
        //===============================================================================================//

        this.platedCopperResistivity_OhmMeter.setName("platedCopperResistivity_OhmMeter");
        this.platedCopperResistivity_OhmMeter.setValueTextField(this.platedCopperResistivityValue);
        this.platedCopperResistivity_OhmMeter.setUnitsComboBox(this.platedCopperResistivityUnits);
        this.platedCopperResistivity_OhmMeter.setUnits(new NumberUnit[]{
                new NumberUnitMultiplier("Ω⋅m", 1e0),
        });
        this.platedCopperResistivity_OhmMeter.setNumDigitsToRound(4);
        this.platedCopperResistivity_OhmMeter.setHelpText("The resistivity of the plated copper which the via is made from.");
        this.platedCopperResistivity_OhmMeter.setIsEngineeringNotationEnabled(false);

        //===== VALIDATORS =====//
        this.platedCopperResistivity_OhmMeter.addValidator(Validator.IsNumber(CalcValidationLevels.Error));
        this.platedCopperResistivity_OhmMeter.addValidator(Validator.IsGreaterThanZero(CalcValidationLevels.Error));

        this.calcVars.add(this.platedCopperResistivity_OhmMeter);


        //===============================================================================================//
        //================================ VIA CROSS-SECTIONAL AREA (output) ============================//
        //===============================================================================================//

        this.viaCrossSectionalArea_M2.setName("viaCrossSectionalArea_M2");
        this.viaCrossSectionalArea_M2.setValueTextField(this.viaCrossSectionalAreaValue);
        this.viaCrossSectionalArea_M2.setUnitsComboBox(this.viaCrossSectionalAreaUnits);
        this.viaCrossSectionalArea_M2.setEquationFunction(() -> {
            // Read dependencies
            Double finishedHoleDiameter_M = this.finishedHoleDiameter_M.getRawVal();
            Double platingThickness_M = this.platingThickness_M.getRawVal();

            return Math.PI * (finishedHoleDiameter_M + platingThickness_M) * platingThickness_M;

        });
        this.viaCrossSectionalArea_M2.setUnits(new NumberUnitMultiplier[]{
                new NumberUnitMultiplier("m2", 1e0, NumberPreference.DEFAULT),
        });
        this.viaCrossSectionalArea_M2.setNumDigitsToRound(4);
        this.viaCrossSectionalArea_M2.setHelpText("The cross-sectional area of the via (the area of the via as viewed from the top down).");
        this.viaCrossSectionalArea_M2.setIsEngineeringNotationEnabled(false);

        // Add validators
        this.viaCrossSectionalArea_M2.addValidator(Validator.IsNumber(CalcValidationLevels.Error));
        this.viaCrossSectionalArea_M2.addValidator(Validator.IsGreaterThanZero(CalcValidationLevels.Error));

        this.calcVars.add(this.viaCrossSectionalArea_M2);

        //===============================================================================================//
        //====================================== CURRENT LIMIT (output) =================================//
        //===============================================================================================//

        this.currentLimit.setName("currentLimit");
        this.currentLimit.setValueTextField(this.currentLimitValue);
        this.currentLimit.setUnitsComboBox(this.currentLimitUnits);
        this.currentLimit.setEquationFunction(() -> {
            return 0.0;

        });
        this.currentLimit.setUnits(new NumberUnitMultiplier[]{
                new NumberUnitMultiplier("A", 1e0, NumberPreference.DEFAULT),
        });
        this.currentLimit.setNumDigitsToRound(4);
        this.currentLimit.setHelpText("The maximum current the via can take before it rises to the specified temperature above ambient.");
        this.currentLimit.setIsEngineeringNotationEnabled(false);

        // Add validators
        this.currentLimit.addValidator(Validator.IsNumber(CalcValidationLevels.Error));
        this.currentLimit.addValidator(Validator.IsGreaterThanZero(CalcValidationLevels.Error));

        this.calcVars.add(this.currentLimit);

        //===============================================================================================//
        //=========================================== VIEW CONFIG =======================================//
        //===============================================================================================//

        // Setup the top PCB layer to dissappear if "External" is selected for the track layer,
        // and visible if "Internal" is selected.
        /*this.trackLayer.RawValueChanged += (sender, e) => {
            if (this.trackLayer.RawVal == "Internal") {
                view.TopPcb.Visibility = System.Windows.Visibility.Visible;
            }
            else if (this.trackLayer.RawVal == "External") {
                view.TopPcb.Visibility = System.Windows.Visibility.Collapsed;
            }
        };*/

        //===============================================================================================//
        //============================================== FINAL ==========================================//
        //===============================================================================================//

        this.findDependenciesAndDependants();
        this.refreshDirectionsAndUpdateUI();
        this.recalculateAllOutputs();
        this.validateAllVariables();

    }
}
