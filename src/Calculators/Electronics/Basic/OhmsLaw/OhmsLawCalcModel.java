
package Calculators.Electronics.Basic.OhmsLaw;

// SYSTEM INCLUDES
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;

// USER INCLUDES

import Core.*;

import java.io.IOException;


public class OhmsLawCalcModel extends Calculator {

    @FXML
    private TextField textFieldVoltageValue;
    @FXML
    private ComboBox comboBoxVoltageUnits;
    @FXML
    private RadioButton radioButtonVoltageIO;
    public CalcVarNumerical Voltage;

    @FXML
    private TextField textFieldCurrentValue;
    @FXML
    private ComboBox comboBoxCurrentUnits;
    @FXML
    private RadioButton radioButtonCurrentIO;
    public CalcVarNumerical Current;

    @FXML
    private TextField textFieldResistanceValue;
    @FXML
    private ComboBox comboBoxResistanceUnits;
    @FXML
    private RadioButton radioButtonResistanceIO;
    public CalcVarNumerical Resistance;

    public OhmsLawCalcModel() {

        super( "Ohm's Law",
                "The hammer in any electrical engineers toolbox. Calculate voltage, resistance and current using Ohm's law.",
                "pack://application:,,,/Calculators/Electronics/Basic/OhmsLaw/grid-icon.png",
                new String[]{ "Electronics", "Basic" },
                new String[]{"ohm, resistor, resistance, voltage, current, law, vir"});



        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("OhmsLawCalcView.fxml"));
        //fxmlLoader.setRoot(this.View);
        fxmlLoader.setController(this);
        try {
            // Create a UI node from the FXML file, and save it to the View variable.
            this.View = fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        //===============================================================================================//
        //============================================ VOLTAGE ==========================================//
        //===============================================================================================//

        this.Voltage = new CalcVarNumerical(
                "voltage",
                textFieldVoltageValue,
                comboBoxVoltageUnits,
                radioButtonVoltageIO,
                () -> {
                    Double current = this.Current.getRawVal();
                    Double resistance = this.Resistance.getRawVal();
                    return current * resistance;
                },
                new NumberUnit[]{
                    new NumberUnit("mV", 1e-3),
                    new NumberUnit("V", 1e0, NumberPreference.DEFAULT),
                    new NumberUnit("kV", 1e3),
                },
                4,
                CalcVarBase.Directions.Input,
                null,
                "The voltage across the resistor." // Help text
                );

        // Add validators
        this.Voltage.AddValidator(Validator.IsNumber(CalcValidationLevels.Error));
        this.Voltage.AddValidator(Validator.IsGreaterThanZero(CalcValidationLevels.Error));

        this.CalcVars.add(this.Voltage);

        //===============================================================================================//
        //============================================ CURRENT ==========================================//
        //===============================================================================================//


        this.Current = new CalcVarNumerical(
                "current",
                textFieldCurrentValue,
                comboBoxCurrentUnits,
                radioButtonCurrentIO,
                //this.CalcVars,
                () -> {
                    Double voltage = this.Voltage.getRawVal();
                    Double resistance = this.Resistance.getRawVal();
                    return voltage / resistance;
                },
                new NumberUnit[]{
                    new NumberUnit("pA", 1e-12),
                    new NumberUnit("nA", 1e-9),
                    new NumberUnit("uA", 1e-6),
                    new NumberUnit("mA", 1e-3),
                    new NumberUnit("A", 1e0, NumberPreference.DEFAULT),
                },
                4,
                CalcVarBase.Directions.Input,
                null,
                "The current going through the resistor" // Help text
                );

        this.Current.AddValidator(Validator.IsNumber(CalcValidationLevels.Error));
        this.Current.AddValidator(Validator.IsGreaterThanZero(CalcValidationLevels.Error));

        this.CalcVars.add(this.Current);

        //===============================================================================================//
        //========================================== RESISTANCE =========================================//
        //===============================================================================================//


        this.Resistance = new CalcVarNumerical(
                "resistance",
                textFieldResistanceValue,
                comboBoxResistanceUnits,
                radioButtonResistanceIO,
                () -> {
                    Double voltage = this.Voltage.getRawVal();
                    Double current = this.Current.getRawVal();
                    return voltage / current;
                },
                new NumberUnit[]{
                    new NumberUnit("mΩ", 1e-3),
                    new NumberUnit("Ω", 1e0, NumberPreference.DEFAULT),
                    new NumberUnit("kΩ", 1e3),
                    new NumberUnit("MΩ", 1e6),
                    new NumberUnit("GΩ", 1e9),
                },
                4,
                CalcVarBase.Directions.Output,
                null,
                "The resistance of the resistor (or other circuit component)." // Help text
                );

        this.Resistance.AddValidator(Validator.IsNumber(CalcValidationLevels.Error));
        this.Resistance.AddValidator(Validator.IsGreaterThanZero(CalcValidationLevels.Error));

        this.CalcVars.add(this.Resistance);

        //===============================================================================================//
        //============================================== FINAL ==========================================//
        //===============================================================================================//

        this.FindDependenciesAndDependants();
        this.RecalculateAllOutputs();
        this.ValidateAllVariables();

    }
}

