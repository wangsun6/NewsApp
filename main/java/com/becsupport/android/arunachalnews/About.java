package com.becsupport.android.arunachalnews;


import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class About extends AppCompatActivity {

    TextView explain,version;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);


        explain=(TextView)findViewById(R.id.exaplain_id);
        version=(TextView)findViewById(R.id.version_id);


        String b=BuildConfig.VERSION_NAME;


        version.setText("AP NEWS v"+b);
        explain.setText("The contain of the site directly fetch from arunachaltimes.in\n" +
                "We only minimize the data and control the performance of the app to load the site faster than other apps.\n");
    }

    public void startMail(View view){
        String[] TO = {"wangzfeeds@gmail.com"};

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "AP News");
        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();

        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
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
}
