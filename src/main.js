// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import Vuex from 'vuex'
import App from './App'

Vue.use(Vuex)

import VueMaterial from 'vue-material'
import 'vue-material/dist/vue-material.css'
Vue.use(VueMaterial)

// Setup v-select component
import vSelect from 'vue-select'
Vue.component(vSelect)

// import BootstrapVue from 'bootstrap-vue'
// Globally register bootstrap-vue components
// Vue.use(BootstrapVue)
// Loading of bootstrap CSS is required for BootstrapVue to work correctly
// import 'bootstrap/dist/css/bootstrap.css'

// KeenUI is used for the tabs
import KeenUI from 'keen-ui'
Vue.use(KeenUI)
import 'keen-ui/dist/keen-ui.css'

// v-tooltip is used for calculator variable tooltips
import VTooltip from 'v-tooltip'
Vue.use(VTooltip)

// =========================================== //
// ==== CALCULATOR COMPONENT REGISTRATION ==== //
// =========================================== //
import CalcValue from 'src/misc/CalculatorEngineV2/CalcValue'
Vue.component('calc-value', CalcValue)
import CalcValueAndUnit from 'src/misc/CalculatorEngineV2/CalcValueAndUnit'
Vue.component('calc-value-and-unit', CalcValueAndUnit)

/* eslint-disable no-unused-vars */
const store = new Vuex.Store({
  state: {
    count: 0,
    showLeftSideBar: false,
    showCalculatorSelectionOverlay: false,
    availableCalcs: [],
    openCalcs: [],
    activeTabId: ''
  },
  mutations: {
    increment (state, payload) {
      state.count += payload.amount
    },
    showLeftSideBar (state, payload) {
      state.showLeftSideBar = payload.trueFalse
    },
    showCalculatorSelectionOverlay (state, payload) {
      state.showCalculatorSelectionOverlay = payload.trueFalse
    },
    openCalculator (state, payload) {
      console.log('openCalculator() called. payload.name = "' + payload.name + '".')
      state.openCalcs.push({
        name: payload.name,
        componentName: payload.componentName,
        // Unique ID is used as a unique tab ID
        uniqueId: state.openCalcs.length
      })
    },
    registerCalc (state, payload) {
      console.log('registerCalc() called with payload =')
      console.log(payload)
      state.availableCalcs.push(payload)
    },
    setNewCalcAsOpenTab (state, payload) {
      console.log('setNewCalcAsOpenTab() called with payload =')
      console.log(payload)
      state.activeTabId = state.openCalcs[state.openCalcs.length - 1].uniqueId
    }
  }
})

// ROOT INSTANCE
/* eslint-disable no-new */
var vm = new Vue({
  el: '#app',
  store,
  template: '<App/>',
  components: { App }
})

