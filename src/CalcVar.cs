﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Controls;
// So we can use expression trees
using System.Linq;
using System.Linq.Expressions;
using System.Reflection;

namespace NinjaCalc
{

    public enum Direction_t {
        Input,
        Output
    }


    /// <summary>
    /// Encapsulates a single variable in a NinjaCalc calculator. Stores the variable name, it's equation, it's state (input or output).
    /// </summary>
    public class CalcVar
    {

        private double rawVal;

        /// <summary>
        /// Holds the "raw" (unscaled, unrounded) value for this variable.
        /// </summary>
        public double RawVal
        {
            get
            {
                // Do we want to do something here so we can work out
                // the dependants
                if (RawValueRead != null)
                {
                    RawValueRead(this, EventArgs.Empty);
                }

                return this.rawVal;
            }

            set
            {
                this.rawVal = value;
                // Should we also update the text box here?
            }
        }

        public event EventHandler RawValueRead;

        private String name;

        public String Name
        {
            get
            {
                return name;
            }
            set
            {
                name = value;
            }
        }

        private TextBox calcValTextBox;
        private RadioButton ioRadioButton;
        private List<CalcVar> calcVars;
        private Func<List<CalcVar>, double> equation;

        private Direction_t direction;

        public Direction_t Direction
        {
            get
            {
                return this.direction;
            }
            set
            {
                this.direction = value;
                if(value == Direction_t.Output) {
                    // If this calc variable is being set as an output,
                    // we need to disable the input text box and check the radio button
                    this.calcValTextBox.IsEnabled = false;
                    this.ioRadioButton.IsChecked = true; 
                } else if(value == Direction_t.Input)
                {
                    this.calcValTextBox.IsEnabled = true;
                    this.ioRadioButton.IsChecked = false; 
                }
                
            }
        }

        private List<CalcVar> dependencies;

        /// <summary>
        /// Designed to be assigned to when Calculator.CalculateDependencies() is run. This is not calculated in this class's constructor,
        /// but rather once all calculator variables and their equations have been added to the calculator.
        /// </summary>
        public List<CalcVar> Dependencies
        {
            get
            {
                return this.dependencies;
            }
            set
            {
                this.dependencies = value;
            }
        }

        private List<CalcVar> dependants;

        /// <summary>
        /// Designed to be assigned to when Calculator.CalculateDependencies() is run. This is not calculated in this class's constructor,
        /// but rather once all calculator variables and their equations have been added to the calculator.
        /// </summary>
        public List<CalcVar> Dependants
        {
            get
            {
                return this.dependants;
            }
            set
            {
                this.dependants = value;
            }
        }
        
        /// <summary>
        /// Constructor.
        /// </summary>
        /// <param name="calcValTextBox">The text box that displays this calculator variables value.</param>
        /// <param name="equation">An expression tree of a function which calculates this variables value from the other variables.</param>
        public CalcVar(String name, TextBox calcValTextBox, RadioButton ioRadioButton, List<CalcVar> calcVars, Func<List<CalcVar>, double> equation)
        {

            this.name = name;

            this.calcValTextBox = calcValTextBox;
            // Setup event handler for when textbox text changes
            this.calcValTextBox.TextChanged += this.TextBoxChanged;

            this.ioRadioButton = ioRadioButton;
            this.ioRadioButton.Checked += this.RadioButtonChanged;
            this.ioRadioButton.Unchecked += this.RadioButtonChanged;

            this.calcVars = calcVars;

            this.equation = equation;

            // Initialise the dependency and dependant lists
            this.dependencies = new List<CalcVar>();
            this.dependants = new List<CalcVar>();

        }
        
        

        public void Calculate()
        {
            Console.WriteLine("CalcVar.Calculate() called for \"" + this.Name + "\".");

            // Invoke the provided equation function,
            // which should return the raw value for this calculator variable
            this.rawVal = equation.Invoke(this.calcVars);

            this.calcValTextBox.Text = this.rawVal.ToString();
        }

        /// <summary>
        /// Event handler for when the calculator variables textbox (e.g. it's value) changes. Assigned
        /// to the .TextChanged event of the TextBox in this class's constructor.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        public void TextBoxChanged(object sender, EventArgs e)
        {
            TextBox textBox = (TextBox)sender;
            Console.WriteLine("TextBox \"" + textBox.Name + "\" changed. Text now equals = \"" + textBox.Text + "\".");

            // Save this to the raw value
            this.rawVal = Convert.ToDouble(textBox.Text);

            // We need to re-calculate any this calculator variables dependants, if they are outputs
            for (int i = 0; i < this.dependants.Count; i++)
            {
                if (this.dependants[i].Direction == Direction_t.Output)
                {
                    this.dependants[i].Calculate();
                }
            }


        }

        public void RadioButtonChanged(object sender, EventArgs e)
        {
            RadioButton radioButton = (RadioButton)sender;
            Console.WriteLine("RadioButtonChanged() event called for \"" + radioButton.Name + "\".");

            if(radioButton.IsChecked == true)
            {
                this.Direction = Direction_t.Output;
            }
            else
            {
                this.Direction = Direction_t.Input;
            }
            
        }

    }
}
