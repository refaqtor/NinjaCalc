// var bigInt = require('big-integer')
import {CrcGeneric} from '@/misc/Crc/CrcGeneric'
import {crcCatalogue, crcIds} from '@/misc/Crc/CrcCatalogue'

describe('CrcCatalogue object tests.', function () {
  it('CRC algorithm name can be retrieved correctly.', function () {
    expect(crcCatalogue.get(crcIds.CRC_8_MAXIM).name).toEqual('CRC-8/MAXIM')
  })
  it('CRC polynomial can be retrieved correctly.', function () {
    expect(crcCatalogue.get(crcIds.CRC_8_MAXIM).crcPolynomial.toJSNumber()).toEqual(0x31)
  })
  it('CRC starting value can be retrieved correctly.', function () {
    expect(crcCatalogue.get(crcIds.CRC_8_MAXIM).startingValue.toJSNumber()).toEqual(0x00)
  })
  it('reflect data variable is set correctly.', function () {
    expect(crcCatalogue.get(crcIds.CRC_8_MAXIM).reflectData).toEqual(true)
  })
  it('Reflect remainder variable is set correctly.', function () {
    expect(crcCatalogue.get(crcIds.CRC_8_MAXIM).reflectRemainder).toEqual(true)
  })
  it('Final XOR value is set correctly.', function () {
    expect(crcCatalogue.get(crcIds.CRC_8_MAXIM).finalXorValue.toJSNumber()).toEqual(0x00)
  })
  it('Check value is set correctly.', function () {
    expect(crcCatalogue.get(crcIds.CRC_8_MAXIM).checkValue.toJSNumber()).toEqual(0xA1)
  })
})

describe('CrcCatalogue CRC check value tests.', function () {
  it('Compare CRC algorithm to check value.', function () {
    for (var entry of crcCatalogue.presetCrcAlgorithms) {
      // Extract just the value (don't need the key)
      var value = entry[1]
      // Create a CRC engine with this algorithm info
      var crcGeneric = new CrcGeneric({
        name: value.name,
        crcWidthBits: value.crcWidthBits,
        crcPolynomial: value.crcPolynomial,
        startingValue: value.startingValue,
        reflectData: value.reflectData,
        reflectRemainder: value.reflectRemainder,
        finalXorValue: value.finalXorValue,
        checkValue: value.checkValue
      })
      var dataArray = ['1', '2', '3', '4', '5', '6', '7', '8', '9']
      for (let data of dataArray) {
        crcGeneric.update(data.charCodeAt())
      }
      var result = crcGeneric.getValue().toJSNumber()
      expect(result).toEqual(value.checkValue.toJSNumber())
    }
  })
})
