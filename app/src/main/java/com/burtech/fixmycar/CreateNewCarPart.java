 package com.burtech.fixmycar;

//https://localhost:44325/api/company/GetAllCompanies

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;


 public class CreateNewCarPart extends AsyncTask<String, Void, JSONObject>
{
    @Override
    protected JSONObject doInBackground(String... params)
    {
        HttpURLConnection connection = null;
        StringBuffer buffer = new StringBuffer();
        BufferedReader reader = null;
        JSONObject response = null;

        try
        {
            connection = (HttpURLConnection) new URL("http://192.168.0.143:2001/api/company/PostCompany").openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json;");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setChunkedStreamingMode(0);
            connection.connect();

            Log.i("ere", "fuck it");

            String isin = params[0].substring(0, 1) + params[0].substring(1, 2) + "0000009165";
            JSONObject postData = new JSONObject();
            postData.put("Name", params[0]);
            postData.put("Exchange", "XXX");
            postData.put("Ticker", "XXX");
            postData.put("Isin", isin);

            String str =  postData.toString();

            try(OutputStream out = new BufferedOutputStream(connection.getOutputStream()))
            {
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
                writer.write(postData.toString());
                writer.flush();

                int code = connection.getResponseCode();
            }
            catch (Exception e)
            {
                Log.e("", e.getMessage());
            }

            Log.e("hhhhh", "worked");

            connection = (HttpURLConnection) new URL("http://192.168.0.143:2001/api/company/GetAllCompanies").openConnection();
            InputStream input = new BufferedInputStream(connection.getInputStream());

            Log.e("CallApiCompanies",  "CallApiCompanies");

            reader = new BufferedReader(new InputStreamReader(input));
            String line;

            while ((line = reader.readLine()) != null)
            {
                buffer.append(line);
                Log.i("CallApiCompanies",  line);
            }
            response = new JSONObject(buffer.toString());
        }
        catch (Exception e)
        {
            Log.e("", e.getMessage());
        }
        finally
        {
            if (connection != null)
            {
                connection.disconnect();
            }

            if (reader != null)
            {
                try
                {
                    reader.close();
                }
                catch (IOException e) {
                    // do something meaningless
                }
            }
        }

        return response;
    }

    @Override
    protected void onPostExecute(JSONObject response) {
        super.onPostExecute(response);
        // update display
    }
}