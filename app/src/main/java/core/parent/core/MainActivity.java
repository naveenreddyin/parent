package core.parent.core;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import com.parent.kid.ConnectToParentActivity;
import com.parent.kid.KidActivity;
import com.parent.parent.AskDetailOfParentActivity;
import com.parent.parent.ParentActivity;
import com.parent.utils.CommonUtilities;


public class MainActivity extends BaseAcitivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        readPreferenceWhichTypeOfApp();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        readParentOrKidPref();
        readPhoneNoPref();
        readPreferenceWhichTypeOfApp();
    }

    /** handles the click button event of parent */
    public void clickParentEvent(View view){
//        if(readPhoneNoPref() == null) {
//            Intent intent = new Intent(this, AskDetailOfParentActivity.class);
//            startActivity(intent);
//        }else{
//            Intent intent = new Intent(this, ParentActivity.class);
//            startActivity(intent);
//        }
        setSharedPreferencesToTrue(1);
        setPreferenceWhichTypeOfApp(1);
        Intent intent = new Intent(this, AskDetailOfParentActivity.class);
        startActivity(intent);
    }

    private void setPreferenceWhichTypeOfApp(int typeOfApp){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        sharedPreferences.edit().putInt(QuickstartPreferences.WHICH_TYPE_OF_APP, typeOfApp).apply();
    }

    private void readPreferenceWhichTypeOfApp(){
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        int appType = sharedPreferences
                .getInt(QuickstartPreferences.WHICH_TYPE_OF_APP, 2);
        Log.i(CommonUtilities.TAG, "app type ===> "+appType);
    }

    /** handles the click button event of kid */
    public void clickKidEvent(View view){
        setSharedPreferencesToTrue(0);
        Intent intent = new Intent(this, ConnectToParentActivity.class);
        startActivity(intent);

    }

    private String readPhoneNoPref(){
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.pref_value_for_phone_no), 0);
        String phoneNo = sharedPreferences.getString(getString(R.string.pref_phone_no), null);
        Log.i("MainActivity", "Phone no: "+phoneNo);
        if(phoneNo != null)
            return phoneNo;
        else
            return null;
    }
    private void setSharedPreferencesToTrue(int which){


        if(which == 0){
            SharedPreferences prefs = getSharedPreferences(getString(R.string.pref_is_kid), 0);

            SharedPreferences.Editor edit = prefs.edit();
            edit.putBoolean(getString(R.string.is_kid), true);
            edit.putBoolean(getString(R.string.is_parent), false);
            edit.apply();
        }
        if(which == 1){
            SharedPreferences prefs = getSharedPreferences(getString(R.string.pref_is_parent), 0);

            SharedPreferences.Editor edit = prefs.edit();

            edit.putBoolean(getString(R.string.is_parent), true);
            edit.putBoolean(getString(R.string.is_kid), false);
            edit.commit();
        }

    }

    private void readParentOrKidPref(){
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        boolean defaultValue = sharedPref.getBoolean(getString(R.string.is_kid), false);
        boolean highScore = sharedPref.getBoolean(getString(R.string.is_parent), false);

        Log.i("MainActivity", "Parent: "+defaultValue);
        Log.i("MainActivity", "Kid: "+highScore);
    }


}
