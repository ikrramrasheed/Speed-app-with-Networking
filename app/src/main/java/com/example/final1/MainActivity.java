package com.example.final1;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import de.nitri.gauge.Gauge;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LocationListener {

    String Name;
    String room;
    int count=0;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef,arrays;
    ArrayList<String>  names,speed;
    private static CustomAdapter adapter;

    String[] Names=new String[40];
    String[] Speed=new String[40];

    TextView n ;
    ArrayList<Data> data2;
    String myroom;
    TextView r ;
    ImageButton ibutton;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        n=findViewById(R.id.textViewa);
        r=findViewById(R.id.textViewb);
        ibutton=findViewById(R.id.shutoff);
        names=new ArrayList<>();
        data2=new ArrayList<>();
        speed=new ArrayList<>();
        firebaseDatabase=FirebaseDatabase.getInstance();
        arrays=firebaseDatabase.getReference("Users");

        final ListView listView =(ListView) findViewById(R.id.listView);







        final SharedPreferences sharedPrefs = getSharedPreferences("sp_name", MODE_PRIVATE);
        SharedPreferences.Editor ed;
        final String value = sharedPrefs.getString("SPName", "");
        n.setText(value);
        String value2 = sharedPrefs.getString("SPRoom", "");
        r.setText(value2);
        myroom=r.getText().toString();
        if(!sharedPrefs.contains("initialized")){
            ed = sharedPrefs.edit();

            //Indicate that the default shared prefs have been set
            ed.putBoolean("initialized", true);
            ed.putString("SPName","");
            ed.putString("SPRoom","");


            ed.commit();

            Intent i=new Intent(getApplicationContext(),Main2Activity.class);
            startActivity(i);




        }
        //added because new access fine location policies, imported class..
        //ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);


        //check permission


        myRef=firebaseDatabase.getReference(value);
        arrays.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                names.clear();
                speed.clear();
                //String temp=dataSnapshot.child("name").getValue().toString();
                //Toast.makeText(getApplicationContext(),dataSnapshot.getValue().toString(),Toast.LENGTH_SHORT).show();
                for(DataSnapshot postSnapshot:dataSnapshot.getChildren())
                {
                    Data data=postSnapshot.getValue(Data.class);
                    if(myroom.equals(data.getRoom())) {
                        data2.add(data);
                        names.add(data.getName());
                        speed.add(data.getSpeed());
                    }

                }

                adapter= new CustomAdapter(data2,getApplicationContext());

                listView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ibutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //myRef.removeValue();
                //myRef.child("name");
                //myRef.child("speed");
                //myRef.child("room");
                String nameee=n.getText().toString();
                arrays.child(nameee).removeValue();

                Intent s=new Intent(getApplicationContext(),Main2Activity.class);
                startActivity(s);
                finish();
            }
        });






        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1000);
        } else {

            //start the program if permission is granted
            doStuff();


        }
      //  MainActivity.CustomAdapter customAdapter= new MainActivity.CustomAdapter();
     //   listView.setAdapter(customAdapter);









    }
    @Override
    public void onLocationChanged(Location location) {
        final Gauge gauge = findViewById(R.id.gauge);
        TextView txt = (TextView) this.findViewById(R.id.textView);

        if (location == null) {

            txt.setText("-.- km/h");
        } else {
            float nCurrentSpeed;
            nCurrentSpeed = location.getSpeed() * 3.6f;
            txt.setText(String.format("%.2f", nCurrentSpeed) + " km/h");
            myRef.child(n.getText().toString()).setValue(nCurrentSpeed);
            gauge.moveToValue(nCurrentSpeed);

        }

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                doStuff();
            } else {

                finish();
            }

        }

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void doStuff() {
        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (lm != null) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            //commented, this is from the old version
            // this.onLocationChanged(null);
        }
        Toast.makeText(this,"Waiting for GPS connection!", Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

}
