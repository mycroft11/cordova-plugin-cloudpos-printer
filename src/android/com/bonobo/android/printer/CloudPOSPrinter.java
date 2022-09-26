package com.bonobo.android.printer;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaWebView;

import org.json.JSONArray;
import org.json.JSONException;

import android.graphics.Bitmap;

import android.util.Base64;
import android.content.Context;
import android.util.Log;

import com.cloudpos.DeviceException;
import com.cloudpos.POSTerminal;
import com.cloudpos.jniinterface.RFCardInterface;
import com.cloudpos.printer.Format;
import com.cloudpos.printer.PrinterDevice;

public class CloudPOSPrinter extends CordovaPlugin {
    public static final String TAG = "CloudPOSPrinter";

    private CloudPOSBitmapUtils bitMapUtils;
    private PrinterDevice printer = null;

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);

        Context applicationContext = this.cordova.getActivity().getApplicationContext();

        bitMapUtils = new CloudPOSBitmapUtils(this.cordova.getContext());

        if (printer == null) {
            printer = (PrinterDevice) POSTerminal.getInstance(this.cordova.getContext()).getDevice("cloudpos.device.printer");
        }

        Log.d(TAG, "Initializing CloudPOSPrinter");
    }

    /**
     * Executes the request and returns PluginResult.
     *
     * @param action            The action to execute.
     * @param args              JSONArry of arguments for the plugin.
     * @param callbackContext   The callback id used when calling back into JavaScript.
     * @return                  True if the action was valid, false if not.
     */
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        cordova.getThreadPool().execute(() -> {
            try {
                Log.d(TAG, "Executing " + action);

                if ("startPrintText".equals(action)) {
                    startPrintText(args.getString(0));
                } else if ("startPrintBitmap".equals(action)) {
                    startPrintBitmap(args.getString(0), args.getInt(1), args.getInt(2));
                } else {
                    callbackContext.error("not found");
                }
            } catch (JSONException exception) {
                callbackContext.error(exception.getMessage());
            }
        });
        callbackContext.success();
        return true;
    }

    public void startPrintBitmap(String data, int width, int height) {
        byte[] decoded = Base64.decode(data, Base64.DEFAULT);
        final Bitmap bitMap = bitMapUtils.decodeBitmap(decoded, width, height);

        printer.printBitmap(bitMap);
    }

    public void startPrintText(String text) {
        printer.printText(text);
    }
}