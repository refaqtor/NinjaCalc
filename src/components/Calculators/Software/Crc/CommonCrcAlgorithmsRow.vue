<template xmlns:v-on="http://www.w3.org/1999/xhtml" xmlns:v-bind="http://www.w3.org/1999/xhtml">

  <tr>
    <td>{{crcEngine.name}}</td>
    <td><calc-var-string :calcVar="calc.getVar(crcEnum)" :width=200></calc-var-string></td>
  </tr>

</template>

<script>

//  var bigInt = require('big-integer')
//  import Calc from '@/misc/CalculatorEngineV2/Calc'
import {CalcVarString} from '@/misc/CalculatorEngineV2/CalcVarString'
import {CrcGeneric} from '@/misc/Crc/CrcGeneric'

// ============================================ //
// =================== vue Object ============= //
// ============================================ //
export default {
  name: 'common-crc-algorithms-row',
  components: {},
  props: {
    calc: {
      type: Object,
      required: true
    },
    crcCatalogue: {
      type: Object,
      required: true
    },
    crcEnum: {
      type: String,
      required: true
    }
  },
  data: function () {
    var value = this.crcCatalogue.get(this.crcEnum)
    var crcEngine = new CrcGeneric({
      name: value.name,
      crcWidthBits: value.crcWidthBits,
      crcPolynomial: value.crcPolynomial,
      startingValue: value.startingValue,
      reflectData: value.reflectData,
      reflectRemainder: value.reflectRemainder,
      finalXorValue: value.finalXorValue,
      checkValue: value.checkValue
    })
    // Create calculator variable which uses the above CRC engine in it's equation
    // function
    this.calc.addVar(new CalcVarString({
      name: this.crcEnum,
      typeEqn: () => {
        return 'output'
      },
      eqn: () => {
        const crcData = this.calc.getVar('convertedCrcData').getVal()
        this.crcEngine.reset()
        for (var i = 0; i < crcData.length; i++) {
          this.crcEngine.update(crcData[i])
        }
        // Prepend '0x' at the front, as getHex() does not do this
        // for us
        return '0x' + this.crcEngine.getHex()
      },
      defaultVal: '',
      validators: [],
      helpText: 'The textual input.'
    }))
    return {
      crcEngine: crcEngine
    }
  },
  mounted () {}
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

  table td {
    text-align: left;
  }

  input {
    color: #989898 !important;
  }

</style>
