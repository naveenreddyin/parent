package com.parent.parent;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parent.utils.CommonUtilities;
import com.parent.utils.dto.SimpleSuccess;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import core.parent.core.BaseAcitivity;
import core.parent.core.R;
import core.parent.core.RegistrationIntentService;

public class ParentActivity extends BaseAcitivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_parent, menu);
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

    public void onClickConfigureKidsDevice(View view){
        Intent intent = new Intent(this, ConfigureKidsDeviceActivity.class);
        startActivity(intent);
    }


    public void onAssignTasksClick(View view){

    }

    private class CallToFetchConnectedApps extends AsyncTask {
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

            SimpleSuccess simpleSuccess = (SimpleSuccess) object;
            Log.i(TAG, "Simple Success -----> " + object);
            if(simpleSuccess.getResponse().equalsIgnoreCase("0")){
                Log.i(TAG, "REGISTRATION OF PHONE NO FAILED");
                Toast toast = Toast.makeText(getApplicationContext(),"Something went wrong...our monkeys will look into it. Sorry!", Toast.LENGTH_SHORT);
                toast.show();

            }else{

                Intent intent = new Intent(ParentActivity.this, ParentActivity.class);
                startActivity(intent);
            }
        }
    }


}
