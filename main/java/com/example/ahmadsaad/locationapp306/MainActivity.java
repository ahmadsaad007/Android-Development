package com.example.ahmadsaad.locationapp306;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {

    TextView tv, tv1;
    Button b;
    Geocoder gb;
    String zip;
    String number;
    List <String[]> HospitalList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InputStream inputStream = getResources().openRawResource(R.raw.hackathon);
        CSVFile csvFile = new CSVFile(inputStream);
        HospitalList = csvFile.read();
        //Toast.makeText(getApplicationContext(),scoreList.get(0)[2],Toast.LENGTH_LONG).show();

        int permissioncheck = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
        int permCheck = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.INTERNET);

        if (permissioncheck == PackageManager.PERMISSION_GRANTED && permCheck == PackageManager.PERMISSION_GRANTED) {

        } else
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}, 1);

        if(ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.CALL_PHONE)){
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.CALL_PHONE},2);
            }
            else{
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.CALL_PHONE},2);
            }
        }else{
            //do nothing
        }
        b = findViewById(R.id.b1);
        tv = findViewById(R.id.tx);
        tv1 = findViewById(R.id.tx2);
        getLocation();
        //number = readCSV();

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int index =  getZipIndex()+1;
                String HospitalName = "Closest Hospital: " + HospitalList.get(index)[0] + HospitalList.get(index)[1] +"\n"+
                        HospitalList.get(index)[2] + HospitalList.get(index)[3] + "-"+ HospitalList.get(index)[4] + "\nPhn.: " + HospitalList.get(index)[5];
                tv1.setText(HospitalName);
                number = getHospitalNumber(index);
                number = "tel:"+number;
                Intent j = new Intent(Intent.ACTION_CALL);
                j.setData(Uri.parse(number));
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    //Toast.makeText(getApplicationContext(),"Inside permission check", Toast.LENGTH_SHORT).show();
                    return;
                }
                startActivity(j);
            }
        });

    }

    private int getZipIndex() {

        List<Integer> zipArray = new ArrayList<>();
        int j =0;
        for(int i = 1;i< HospitalList.size();i++){
            int zipOrig = Integer.parseInt(zip);//zip from location
            int ziptemp = Integer.parseInt(HospitalList.get(i)[4]);
            ziptemp = Math.abs(ziptemp-zipOrig);
            zipArray.add(j,ziptemp);
            j++;
        }
        int minZip = zipArray.get(0);
        int minZipIndex = 0;
        for(int i = 1;i< zipArray.size(); i++){
            if(minZip > zipArray.get(i)){
                minZip = zipArray.get(i);//dont need it but just for reference
                minZipIndex = i;
            }
        }
        return minZipIndex;
    }

    @Override
    public void onLocationChanged(Location location) {

        //tv.setText("Location:\nLatitude: "+ Double.toString(location.getLatitude())+ " Longitude: "+Double.toString(location.getLongitude()) + "\n");

        gb = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> l = gb.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            tv.setText("Current Address: "+l.get(0).getAddressLine(0)+"\n");
            tv.append("-"+l.get(0).getPostalCode());
            zip = l.get(0).getPostalCode();
            //tv.append(l.get(0).getAddressLine(1));
            //tv.append(l.get(0).getAddressLine(2));
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

        Toast.makeText(getApplicationContext(),"Please turn on GPS and/or Internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {

            case 1:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else
                    Toast.makeText(getApplicationContext(), "Permission not Granted", Toast.LENGTH_SHORT).show();
                return;
            case 2:
                if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.CALL_PHONE)==PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this,"No Permission Granted",Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

    public void getLocation() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
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
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);


    }

    public String getHospitalNumber(int index){

        String num = HospitalList.get(index)[5];
        return num;
    }

}

