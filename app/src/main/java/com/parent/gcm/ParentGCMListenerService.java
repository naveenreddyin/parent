package com.parent.gcm;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

public class ParentGCMListenerService extends GcmListenerService {
    public ParentGCMListenerService() {
    }

    @Override
    public void onMessageReceived(String from, Bundle data) {
        super.onMessageReceived(from, data);
        String message = data.getString("message");
        Log.i("GCM", "From: " + from);
        Log.i("GCM", "Message: " + message);
        // Handle received message here.

    }


}
