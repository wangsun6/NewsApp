package com.becsupport.android.arunachalnews.notification;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.becsupport.android.arunachalnews.ArunachalNews;
import com.becsupport.android.arunachalnews.R;

public class Notice extends AppCompatActivity {
    TextView noti;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        noti=(TextView)findViewById(R.id.notification_id);
        String message=getIntent().getExtras().getString("noti");
        noti.setText(message);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ArunachalNews.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, ArunachalNews.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;
        }
        return true;
    }
}

