package com.parent.kid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parent.parent.ParentActivity;
import com.parent.utils.CommonUtilities;
import com.parent.utils.dto.SimpleSuccess;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import core.parent.core.BaseAcitivity;
import core.parent.core.QuickstartPreferences;
import core.parent.core.R;
import core.parent.core.RegistrationIntentService;

public class ConnectToParentActivity extends BaseAcitivity {
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private EditText childNo, parentNo;
    private ProgressBar connectAppProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_to_parent);

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
                    new CallToConnect().execute(childNo.getText().toString(), gid, 0, parentNo.getText().toString());

                } else {
                }
            }
        };
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_connect_to_parent, menu);
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

    /** handles connect button click **/
    public void onConnectClick(View view){
        connectAppProgressBar = (ProgressBar) findViewById(R.id.connect_app_progressbar);
        connectAppProgressBar.setVisibility(ProgressBar.VISIBLE);

        // collect data
        childNo = (EditText) findViewById(R.id.phone_no_of_child);
        parentNo = (EditText) findViewById(R.id.phone_no_to_connect_text);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ConnectToParentActivity.this);
        sharedPreferences.edit().putString(getString(R.string.pref_phone_no), childNo.getText().toString()).apply();

        // call for gid
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);

    }

    private class CallToConnect extends AsyncTask {
        private static final String TAG = "CallToRegParentPhNo";
        @Override
        protected Object doInBackground(Object[] params) {
            Log.i(TAG, "PASSED PARAMETER => " + params[0]);
            Log.i(TAG, "GID => "+params[1]);
            Log.i(TAG, "TYPE => "+params[2]);
            Log.i(TAG, "parent no => "+params[3]);
            try {
                MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
                map.add("gid", params[1].toString());
                map.add("type", params[2].toString());

                final String url = CommonUtilities.SERVER_URL+CommonUtilities.CONNECT_URL+params[0]+"/"+params[3];
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
            connectAppProgressBar.setVisibility(ProgressBar.GONE);
            SimpleSuccess simpleSuccess = (SimpleSuccess) object;
            Log.i(TAG, "Simple Success -----> " + simpleSuccess.getMessage());
            if(simpleSuccess.getResponse().equalsIgnoreCase("0")){
                Log.i(TAG, "REGISTRATION OF PHONE NO FAILED");
                Toast toast = Toast.makeText(getApplicationContext(),"Something went wrong...our monkeys will look into it. Sorry!", Toast.LENGTH_SHORT);
                toast.show();

            }else{

                Intent intent = new Intent(ConnectToParentActivity.this, KidActivity.class);
                startActivity(intent);
            }
        }
    }
}
