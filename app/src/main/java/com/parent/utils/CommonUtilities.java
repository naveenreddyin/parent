package com.parent.utils;

import android.content.Context;
import android.content.Intent;

import core.parent.core.BaseAcitivity;

/**
 * Created by naveenkumarvasudevan on 8/16/15.
 */
public class CommonUtilities {
    public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    // give your server registration url here
    public static final String SERVER_URL = "http://10.0.2.2:8000/";

    public static final String REGISTRATION_URL = "registration/register-parent-app/";

    public static final String CONNECT_URL = "registration/connect-app/";

    // Google project id
    public static final String SENDER_ID = "204810776192";

    /**
     * Tag used on log messages.
     */
    public static final String TAG = "Parent App";

    static final String DISPLAY_MESSAGE_ACTION =
            "core.parent.core.DISPLAY_MESSAGE";

    static final String EXTRA_MESSAGE = "message";

    /**
     * Notifies UI to display a message.
     * <p>
     * This method is defined in the common helper because it's used both by
     * the UI and the background service.
     *
     * @param context application's context.
     * @param message message to be displayed.
     */
    static void displayMessage(Context context, String message) {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }

}
