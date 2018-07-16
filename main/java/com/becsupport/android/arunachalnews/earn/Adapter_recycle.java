package com.becsupport.android.arunachalnews.earn;

/**
 * Created by WANGSUN on 23-Jul-17.
 */

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.becsupport.android.arunachalnews.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class Adapter_recycle extends RecyclerView.Adapter<Adapter_recycle.RecyclerViewHolder> {

    private ArrayList<App_info> arrayList = new ArrayList<>();
    Context context;
    FirebaseDatabase database = FirebaseDatabase.getInstance();;

    public Adapter_recycle(ArrayList<App_info> arrayList, Context context){
        this.arrayList=arrayList;
        this.context=context;


    }

    @Override
    public Adapter_recycle.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.model,parent,false);
        return new Adapter_recycle.RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Adapter_recycle.RecyclerViewHolder holder, int position) {
        final App_info appInfo = arrayList.get(position);

        holder.des.setText(appInfo.getDefine());


        Picasso.with(context).load(appInfo.getUrl()).resize(150, 150).placeholder(R.drawable.icon_folder).into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Uri tempImage=Uri.parse(appInfo.getLink());

                String url = appInfo.getLink();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView des;
        ImageView imageView;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            des = (TextView) itemView.findViewById(R.id.text_id);
            imageView = (ImageView) itemView.findViewById(R.id.imageview_id);

        }
    }

}
