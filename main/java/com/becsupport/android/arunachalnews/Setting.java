package com.becsupport.android.arunachalnews;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import static com.becsupport.android.arunachalnews.R.id.checkBox;
import static com.becsupport.android.arunachalnews.R.id.checkBox2;


public class Setting extends AppCompatActivity implements View.OnClickListener,CompoundButton.OnCheckedChangeListener{
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    CheckBox chk_btn_image,chk_bx_java;

    TextView share,report,notes,updateButton,txt_about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        chk_btn_image=(CheckBox)findViewById(checkBox);
        chk_bx_java=(CheckBox)findViewById(checkBox2);
        updateButton=(TextView)findViewById(R.id.update_id);

        share=(TextView)findViewById(R.id.share_id);
        report=(TextView)findViewById(R.id.report_id);
        notes=(TextView)findViewById(R.id.releaseNote_id);
        txt_about=(TextView)findViewById(R.id.about_id);

        share.setOnClickListener(this);
        report.setOnClickListener(this);
        notes.setOnClickListener(this);
        txt_about.setOnClickListener(this);

        sp = getSharedPreferences("MyDataForNews", Context.MODE_PRIVATE);

        if(sp.getInt("chk_image",1)==1)
            chk_btn_image.setChecked(true);
        else
            chk_btn_image.setChecked(false);

        if(sp.getInt("chk_java",1)==1)
            chk_bx_java.setChecked(true);
        else
            chk_bx_java.setChecked(false);


        chk_btn_image.setOnCheckedChangeListener(this);
        chk_bx_java.setOnCheckedChangeListener(this);


        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(updateButton.getText().toString().equals("Check Update")){
                    if(!isNetworkAvailable())
                        Toast.makeText(Setting.this,"Make sure you have internet connection.",Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(Setting.this,"You are using latest version!",Toast.LENGTH_LONG).show();
                }
                else{
                    AlertDialog.Builder updateBuilder = new AlertDialog.Builder(Setting.this);
                    updateBuilder.setTitle("Update")
                            .setMessage("The updated App will be downloaded in ArunachalNews folder.")
                            .setCancelable(false)
                            .setPositiveButton("Download", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });
                    updateBuilder.show();
                }
            }
        });


    }



    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // if no network is available networkInfo will be null
        // otherwise check if we are connected
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.share_id:
                String link = "https://goo.gl/9Pbb5R";

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Download ArunachalTimes: " + link);
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent,"Share App using..."));
                break;

            case R.id.report_id:
                String deviceInfo="Model: "+android.os.Build.MODEL+"\nOS Version: "+ android.os.Build.VERSION.RELEASE
                        +"\n App Version: "+ BuildConfig.VERSION_NAME+"("+BuildConfig.VERSION_CODE+")";
                String[] TO = {"wangzfeeds@gmail.com"};

                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("message/rfc822");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "AP NEWS");
                emailIntent.putExtra(Intent.EXTRA_TEXT,deviceInfo+"\n--------------------------" +
                        "\n\nExplain what exactly happen:\n->");
                try {
                    startActivity(Intent.createChooser(emailIntent, "Send mail..."));

                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.about_id:
                Intent intent_about=new Intent(this,About.class);
                startActivity(intent_about);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(buttonView== chk_btn_image){
            if(chk_btn_image.isChecked()){
                editor = sp.edit();
                editor.putInt("chk_image",1);
                editor.apply();
            }
            else {
                editor = sp.edit();
                editor.putInt("chk_image",0);
                editor.apply();
            }
        }
        else if(buttonView==chk_bx_java){
            if(chk_bx_java.isChecked()){
                editor = sp.edit();
                editor.putInt("chk_java",1);
                editor.apply();
            }
            else {
                editor = sp.edit();
                editor.putInt("chk_java",0);
                editor.apply();
            }
        }
        Toast.makeText(Setting.this,"Reload the News to apply changes.",Toast.LENGTH_LONG).show();
    }
}
