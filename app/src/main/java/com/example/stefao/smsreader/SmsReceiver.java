package com.example.stefao.smsreader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * Created by stefao on 3/12/2018.
 */

public class SmsReceiver extends BroadcastReceiver {

    //interface
    private static SmsListener mListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle data = intent.getExtras();

        Object[] pdus = (Object[]) data.get("pdus");

        //for (int i = 0; i < pdus.length; i++) {
        SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[0]);

        String sender = smsMessage.getDisplayOriginatingAddress();
        //Check the sender to filter messages which we require to read
        String messageBody = smsMessage.getMessageBody();
        //Pass the message text to interface
        mListener.messageReceived(messageBody);
        Log.e("Message", messageBody);
        //}

    }

    public static void bindListener(SmsListener listener) {
        mListener = listener;
    }
}
