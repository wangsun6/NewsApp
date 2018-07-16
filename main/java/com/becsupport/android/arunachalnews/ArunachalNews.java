package com.becsupport.android.arunachalnews;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.Manifest;
import android.telephony.TelephonyManager;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.becsupport.android.arunachalnews.earn.Earn;
import com.becsupport.android.arunachalnews.fcm.Delete_token;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.becsupport.android.arunachalnews.R.id.adView;

public class ArunachalNews extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener,View.OnClickListener {

    private AdView mAdView;
    private FirebaseAnalytics mFirebaseAnalytics;
    AdRequest adRequest;

    private InterstitialAd mInterstitialAd;

    WebView mwebView;
    FloatingActionButton fab_reload,fab_setting,fab_exit;
    FloatingActionMenu fab_menu;

    SharedPreferences sp;
    SharedPreferences.Editor editor;

    int firstLoad,imageLoad,java_script;
    ProgressBar progressBar;
    WebSettings webSettings;
    String webaddress;

    final static int MY_PERMISSIONS_REQUEST_TELEPHONE=75;

    TelephonyManager manager; //to get imei number

    FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arunachal_news);

        mwebView=(WebView)findViewById(R.id.myweb_id);
        progressBar=(ProgressBar)findViewById(R.id.progress_id);

        fab_menu = (FloatingActionMenu) findViewById(R.id.id_fab_menu);
        fab_reload = (FloatingActionButton) findViewById(R.id.id_fab_reload);
        fab_setting = (FloatingActionButton) findViewById(R.id.id_fab_setting);
        fab_exit = (FloatingActionButton) findViewById(R.id.id_fab_exit);

        sp=getSharedPreferences("MyDataForNews", Context.MODE_PRIVATE);

        MobileAds.initialize(this, sp.getString("ad_app_id","ca-app-pub-9084438318738082~3780941225"));

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mAdView = (AdView) findViewById(adView);
        adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        fab_reload.setOnClickListener(this);
        fab_setting.setOnClickListener(this);
        fab_exit.setOnClickListener(this);


        // real ad  ca-app-pub-8298019249336218/8225734724

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(sp.getString("ad_image","ca-app-pub-9084438318738082/1955179070"));
        //mInterstitialAd.loadAd(new AdRequest.Builder().build());

        //ad_repeat();
        initialize();
    }

    public void ad_repeat(){
        Handler handler2 = new Handler();
        handler2.postDelayed(new Runnable() {
            public void run() {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
                else {
                    ad_repeat();
                }
            }
        }, 15000);
    }

    public void initialize(){ //http://arunachaltimes.in/index.php/category/state-news/


        progressBar.setMax(100);
        webaddress=sp.getString("news_link","http://arunachaltimes.in/index.php/category/state-news/");

        firstLoad=sp.getInt("info",0);
        imageLoad=sp.getInt("chk_image",1);
        java_script=sp.getInt("chk_java",1);

        webSettings = mwebView.getSettings();

        if(java_script==1)
            webSettings.setJavaScriptEnabled(true);
        else
            webSettings.setJavaScriptEnabled(false);

        if (Build.VERSION.SDK_INT >= 19) {
            mwebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
        else {
            mwebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        if(imageLoad==1)
            mwebView.getSettings().setLoadsImagesAutomatically(true);
        else
            mwebView.getSettings().setLoadsImagesAutomatically(false);

        mwebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        mwebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);

        String appCachePath = this.getCacheDir().getAbsolutePath();
        webSettings.setAppCachePath(appCachePath);
        mwebView.getSettings().setAppCacheEnabled(true);
        mwebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setUseWideViewPort(true);
        webSettings.setSaveFormData(true);


        mwebView.setWebChromeClient(new WebChromeClient(){
            public void onProgressChanged(WebView view,int progress){
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(progress);
                setTitle("Loading...");

                if(progress==100){
                    progressBar.setVisibility(View.GONE);
                    setTitle(view.getTitle());
                }
                super.onProgressChanged(view,progress);
            }
        });

        mwebView.loadUrl(webaddress);
        mwebView.setWebViewClient(new MyWebviewClient());

        if(firstLoad==0){
            notice();
            firstLoad=1;
            editor=sp.edit();
            editor.putInt("info",1);
            editor.apply();
        }
        else {
            get_server_values();
        }

        /*fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popUp = new PopupMenu(ArunachalNews.this,view);
                popUp.setOnMenuItemClickListener(ArunachalNews.this);
                MenuInflater inflater = popUp.getMenuInflater();
                inflater.inflate(R.menu.papers_name,popUp.getMenu());
                popUp.show();

            }
        });*/

        manager=(TelephonyManager) getSystemService(this.TELEPHONY_SERVICE);

        if(!sp.getString("token_insert", "").equals("yes")){
            if(isOnline()){
                if(isTelephoneAllowed()) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            Delete_token delete_imei2=new Delete_token(ArunachalNews.this,manager.getDeviceId());
                            delete_imei2.execute();
                        }
                    }, 5000);
                }
                else
                    requestTelephonePermission();
            }
        }

    }

    public void notice(){

        final AlertDialog.Builder welcomeBuilder = new AlertDialog.Builder(ArunachalNews.this);
        welcomeBuilder.setTitle("Thanks for Installing our App.")
                .setMessage("First loading may take some extra time.")
                .setCancelable(false)
                .setPositiveButton("Understood", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        get_server_values();

                    }
                });
        welcomeBuilder.show();
    }

    public void get_server_values(){
        DatabaseReference ref_root = database.getReference("server_values");

        ref_root.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                editor=sp.edit();
                for(DataSnapshot infoSnapshot1: dataSnapshot.getChildren()){
                    //info in= infoSnapshot1.getValue(info.class);
                    if(infoSnapshot1.getKey().equals("news_link")){ //"http://192.168.43.134/mysql/aaa/second.html"
                        editor.putString("news_link","http://192.168.43.134/mysql/aaa/second.html");//infoSnapshot1.getValue(String.class));
                        editor.apply();
                    }
                    else if(infoSnapshot1.getKey().equals("app_version")){
                        editor.putInt("app_version",infoSnapshot1.getValue(Integer.class));
                        editor.apply();
                    }
                    else if (infoSnapshot1.getKey().equals("ad_image")) {
                        editor.putString("ad_image", infoSnapshot1.getValue(String.class));
                        editor.apply();
                    }
                    else if (infoSnapshot1.getKey().equals("ad_app_id")) {
                        editor.putString("ad_app_id", infoSnapshot1.getValue(String.class));
                        editor.apply();
                    }
                }
                check_update();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ArunachalNews.this,databaseError.toString(),Toast.LENGTH_LONG).show();
            }
        });
    }

    public void check_update(){
        //Long temp_version= (Long) dataSnapshot.getValue();
        int temp_version = sp.getInt("app_version",1);
        int versionCode = BuildConfig.VERSION_CODE;
        // int temp_ver=(int)(long) temp_version;
        if(versionCode<temp_version){

            editor = sp.edit();
            editor.putString("update_need","yes");
            editor.apply();

            AlertDialog.Builder updateBuilder = new AlertDialog.Builder(this);
            updateBuilder.setTitle("Update Available")
                    .setMessage("Update brings more security and improvements.")
                    .setCancelable(false)
                    .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            final String appPackageName = "com.becsupport.android.arunachalnews"; // getPackageName() from Context or Activity object
                            try {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                            } catch (android.content.ActivityNotFoundException anfe) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
                            }
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }}
                    );
            updateBuilder.show();
        }
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.paper_1:
                //mwebView.loadUrl(webaddress);
                //mwebView.setWebViewClient(new MyWebviewClient());
                initialize();
              return true;

            case R.id.earn_id:
                    Intent intent =new Intent(this,Earn.class);
                    startActivity(intent);
                return true;

            case R.id.setting_id:
                    Intent intent2 = new Intent(this,Setting.class);
                    startActivity(intent2);
                return true;

            case R.id.exit_id:
                finish();
                return true;

            default: return false;
        }
    }

    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    @Override
    public void onClick(View v) {
        if(v==fab_reload){
            fab_menu.close(true);
            initialize();
        }
        else if(v==fab_setting){
            fab_menu.close(true);
            Intent intent2 = new Intent(this,Setting.class);
            startActivity(intent2);
        }
        else if(v==fab_exit){
            fab_menu.close(true);
            finish();
        }
    }


    public class MyWebviewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view,String url){
            view.loadUrl(url);
            progressBar.setVisibility(View.VISIBLE);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

    }

    @Override
    public void onBackPressed() {
        if(mwebView.canGoBack())
            mwebView.goBack();
        else
        {
            AlertDialog.Builder welcomeBuilder = new AlertDialog.Builder(this);
            welcomeBuilder.setTitle("Exit")
                    .setMessage("Close App?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            welcomeBuilder.show();
        }
    }

    private boolean isTelephoneAllowed() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

        if (result == PackageManager.PERMISSION_GRANTED)
            return true;
        return false;
    }

    private void requestTelephonePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_PHONE_STATE)){
        }

        ActivityCompat.requestPermissions(this,new String[]{ Manifest.permission.READ_PHONE_STATE},
                MY_PERMISSIONS_REQUEST_TELEPHONE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_TELEPHONE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            Delete_token delete_imei2=new Delete_token(ArunachalNews.this,manager.getDeviceId());
                            delete_imei2.execute();
                        }
                    }, 5000);
                }
                else
                    finish();
                break;
        }
    }
}
