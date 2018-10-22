
package com.example.ahmadsaad.weatherapp;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

public class DownloadTask extends AsyncTask<String,Void,String>{


    String weatherType = "";

    @Override
    protected String doInBackground(String... urls) {

        String result = "";
        URL url;
        HttpURLConnection urlConnection = null;

        try {
            url = new URL(urls[0]);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if(urlConnection.getResponseCode() == 200){
                InputStream inputStream = urlConnection.getInputStream();
                result = readFromStream(inputStream);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        //onPostExecute(result);
        return result;
    }

    private String readFromStream(InputStream inputStream) throws IOException {

        StringBuilder output = new StringBuilder();
        InputStreamReader inp;
        inp = new InputStreamReader(inputStream, Charset.forName("UTF-8"));

        BufferedReader bf = new BufferedReader(inp);

        String line = bf.readLine();
        while(line != null){
            output.append(line);
            line = bf.readLine();
        }

        return output.toString();
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        try {
            JSONObject root = new JSONObject(result);

            JSONObject main = root.getJSONObject("main");
            Double temp = main.getDouble("temp");
            int temperature = (int) (temp-273.15);

            Double humid = main.getDouble("humidity");

            String name = root.getString("name");

            JSONArray arr = root.getJSONArray("weather");
            JSONObject Type = arr.getJSONObject(0);

            weatherType = Type.getString("main");

            //MainActivity.imageRes(weatherType);

            MainActivity.temp.setText("City: "+ name + "\nTemperature: "+ Integer.toString(temperature)+"C\nHumidity: " + humid + "\nWeather Type: " + weatherType);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
