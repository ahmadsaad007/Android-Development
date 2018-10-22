package com.example.ahmadsaad.weatherapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class MainActivity extends AppCompatActivity {

    private FusedLocationProviderClient mFusedLocationClient;

    static TextView temp;
    static ImageView im;
    static String type = "";
    //Double lat, lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int imageRes;
        //place = findViewById(R.id.tx);
        temp = findViewById(R.id.textView);
        im = findViewById(R.id.imageView);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                DownloadTask downloadTask = new DownloadTask();

                if(location != null){
                    String lat = Double.toString(location.getLatitude());
                    String lon = Double.toString(location.getLongitude());
                    String url = "http://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&appid=7c74d212e480397a1453e06029f35efc";
                    //temp.setText(url);
                    //Log.v("!!!URL!!!>>>>>>>>>>>", url);
                    downloadTask.execute(url);
                }
                else
                    Toast.makeText(getApplicationContext(),"Turn on Location and Internet", Toast.LENGTH_SHORT).show();

                //type = downloadTask.weatherType;


            }
        });

        //Toast.makeText(getApplicationContext(), type, Toast.LENGTH_SHORT).show();
        if(type.equalsIgnoreCase("Clear")){

            imageRes = getResources().getIdentifier("@drawable/clear", null, this.getPackageName());
            im.setImageResource(imageRes);
        }
        else if(type.equalsIgnoreCase("Rain")){

            imageRes = getResources().getIdentifier("@drawable/overcast", null, this.getPackageName());
            im.setImageResource(imageRes);
        }
        else if(type.equalsIgnoreCase("Clouds")){

            imageRes = getResources().getIdentifier("@drawable/partly", null, this.getPackageName());
            im.setImageResource(imageRes);
        }
        else if(type.equalsIgnoreCase("Snow")){

            imageRes = getResources().getIdentifier("@drawable/snow", null, this.getPackageName());
            im.setImageResource(imageRes);
        }
        else if (type.equalsIgnoreCase("Haze")){
            imageRes = getResources().getIdentifier("@drawable/haze", null, this.getPackageName());
            im.setImageResource(imageRes);
        }
        else if(type.equalsIgnoreCase("Fog")){
            imageRes = getResources().getIdentifier("@drawable/fog", null, this.getPackageName());
            im.setImageResource(imageRes);
        }
        else if(type.equalsIgnoreCase("Thunder")){
            imageRes = getResources().getIdentifier("@drawable/thunder", null, this.getPackageName());
            im.setImageResource(imageRes);
        }
        else{
            imageRes = getResources().getIdentifier("@drawable/imageunavailable", null, this.getPackageName());
            im.setImageResource(imageRes);
        }


    }

    public static void imageRes(String wtype){

        type = wtype;

    }

}
