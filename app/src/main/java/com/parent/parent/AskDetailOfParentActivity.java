package com.parent.parent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.parent.utils.CommonUtilities;
import com.parent.utils.dto.SimpleSuccess;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.prefs.BackingStoreException;

import core.parent.core.BaseAcitivity;
import core.parent.core.QuickstartPreferences;
import core.parent.core.R;
import core.parent.core.RegistrationIntentService;

public class AskDetailOfParentActivity extends BaseAcitivity {
    private TextView phoneNo;
    private ProgressBar mRegistrationProgressBar;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_detail_of_parent);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        int appType = sharedPreferences.getInt(QuickstartPreferences.WHICH_TYPE_OF_APP, 2);
        Log.i(CommonUtilities.TAG, "Which type of app ==> "+appType);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
                if (sentToken) {
                    String gid = intent.getStringExtra("token");
                    Log.i(CommonUtilities.TAG, "Received token...");
                    Log.i(CommonUtilities.TAG, "TOKEN ---> "+gid);
                    Log.i(CommonUtilities.TAG, "Calling service..");
                    new CallToRegisterParentPhoneNo().execute(phoneNo.getText().toString(), gid, 1);

                } else {
                }
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ask_detail_of_parent, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClickAskPhoneNoOfParent(View view){
        // set progressbar to visible
        mRegistrationProgressBar = (ProgressBar) findViewById(R.id.registrationProgrssBar);
        mRegistrationProgressBar.setVisibility(ProgressBar.VISIBLE);

        phoneNo = (TextView) findViewById(R.id.enter_phone_no_of_parent_text);
        setPhoneNoPreferences(phoneNo.getText().toString());


//        if(checkPlayServices()){
//
//        }
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);

    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    private void setPhoneNoPreferences(String phoneNo){
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.pref_value_for_phone_no), 0);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.pref_phone_no), phoneNo);
        editor.commit();
    }

    private class CallToRegisterParentPhoneNo extends AsyncTask {
        private static final String TAG = "CallToRegParentPhNo";
        @Override
        protected Object doInBackground(Object[] params) {
            Log.i(TAG, "PASSED PARAMETER => " + params[0]);
            Log.i(TAG, "GID => "+params[1]);
            Log.i(TAG, "TYPE => "+params[2]);
            try {
                MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
                map.add("gid", params[1].toString());
                map.add("type", params[2].toString());

                final String url = CommonUtilities.SERVER_URL+CommonUtilities.REGISTRATION_URL+params[0];
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                SimpleSuccess simpleSuccess = restTemplate.postForObject(url, map, SimpleSuccess.class);
                Log.i(TAG, "RESPONSE STATUS: "+simpleSuccess);

                return simpleSuccess;

            } catch (RestClientException e) {
                Log.i(TAG, e.getMessage());
            }catch(Exception e){
                Log.i(TAG, e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object object) {
            super.onPostExecute(object);
            mRegistrationProgressBar.setVisibility(ProgressBar.GONE);

            SimpleSuccess simpleSuccess = (SimpleSuccess) object;
            Log.i(TAG, "Simple Success -----> " + object);
            if(simpleSuccess.getResponse().equalsIgnoreCase("0")){
                Log.i(TAG, "REGISTRATION OF PHONE NO FAILED");
                Toast toast = Toast.makeText(getApplicationContext(),"Something went wrong...our monkeys will look into it. Sorry!", Toast.LENGTH_SHORT);
                toast.show();

            }else{

                Intent intent = new Intent(AskDetailOfParentActivity.this, ParentActivity.class);
                startActivity(intent);
            }
        }
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        CommonUtilities.PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(CommonUtilities.TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

}
