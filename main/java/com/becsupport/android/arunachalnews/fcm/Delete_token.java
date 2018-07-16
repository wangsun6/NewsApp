package com.becsupport.android.arunachalnews.fcm;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import com.google.firebase.iid.FirebaseInstanceId;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by WANGSUN on 23-Jul-17.
 */

public class Delete_token extends AsyncTask<Void,Void,String> {

    Context context;

    String imei_num;


    public Delete_token(Context context, String imei_num){
        this.context=context;
        this.imei_num = imei_num;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected String doInBackground(Void... args) {

        try {
            URL url=new URL("http://becsupport.com/ap_news/delete_row.php");
            HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
            String data= URLEncoder.encode("imei","UTF-8")+"="+URLEncoder.encode(imei_num,"UTF-8");
            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            InputStream inputStream = httpURLConnection.getInputStream();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();

            String temp_receive_data;

            while((temp_receive_data=bufferedReader.readLine())!=null){
                stringBuilder.append(temp_receive_data+"\n");
            }

            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            return stringBuilder.toString().trim();

        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (ProtocolException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        return "Nothing received";
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result) {
        if(result.equals("done")){
            Toast.makeText(context, "50%", Toast.LENGTH_SHORT).show();
            Async_insert token_insert=new Async_insert(context,imei_num, FirebaseInstanceId.getInstance().getToken());
            token_insert.execute();
        }
        else {
            Toast.makeText(context, "Token not inserted-1", Toast.LENGTH_SHORT).show();
        }
    }
}
