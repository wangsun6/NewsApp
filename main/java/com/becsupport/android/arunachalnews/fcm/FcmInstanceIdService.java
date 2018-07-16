package com.becsupport.android.arunachalnews.fcm;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by WANGSUN on 10-Feb-17.
 */

public class FcmInstanceIdService extends FirebaseInstanceIdService{
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        //Log.d(TAG, "Refreshed token: " + refreshedToken);
        //SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.FCM_PREF), Context.MODE_PRIVATE);
        //SharedPreferences.Editor editor=sharedPreferences.edit();
        //editor.putString(getString(R.string.FCM_TOKEN),refreshedToken);
        //editor.commit();

        //async_insert back=new async_insert(getApplicationContext());
        //back.execute(refreshedToken);
    }
}
