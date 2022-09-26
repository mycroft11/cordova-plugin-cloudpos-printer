/* global cordova, module */

module.exports = {

  startPrintText: function(data, successCallback, errorCallback) {
    cordova.exec(successCallback, errorCallback, 'CloudPOSPrinter', 'startPrintText', [data]);
  },

  startPrintBitmap: function(data, width, height, successCallback, errorCallback) {
    cordova.exec(successCallback, errorCallback, 'CloudPOSPrinter', 'startPrintBitmap', [data, width, height]);
  },
};