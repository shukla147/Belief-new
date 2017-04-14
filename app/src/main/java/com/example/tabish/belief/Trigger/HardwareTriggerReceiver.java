package com.example.tabish.belief.Trigger;

/**
 * Created by Tabish on 12-Mar-17.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Vibrator;
import android.util.Log;

import static android.content.Intent.ACTION_SCREEN_OFF;
import static android.content.Intent.ACTION_SCREEN_ON;


public class HardwareTriggerReceiver extends BroadcastReceiver {
    public static final int ALERT_CONFIRMATION_VIBRATION_DURATION = 500;
    private Context context;

    private static final String TAG = HardwareTriggerReceiver.class.getName();
    //    private MultiClickEvent multiClickEvent;
    protected MultiClickEvent multiClickEvent;

    public HardwareTriggerReceiver() {
        resetEvent();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(">>>>>>>", "in onReceive of HWReceiver");
        String action = intent.getAction();
        if (!isCallActive(context) && (action.equals(ACTION_SCREEN_OFF) || action.equals(ACTION_SCREEN_ON))) {
            multiClickEvent.registerClick(System.currentTimeMillis());

            if(multiClickEvent.skipCurrentClick()){
                Log.e("*****", "skipped click");
                multiClickEvent.resetSkipCurrentClickFlag();
            }

            else if(multiClickEvent.canStartVibration()){
                Log.e("*****", "vibration started");
                Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(ALERT_CONFIRMATION_VIBRATION_DURATION);
                //PanicAlert panicAlert = getPanicAlert(context);
                //panicAlert.vibrate();
            }

            else if (multiClickEvent.isActivated()) {
                Log.e("*****", "alerts activated");
                onActivation(context);
                resetEvent();
            }
        }
    }

    protected void onActivation(Context context) {
        Log.e(">>>>>>>", "in onActivation of HWReceiver");
        //activateAlert(getPanicAlert(context));
    }

  /*  void activateAlert(PanicAlert panicAlert) {
//        panicAlert.start();
        panicAlert.activate();
    }
*/
    protected void resetEvent() {
        multiClickEvent = new MultiClickEvent();
    }

    /*protected PanicAlert getPanicAlert(Context context) {
        return new PanicAlert(context);
    }
*/
    private boolean isCallActive(Context context) {
        AudioManager manager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        return manager.getMode() == AudioManager.MODE_IN_CALL;
    }
}

