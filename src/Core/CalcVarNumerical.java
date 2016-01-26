
package Core;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.Border;
import javafx.util.StringConverter;

import java.util.ArrayList;
//import javafx.scene.control.RadioButton;


/// <summary>
/// Encapsulates a single numerical variable in a NinjaCalc calculator (inherits from CalcVarBase).
/// Stores the variable name, it's equation, it's state (input or output).
/// Is further inherited by specialised input and output variable classes.
/// </summary>
public class CalcVarNumerical extends CalcVarBase {

    //===============================================================================================//
    //==================================== VARIABLES AND PROPERTIES =================================//
    //===============================================================================================//

    //============================================= RAW VAL =========================================//

    protected double rawVal;

    /// <summary>
    /// Gets or sets the the "raw" (unscaled, unrounded) value for this variable. Setting will cause the displayed value, textbox, and all
    /// dependant variables to update.
    /// </summary>
    public double getRawVal() {
        this.OnRawValueRead();

        return this.rawVal;
    }

    public void setRawVal(double value) {
        // Only set if new value is different from current
        if (this.rawVal != value) {
            this.rawVal = value;
            // Fire the RawValueChanged event handler
            this.OnRawValueChanged();
        }
    }

    //private event EventHandler RawValueChanged;

    /// <summary>
    /// Fires the RawValueChanged event handler (as long as it's not null).
    /// </summary>
    /// <param name="e">The event arguments you wish to provide to listening methods.</param>
    /*public virtual void OnRawValueChanged(EventArgs e) {
        EventHandler handler = RawValueChanged;
        if (handler != null) {
            handler(this, e);
        }
    }*/

    //============================================ DISP VAL =========================================//

    public double dispVal;

    private TextField calcValTextBox;
    private ChangeListener<String> textListener;

    private RadioButton ioRadioButton;

    //private Dictionary<string, CalcVar> calcVars;

    //private Directions direction;

    public Directions getDirection() {
        return this.direction;
    }

    public void setDirection(Directions value){
        System.out.println("setDirection() called with direction = \"" + value.toString() + "\" for variable \"" + this.Name + "\".");
        this.direction = value;
        if (value == Directions.Output) {
            // If this calc variable is being set as an output,
            // we need to disable the input text box, check the radio button,
            // and remove the event handler
            this.calcValTextBox.setDisable(true);
            //this.calcValTextBox.setBorder(new Border()) = new System.Windows.Thickness(0);

            // Remove event handler from output

            this.calcValTextBox.textProperty().removeListener(this.textListener);
            /*this.calcValTextBox.onActionProperty() -= this.TextBoxChanged;
            if (this.ioRadioButton != null) {
                this.ioRadioButton.IsChecked = true;
            }*/

            // Now the event handler is removed, update the displayed value for the raw value
            // (this is so rounding occurs)
            this.UpdateDispValFromRawVal();

        }
        else if (value == Directions.Input) {

            this.calcValTextBox.setDisable(false);
            //this.calcValTextBox.BorderThickness = new System.Windows.Thickness(3);

            //this.calcValTextBox.setOnAction(this::TextBoxChanged);

            //this.calcValTextBox.setOnAction(this.calcValTextBoxChanged);

            this.calcValTextBox.textProperty().addListener(textListener);

            /*this.calcValTextBox.TextChanged += this.TextBoxChanged;
            if (this.ioRadioButton != null) {
                this.ioRadioButton.IsChecked = false;
            }*/
        }

    }


    private ArrayList<Validator> validators;

    public ArrayList<CalcValidationResult> ValidationResults;

    private CalcValidationLevel validationResult;

    /// <summary>
    /// Gets or sets the validation result for this calculator variable.
    /// Will also change the border colour of the associated text box.
    /// </summary>
    public CalcValidationLevel WorstValidationLevel;

    private ComboBox unitsComboBox;

    /**
     * Uses an observable list so that it can be bound to the combo box.
     * See http://code.makery.ch/blog/javafx-8-event-handling-examples/
     */
    public ObservableList<NumberUnit> Units;

    /// <summary>
    /// Do NOT access this from anything put the SelectionChanged event handler for
    /// the ComboBox.
    /// </summary>
    private NumberUnit selUnit;

    /// <summary>
    /// Gets and sets the selected unit for this calculator variable. If set, it will also update
    /// the associated ComboBox on the UI.
    /// </summary>
    public NumberUnit getSelUnit()
    {
        return this.selUnit;
    }

    /**
     *
     * @param value
     */
    public void setSelUnit(NumberUnit value) {
        this.selUnit = value;
        // Anytime this is set, also update selected value in combobox
        this.unitsComboBox.getSelectionModel().select(this.selUnit);
    }


    public int NumDigitsToRound;

    public String HelpText;

    private ToggleGroup radioButtonToggleGroup;

    //===============================================================================================//
    //========================================== CONSTRUCTORS =======================================//
    //===============================================================================================//

    /// <summary>
    /// Base constructor. Requires all possible arguments.
    /// </summary>
    /// <param name="calcValTextBox">The text box that displays this calculator variables value.</param>
    /// <param name="equation">An expression tree of a function which calculates this variables value from the other variables.</param>
    /// <param name="defaultRawValue">Default number for raw value to be set to. Can also be set to null, in which case the displayed text will be empty.</param>
    public CalcVarNumerical(
        String name,
        TextField calcValTextBox,
        ComboBox unitsComboBox,
        RadioButton ioRadioButton,
        ToggleGroup radioButtonToggleGroup,
        IEquation equation,
        NumberUnit[] units,
        int numDigitsToRound,
        Directions defaultDirection,
        Double defaultRawValue,
        String helpText)
    {

        super(name, equation);

        // Create text field listener
        this.textListener = (observable, oldValue, newValue) -> {
            System.out.println("CalcVarNumerical.TextBoxChanged() called. Text changed from \"" + oldValue + "\" to \"" + newValue + "\".");

            // Make sure this event only fires when this variable is an input!
            assert this.getDirection() == Directions.Input;

            // Save this to the raw value
            // (bypass setting the property as we don't want to update the TextBox)
            // This could throw a System.FormatException if the value can't be converted into a double,
            // for example, if it had letters (a2) or was just a negative sign (-).
            //try {
            this.dispVal = Double.valueOf(newValue);
            this.rawVal = this.dispVal * this.selUnit.Multiplier;
            //}
            //catch (System.FormatException exception) {
            //    this.dispVal = Double.NaN;
            //    this.rawVal = Double.NaN;
            //}

            this.Validate();

            // We need to re-calculate any this calculator variables dependants, if they are outputs
            this.ForceDependantOutputsToRecalculate();
        };


        this.calcValTextBox = calcValTextBox;
        // The next line sets the delay before the tooltip is shown for the textboxes.
        // The delay (2nd argument) is in milli-seconds
        //ToolTipService.SetInitialShowDelay(this.calcValTextBox, 50);
        // We want to show the tooltip for disabled textboxes (textboxes belonging
        // to output calculator variables are disabled)
        //ToolTipService.SetShowOnDisabled(this.calcValTextBox, true);

        //===============================================================================================//
        //===================================== RADIOBUTTON AND I/O =====================================//
        //===============================================================================================//

        // Make sure that if a radio button is supplied, then a toggle group is also supplied
        assert((ioRadioButton == null && radioButtonToggleGroup == null) || (ioRadioButton != null && radioButtonToggleGroup != null));

        this.radioButtonToggleGroup = radioButtonToggleGroup;

        this.ioRadioButton = ioRadioButton;
        // Setup event handlers. Note that Checked and Unchecked have their own handlers. In this
        // case we point them to the same handler function and inspect state there.
        // Note that the radio button is optional, and could be null!
        if (this.ioRadioButton != null) {

            // Add the provided radio button to the provided toggle group
            this.ioRadioButton.setToggleGroup(this.radioButtonToggleGroup);

            this.ioRadioButton.setUserData(this.Name);

            // Following code provides lambda function which listens to radiobuttons changes and modifies direction accordingly
            this.radioButtonToggleGroup.selectedToggleProperty().addListener((ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) -> {
                    System.out.println("Listener called for radio button toggle group.");
                    if (this.radioButtonToggleGroup.getSelectedToggle() != null) {
                        if(this.radioButtonToggleGroup.getSelectedToggle().getUserData().toString() == this.Name) {
                            System.out.println("Setting calculator variable \"" + this.Name + "\" as an output.");
                            this.setDirection(Directions.Output);
                        } else {
                            System.out.println("Setting calculator variable \"" + this.Name + "\" as an input.");
                            this.setDirection(Directions.Input);
                        }
                    }
                }
            );

        }

        // Initialise empty validators list
        this.validators = new ArrayList<Validator>();

        // Initialise empty validation results list
        this.ValidationResults = new ArrayList<CalcValidationResult>();

        //===============================================================================================//
        //====================================== UNITS AND COMBOBOX =====================================//
        //===============================================================================================//

        this.unitsComboBox = unitsComboBox;

        // Attach event handler to the selection change for the units combo box
        //this.unitsComboBox.SelectionChanged += this.UnitsComboBox_SelectionChanged;
        //this.unitsComboBox.setOnAction(this::UnitsComboBox_SelectionChanged);

        // Initialise empty units list
        this.Units = FXCollections.observableArrayList();

        // Internally save the units
        // Note we can't implictly convert from an array of NumberUnit to a List<NumberUnit>
        NumberUnit defaultUnit = null;

        for(NumberUnit unit : units) {
            this.Units.add(unit);
            if (unit.Preference == NumberPreference.DEFAULT) {
                defaultUnit = unit;
            }
        }

        // Bind the combo-box to the observable collection
        this.unitsComboBox.setItems(this.Units);

        //============ LET THE COMBOBOX KNOW HOW TO RENDER NUMBER UNITS ============//

        this.unitsComboBox.setCellFactory((combobox) -> {

            // Define rendering of the list of values in ComboBox drop down.
            return new ListCell<NumberUnit>() {
                @Override
                protected void updateItem(NumberUnit item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(item.Name);
                    }
                }
            };
        });

        // Define rendering of selected value shown in ComboBox.
        this.unitsComboBox.setConverter(new StringConverter<NumberUnit>() {
            @Override
            public String toString(NumberUnit numberUnit) {
                if (numberUnit == null) {
                    return null;
                } else {
                    return numberUnit.Name;
                }
            }

            @Override
            public NumberUnit fromString(String numberUnitString) {
                return null; // No conversion fromString needed.
            }
        });


        // Set current combobox selection to default unit
        if (defaultUnit != null) {
            this.setSelUnit(defaultUnit);
        }
        else {
            this.setSelUnit(this.Units.get(0));
        }

        // Setup default direction
        this.setDirection(defaultDirection);

        // Internally save reference to the units combo box

        this.NumDigitsToRound = numDigitsToRound;

        // Assign the default raw value
        if (defaultRawValue != null) {
            this.rawVal = defaultRawValue;
            this.dispVal = this.rawVal * this.selUnit.Multiplier;
            this.calcValTextBox.setText(String.valueOf(this.dispVal));
        }
        else {
            // Provided default value was null, so lets make
            // the textbox empty
            this.rawVal = Double.NaN;
            this.dispVal = Double.NaN;
            this.calcValTextBox.setText("");
        }

        // Install event handlers
        /*this.RawValueChanged += (sender, EventArgs) => {
            // Update displayed value
            this.dispVal = this.rawVal * this.selUnit.Multiplier;
            // Update textbox
            this.calcValTextBox.Text = this.dispVal.ToString();
        };*/
        this.addRawValueChangedListener(calcVarBase -> {
            // Update displayed value
            this.dispVal = this.rawVal * this.selUnit.Multiplier;
            // Update textbox
            this.calcValTextBox.setText(String.valueOf(this.dispVal));
        });

        // Save the help text (displayed in the tooltip)
        this.HelpText = helpText;

    }

    //===============================================================================================//
    //============================================ METHODS ==========================================//
    //===============================================================================================//

    /// <summary>
    /// This should only be called for output variables.
    /// </summary>
    public void Calculate() {
        // Make sure this event only fires when this calculator variable is an output!
        assert this.getDirection() == Directions.Output;

        System.out.println("CalcVar.Calculate() called for \"" + this.Name + "\".");

        // Invoke the provided equation function,
        // which should return the raw value for this calculator variable
        this.rawVal = this.Equation.execute();
        //this.dispVal = this.rawVal / this.selUnit.Multiplier;
        //this.calcValTextBox.Text = this.dispVal.ToString();
        this.UpdateDispValFromRawVal();

        // Validation is done in the TextBoxChanged event handler
        this.Validate();

        this.ForceDependantOutputsToRecalculate();
    }


    /// <summary>
    /// Use this to add a specific validator to this calculator variable.
    /// </summary>
    public void AddValidator(Validator validator) {
        // Add provided validator to the internal list
        this.validators.add(validator);
    }

    /// <summary>
    /// Call this to perform validation on this calculator variable. Will run all validators
    /// that have been added through calling AddValidator(), and populate ValidationResults with the
    /// results. Also updates UI based on these results.
    /// </summary>
    public void Validate() {
        System.out.println("Validate() called from \"" + this.Name + "\" with this.RawVal = \"" + String.valueOf(this.rawVal) + "\".");

        // Clear the old validation results
        this.ValidationResults.clear();

        CalcValidationLevel worstValidationResult = CalcValidationLevels.Ok;

        // Validate this value (if validators are provided)
        for(Validator validator : this.validators) {
            // Run the validation function
            CalcValidationLevel validationLevel = validator.ValidationFunction.execute(this.rawVal);

            // Save this validation result
            this.ValidationResults.add(new CalcValidationResult(validationLevel, validator.Message));

            // Logic for keeping track of the worst validation resut
            // (error worse than warning worse than ok)
            if (validationLevel == CalcValidationLevels.Warning && worstValidationResult == CalcValidationLevels.Ok) {
                worstValidationResult = CalcValidationLevels.Warning;
            }
            else if (validationLevel == CalcValidationLevels.Error) {
                worstValidationResult = CalcValidationLevels.Error;
            }
        }

        System.out.println("Validation result was \"" + String.valueOf(worstValidationResult) + "\".");

        // Save this to the internal variable
        this.WorstValidationLevel = worstValidationResult;

        // Finally, force an update of the UI based on these validation results
        this.UpdateUIBasedOnValidationResults();
    }


    public void UnitsComboBox_SelectionChanged(ActionEvent event) {
        System.out.println("UnitsComboBox_Changed() called.");

        // Need to update the selected unit, bypassing the property (otherwise
        // we will create an infinite loop)
        //ComboBox units = (ComboBox)sender;
        this.selUnit = (NumberUnit)this.unitsComboBox.getSelectionModel().getSelectedItem();

        System.out.println("Selected unit is now \"" + this.selUnit + "\".");

        // If the variable is an input, we need to adjust the raw value, if the
        // variable is an output, we need to adjust the displayed value
        if (this.getDirection() == Directions.Input) {
            this.rawVal = this.dispVal * this.selUnit.Multiplier;
            System.out.println("rawVal re-scaled to \"" + String.valueOf(this.rawVal) + "\".");

            // Since the raw value has changed, we also need to re-validate this variable
            this.Validate();

            // We also need to force a recalculation of any dependants (which are also outputs)
            // of this variable
            this.ForceDependantOutputsToRecalculate();

        }
        else if(this.getDirection() == Directions.Output) {
            this.UpdateDispValFromRawVal();
        }
    }

    private void UpdateDispValFromRawVal() {

        System.out.println("UpdateDispValFromRawVal() called for variable \"" + this.Name + "\".");

        // Recalculate dispVal and update textbox
        // We don't need to validate again if the units are changed for an output,
        // as the actual value (raw value) does not change.
        double unroundedDispVal = this.rawVal / this.selUnit.Multiplier;
        //this.dispVal = Rounding.RoundToSignificantDigits(unroundedDispVal, this.NumDigitsToRound);
        this.calcValTextBox.setText(String.valueOf(unroundedDispVal));
    }

    /// <summary>
    /// Updates the tooltip text (adds help info plus validation results).
    /// </summary>
    public void UpdateUIBasedOnValidationResults() {
        // Change the textbox's border colour
        //this.calcValTextBox.BorderBrush = this.validationResult.BorderBrush;
        //this.calcValTextBox.Background = this.validationResult.BackgroundBrush;

        // Build up string from all of the validators which are at the same validation
        // level as the worse one
        String validationMsg = "";
        if (this.WorstValidationLevel != CalcValidationLevels.Ok) {
            for(CalcValidationResult validationResult : this.ValidationResults) {
                // Check to see if this validation result was just as bad as the worse one
                // (i.e. the same level of validation)
                if (validationResult.CalcValidationLevel == this.WorstValidationLevel) {
                    validationMsg += validationResult.Message + " ";
                }
            }
        }
        else {
            // Validation must of been o.k., so in this case we display a stock-standard message
            validationMsg = "Value is o.k.";
        }

        // We need to use a TextBlock so we can do advanced formatting
        //var toolTip = new System.Windows.Controls.TextBlock();

        // Tooltip content is help info plus validation results
        //toolTip.Inlines.Add(this.HelpText + "\r\n\r\n");
        //toolTip.Inlines.Add(new Italic(new Run(validationMsg)));

        // Setting a max width prevents the tooltip from getting rediculuosly large when there is a long help info string.
        // Keeping this quite small also makes the tooltip easier to read.
        //toolTip.MaxWidth = 300;
        // Important to allow wrapping as we are restricting the max. width!
        //toolTip.TextWrapping = System.Windows.TextWrapping.Wrap;

        //this.calcValTextBox.ToolTip = toolTip;
    }

    /// <summary>
    /// Set the selected unit for this calculator variable by passing in a unit name. If the unit can't be found in
    /// the units array, a System.ArgumentException exception will be thrown.
    /// </summary>
    /// <param name="unitName">The name (i.e. whats displayed in the combobox) of the unit you wish to be selected.</param>
    public void SetUnits(String unitName) {

        Core.NumberUnit foundUnit = null;

        for(NumberUnit unit : this.Units) {
            if (unit.Name == unitName) {
                foundUnit = unit;
                break;
            }
        }

        if (foundUnit == null) {
            throw new IllegalArgumentException("Unit name was not found in unit array.");
        }

        // Valid unit in unit array found, so lets set it to the currently
        // selected unit
        this.selUnit = foundUnit;

    }

}
