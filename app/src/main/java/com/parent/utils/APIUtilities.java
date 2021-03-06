package com.parent.utils;

import static com.parent.utils.CommonUtilities.SERVER_URL;
import static com.parent.utils.CommonUtilities.TAG;
import static com.parent.utils.CommonUtilities.displayMessage;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;


/**
 * Created by naveenkumarvasudevan on 8/16/15.
 */
public class APIUtilities {
    private static final int MAX_ATTEMPTS = 5;
    private static final int BACKOFF_MILLI_SECONDS = 2000;
    private String method = null;

    private void execute(){

    }

    private APIUtilities setMethod(String method){
        this.method = method;
        return this;
    }

    private String getMethod(){

        return method;
    }

}
