package core.parent.core;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.iid.InstanceIDListenerService;
import com.parent.utils.CommonUtilities;

public class ParentInstanceIDListenerService extends InstanceIDListenerService {
    public ParentInstanceIDListenerService() {
    }

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        Log.i(CommonUtilities.TAG, "Token refreshed...");
        // Fetch updated Instance ID token and notify our app's server of any changes (if applicable).
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
    }


}
