package com.yoctopuce.yespresso;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.yoctopuce.YoctoAPI.YAPI;
import com.yoctopuce.YoctoAPI.YAPI_Exception;
import com.yoctopuce.YoctoAPI.YModule;
import com.yoctopuce.YoctoAPI.YServo;

public class YoctopuceInterface implements YAPI.LogCallback {
    public static final int NB_TUBE = 5;
    private static final int UP_POSITION = -1000;
    private static final int DOWN_POSITION = 1000;
    private static YoctopuceInterface __instance;
    private final Context _ctx;
    private int _refcount;
    private String _lastError = null;
    private String _serial = "SERVORC1-2E093";

    private YoctopuceInterface(Context applicationContext) {
        _ctx = applicationContext;
        _refcount = 0;
    }

    public static YoctopuceInterface Get(Context ctx) {
        if (__instance == null) {
            __instance = new YoctopuceInterface(ctx.getApplicationContext());
        }
        return __instance;
    }


    public synchronized void startUsage() {
        if (_refcount == 0) {
            try {
                YAPI.EnableUSBHost(_ctx);
            } catch (YAPI_Exception e) {
                e.printStackTrace();
            }
            YAPI.RegisterLogFunction(this);
            new InitYoctopuceAPI().execute("usb");
        }
        _refcount++;
    }


    private void initServo(String serial) throws YAPI_Exception {
        for (int i = 0; i < NB_TUBE; i++) {
            YServo servo = YServo.FindServo(String.format("%s.servo%d", serial, i + 1));
            servo.set_range(150);
            servo.set_enabled(YServo.ENABLED_TRUE);
            servo.move(DOWN_POSITION, 300);
            YAPI.Sleep(500);
            servo.set_positionAtPowerOn(DOWN_POSITION);
            servo.set_enabledAtPowerOn(YServo.ENABLEDATPOWERON_FALSE);
            servo.set_enabled(YServo.ENABLED_FALSE);
        }
        YModule module = YModule.FindModule(serial);
        module.saveToFlash();
    }


    private class InitYoctopuceAPI extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            for (String hub : params) {
                try {
                    YAPI.RegisterHub(hub);
                    final YServo yServo = YServo.FirstServo();
                    if (yServo != null) {
                        _serial = yServo.get_module().get_serialNumber();
                        initServo(_serial);
                    }
                } catch (YAPI_Exception e) {
                    return e.getLocalizedMessage();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String error) {
            if (error != null) {
                Log.e("YAPI", error);
                _lastError = error;
            }
        }
    }


    private class DistributeCapsule extends AsyncTask<Integer, Void, String> {

        @Override
        protected String doInBackground(Integer... params) {
            int index = params[0];
            YServo servo = YServo.FindServo(String.format("%s.servo%d", _serial, index + 1));
            try {
                servo.set_enabled(YServo.ENABLED_TRUE);
                servo.move(UP_POSITION, 300);
                Thread.sleep(1000);
                servo.move(DOWN_POSITION, 300);
                Thread.sleep(500);
                servo.set_enabled(YServo.ENABLED_FALSE);
            } catch (YAPI_Exception e) {
                e.printStackTrace();
                return e.getLocalizedMessage();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String error) {
            if (error != null) {
                Log.e("YAPI", error);
                _lastError = error;
            }
        }
    }

    public synchronized void distributeCapsule(int tubeIndex) {
        new DistributeCapsule().execute(tubeIndex);
    }


    public synchronized void stopUsage() {
        _refcount--;
        if (_refcount == 0) {
            YAPI.FreeAPI();
        }
    }

    @Override
    public void yLog(String line) {
        Log.d("YAPI", line.trim());
    }


}
