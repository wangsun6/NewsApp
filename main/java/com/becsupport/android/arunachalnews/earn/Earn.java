package com.becsupport.android.arunachalnews.earn;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;

import com.becsupport.android.arunachalnews.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Earn extends AppCompatActivity {



    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<App_info> arrayList;

    FirebaseDatabase database;
    DatabaseReference myRef_app_info;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earn);

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("earn_apps");
        myRef_app_info = myRef.child("app_info");


        recyclerView =(RecyclerView)findViewById(R.id.recycler_id);

        layoutManager = new LinearLayoutManager(this);

        // Read from the database
        initialize();

    }


    public void initialize(){

        arrayList=new ArrayList<>();

        myRef_app_info.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if((dataSnapshot.getValue(App_info.class))!=null) {

                    for (DataSnapshot infoSnapshot1 : dataSnapshot.getChildren()) {
                        arrayList.add(infoSnapshot1.getValue(App_info.class));
                    }


                    Collections.sort(arrayList, new Comparator<App_info>() {
                        public int compare(App_info v1, App_info v2) {
                            return v1.getPriority()- v2.getPriority();
                        }
                    });

                    adapter = new Adapter_recycle(arrayList,Earn.this);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter);
                    progressDialog.cancel();

                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                progressDialog.cancel();
                Toast.makeText(Earn.this,error.toString(),Toast.LENGTH_LONG).show();
            }
        });
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
