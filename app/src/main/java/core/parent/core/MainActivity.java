package core.parent.core;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.parent.parent.ParentActivity;


public class MainActivity extends BaseAcitivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
    }

    /** handles the click button event of parent */
    public void clickParentEvent(View view){
        Intent intent = new Intent(this, ParentActivity.class);
        startActivity(intent);
    }

    /** handles the click button event of kid */
    public void clickKidEvent(View view){
        setSharedPreferencesToTrue(0);
        Intent intent = new Intent(this, ParentActivity.class);
        startActivity(intent);

    }

    private void setSharedPreferencesToTrue(int which){

        SharedPreferences prefs = MainActivity.this.getPreferences(Context.MODE_PRIVATE);

        SharedPreferences.Editor edit = prefs.edit();
        Log.i("MainActivity", "_______________________"+which);
        if(which == 0){
            Log.i("MainActivity", "_______________________ comig inside 0 ");
            edit.putBoolean(getString(R.string.is_parent), true);
            edit.apply();
        }else{
            Log.i("MainActivity", "_______________________ comig inside 1 ");

            edit.putBoolean(getString(R.string.is_kid), true);
            edit.commit();
        }

    }
}
