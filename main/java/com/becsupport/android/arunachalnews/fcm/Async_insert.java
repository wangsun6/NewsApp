package com.becsupport.android.arunachalnews.fcm;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

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

public class Async_insert extends AsyncTask<String,Void,String> {

    String url_1;
    Context context;

    String imei_num,str_token;

    SharedPreferences sp;
    SharedPreferences.Editor editor;


    public Async_insert(Context context,String imei_num,String str_token){
        this.context=context;
        this.imei_num = imei_num;
        this.str_token=str_token;
    }

    @Override
    protected void onPreExecute() {
        url_1="http://becsupport.com/ap_news/add_row.php";
    }

    @Override
    protected String doInBackground(String... args) {


        try {
            URL url=new URL(url_1);
            HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
            String data=
                            URLEncoder.encode("imei","UTF-8")+"="+URLEncoder.encode(imei_num,"UTF-8")+"&"+
                            URLEncoder.encode("token","UTF-8")+"="+URLEncoder.encode(str_token,"UTF-8");
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

        return "Nothing received-2";
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result) {
        if(result.equals("done")){  //retrieve done string from wedsite...if(string == done)  token_insert=yes

            sp = context.getSharedPreferences("MyDataForNews", Context.MODE_PRIVATE);
            editor = sp.edit();
            editor.putString("token_insert","yes");
            editor.commit();
            Toast.makeText(context, "100%", Toast.LENGTH_SHORT).show();         //11111111111111111111111111111111
        }
        else {
            Toast.makeText(context, "Token not inserted-2", Toast.LENGTH_SHORT).show();
        }
    }
}