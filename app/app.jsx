//!
//! @file               app.jsx
//! @author             Geoffrey Hunter <gbmhunter@gmail.com> (www.mbedded.ninja)
//! @created            2015-11-02
//! @last-modified      2015-11-17
//! @brief              Contains the "redux" actions for the NinjaCalc app.
//! @details
//!     See README.rst in repo root dir for more info.

//import React, { Component } from 'react';

import React from 'react';
import ReactDOM from 'react-dom';
// Redux utility functions
import { compose, createStore, applyMiddleware } from 'redux';
import { Provider, connect } from 'react-redux';
import thunk from 'redux-thunk';
var Select = require('react-select');
import Dropdown from 'react-dropdown';
import { Input, Tooltip, OverlayTrigger, Popover, Tabs, Tab } from 'react-bootstrap';
var PureRenderMixin = require('react-addons-pure-render-mixin');
var _ = require('lodash');
var Latex = require('react-latex');

//import Tabs from './utility/react-draggable-tab/components/Tabs';
//import Tab from './utility/react-draggable-tab/components/Tab';


var ReactRadioGroup = require('react-radio-group');

import AbsoluteGrid from './utility/react-absolute-grid/AbsoluteGrid.js';
//var ReactGridLayout = require('react-grid-layout');

const finalCreateStore = compose(
  // Enables your middleware:
  applyMiddleware(thunk) // any Redux middleware, e.g. redux-thunk
  
  // Provides support for DevTools:
  //! @warning  This will cause the entire state/action history to be re-run everytime a
  //!     new action is dispatched, which could cause performance issues!
  //devTools()
)(createStore);

//=========== REDUCER ===========//
import defaultReducer from './reducers/calc-reducer.js';
import * as calcActions from './actions/calc-actions.js';
//console.log(defaultReducer);

// Create store. Note that there is only one of these for the entire app.
const store = finalCreateStore(defaultReducer);


//============== LOAD CALCULATORS ============//

import * as lt3745Calc from './calculators/chip-specific/lt3745/lt3745.js';
import ohmsLawCalc from './calculators/basic/ohms-law/ohms-law.js';
import * as resistorDividerCalc from './calculators/basic/resistor-divider/resistor-divider.js';

// Calculators are loaded into Redux state in the onMount function of the react App


//var ReactGridLayout = require('react-grid-layout');

 var sampleItems = [
  {key: 1, name: 'Test', sort: 0, filtered: 0},
  {key: 2, name: 'Test 1', sort: 1, filtered: 0},
];

var CalcInput = React.createClass({

	mixins: [PureRenderMixin],

	render: function() {

		//console.log('CalcInput.render() called. this.props = ');
		//console.log(this.props);

		var placeholder;
		if(!this.props.disabled) {
			placeholder = 'Enter value';
		} else {
			placeholder = '';
		}

		return (			
			<OverlayTrigger placement="right" overlay={<Tooltip>{this.props.overlay}</Tooltip>}>
				<Input
			        type="text"
			        value={this.props.value}
			        disabled={this.props.disabled}
			        placeholder={placeholder}
			        hasFeedback
			        bsStyle={this.props.bsStyle}
			        ref="input"
			        groupClassName="group-class"
			        labelClassName="label-class"
			        onChange={this.props.onChange} />
			</OverlayTrigger>
    	);
	},

});

var CalcUnits = React.createClass({

	mixins: [PureRenderMixin],

	render: function() {

		//console.log('this.props = ');
		//console.log(this.props);

		return (			
			<Select
					name="form-field-name"
					value={this.props.value}
					options={this.props.options}
					onChange={this.props.onChange}
					clearValueText=""	
					clearable={false}			
					multi={false}
					searchable={false}	
					placeholder="Select"
				/>
		);
	},

});


//! @brief    A single row in the calculator table.
// Have had serious issues with using the "class" ES6 syntax!!!
// e.g. "this" no longer exists as I know it!
//class CalcRow extends React.Component {
var CalcRow = React.createClass({

	mixins: [PureRenderMixin],

	onValueChange: function(event) {
		console.log('onValueChange() called with event = ');
		console.log(event);

		// Let's call a thunk to set the variable value inside redux state
		this.props.dispatch(calcActions.setVarVal(this.props.calcId, this.props.varData.get('id'), event.target.value));
	},

	onCalcWhatChange: function(event) {
		console.log('CalcRow.onCalcWhatChange() called.');
		console.log('this =');
		console.log(this);
		//this.props.onCalcWhatChange(event, this.props.name);

		this.props.dispatch(calcActions.setOutputVar(this.props.calcId, this.props.varData.get('id')));
	},

	onUnitsChange: function(event) {
		console.log('onUnitsChange() called with event =');
		console.log(event);

		this.props.dispatch(calcActions.setVarUnits(this.props.calcId, this.props.varData.get('id'), event));
	},

	render: function() {
		//console.log('CalcRow.render() called, with this.props.varData =');
		//console.log(this.props.varData);

		var isInputDisabled;
		if(this.props.varData.get('direction') == 'input') {
			isInputDisabled = false;
		} else {
			// direction must == 'output'
			isInputDisabled = true;
		}

		// Build up the required classes for styling
		var bsStyle = '';
		// worstValidationResult should either be 'ok', 'warning' or 'error'
		bsStyle += this.props.varData.get('worstValidationResult');


		// Work out if radio button is needed
		var radioButton;
		if(this.props.varData.get('showRadio')) {
			radioButton = <input type="radio" checked={this.props.varData.get('direction') == 'output'} onChange={this.onCalcWhatChange} />
		}

		return (
			<tr>
				<td>{this.props.varData.get('name')}</td>
				{/* Now display the dispVal for each calculator variable */}
				<td>
					<CalcInput
						value={this.props.varData.get('dispVal')}
						overlay={this.props.varData.get('tooltipText')}
						disabled={isInputDisabled}
						bsStyle={bsStyle}
						onChange={this.onValueChange} />
					
				</td>
				<td className="unitsCol">
					
					<CalcUnits
						name="form-field-name"
						value={this.props.varData.get('selUnitValue')}
						options={this.props.varData.get('units').toJS()}
						onChange={this.onUnitsChange}
					/>
				</td>
				<td>{radioButton}</td>				
			</tr>
		);
	}
});

var Calculator = React.createClass({

	mixins: [PureRenderMixin],

	render: function() {

		var that = this;

		//console.log('this.props.data = ');
		//console.log(this.props.data);

		return (
			<div>
				<h1>{this.props.data.name}</h1>	
				<table className="calculatorTable">
					<tbody>
						{/* This generates the rows of the table which contain the calculator variables */							
							this.props.data.get('vars').map((el) => {
								//console.log('el.id = ' + el.get('id'));
								return (<CalcRow key={el.get('id')} calcId={this.props.data.get('id')} varData={el} dispatch={that.props.dispatch} />)
							})
						}
					</tbody>
				</table>			
			</div>
		);
	}
})



//class App extends React.Component {

var App = React.createClass({

	//mixins: [PureRenderMixin],

	componentDidMount: function() {


		this.props.dispatch(calcActions.addCalc(ohmsLawCalc));
		this.props.dispatch(calcActions.addCalc(lt3745Calc.data));		
		this.props.dispatch(calcActions.addCalc(resistorDividerCalc.data));
	},

	handleSelect(key) {
		//alert('selected ' + key);
		this.props.dispatch(calcActions.setActiveTab(key));		
	},

	//! @brief		Called when the text within the "search" input changes.
	//! @details	Dispatches a setSearchTerm event, which then updates the input and filters the calculator grid results.
	onSearchInputChange(event) {
	    console.log('onSearchInputChange() called with event.target.value = ');
	    console.log(event.target.value);

	    this.props.dispatch(calcActions.setSearchTerm(event.target.value));
 	},

 	//! @brief		This function determines what calculator element to render inside the tab.
 	//! @details	We need this because the UI structure of each calculator may be different.
 	renderCalc: function(calculator, key) {
 		//if(el.get('id') == 'resistorDivider') {
 			//console.log('renderCalc() called with id = resistorDivider.');
 			//console.log(el.get('view'));

 			// Create the view for the specific calculator.
 			// Note that since this isn't created in JSX (i.e. <CalcView>...</CalcView>),
 			// we have to use React.createFactory

		return React.createElement(calculator.get('view'), { key: key, data: calculator, dispatch: this.props.dispatch });



 			/*var CalcView = React.createFactory(el.get('view'));
 			
 			return CalcView({ 
 				// Pass in the entire calculator model as data
 				data: el,
 				// Pass in dispatch so actions can be called!
 				dispatch: this.props.dispatch,
 			});*/
 		//}


 		//return <Calculator key={el.get('id')} data={el} dispatch={this.props.dispatch} />
 	},

	render: function() {

		var that = this;

		// We have to inject the dispatch function as a prop to all of the grid elements so we
		// can do something when the 'Load' button is clicked. The function pointer can't be added
		// in the reducer because the reducer has no knowledge of it.
		var items = this.props.state.get('gridElements').toJS().map((gridElement) => {
			gridElement.dispatch = this.props.dispatch;
			return gridElement;
		});

		var that = this;

		return (
			<div className="app">	
				{/* Tabs are the main view element on the UI */}
				<Tabs activeKey={this.props.state.get('activeTabKey')} onSelect={this.handleSelect}>
					{/* First tab is static and non-removable */}
					<Tab eventKey={0} title="Calculators">
						{/* This is used to narrow down on the desired calculator */}
						<Input
					        type="text"
					        value={this.props.state.get('searchTerm')}
					        placeholder="Enter text"
					        label="Search for calculator"
					        hasFeedback
					        ref="input"
					        groupClassName="group-class"
					        labelClassName="label-class"
					        onChange={this.onSearchInputChange} />					        
						<br />
						<div>
							{/* Item width and height determine the size of the card. Note that if the card is too big it can make the
							height larger, but not the width */}
							<AbsoluteGrid
								items={items}
								itemWidth={240}
								itemHeight={360}
								responsive={true}
								zoom={1}
								animation="transform 300ms ease"/>							
						</div>
					</Tab>
					{/* Let's create a visual tab for every calculator in array */

						this.props.state.get('calculators').filter((calculator) => {
							//console.log('calculator.get(\'visible\') = ' + calculator.get('visible'));
							return calculator.get('visible');
						}).map(function(calculator, index) {
							return (
								<Tab key={index+1} eventKey={index+1} title={calculator.get('name')}>
									{/* This next line of code inserts the entire calculator into the tab element. */}
									{that.renderCalc(calculator)}										
								</Tab>
							);
						})
					}
				</Tabs>											
			</div>
		);
	}
});



//! @brief    Selects what props to inject into app.
//! @details  Currently injecting everything.
function mapStateToProps(state) {
  return {
    /*serialPort: state.serialPort,
    loggingState: state.logging,
    stats: state.stats,
    util: state.util,*/
    // Map everything at the moment, might change in future
    state: state,    
  };
}


// Inject dispatch and state into app
App = connect(mapStateToProps)(App);

// Wrapping the app in Provider allows us to use Redux
console.log(document);
//console.log('document.getElementById(\'content\') = ');
console.log(document.getElementById('content'));

var appRender = ReactDOM.render(
  <div id='redux-wrapper-div' style={{height: '100%'}}>
    <Provider store={store}>
        <App />
    </Provider>
  </div>,
  document.getElementById('content')
);




