package com.example.final1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        final DatabaseReference myRef=firebaseDatabase.getReference("Users");
        final EditText name=findViewById(R.id.ename);
        final EditText room=findViewById(R.id.eroom);
        final int sp=0;
        Button button=findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPrefs = getSharedPreferences("sp_name", MODE_PRIVATE);
                SharedPreferences.Editor ed;
                ed = sharedPrefs.edit();
                ed.putString("SPName", name.getText().toString());
                ed.putString("SPRoom",room.getText().toString());
                ed.commit();
                Data data=new Data(name.getText().toString(),"0",room.getText().toString());
                myRef.child(name.getText().toString()).setValue(data);

                //myRef.child(name.getText().toString()).child("name").setValue(name.getText().toString());
               // myRef.child(name.getText().toString()).child("room").setValue(room.getText().toString());
             //   myRef.child(name.getText().toString()).child("speed").setValue(0);

                Intent j= new Intent(getApplicationContext(),MainActivity.class);

                j.putExtra("Name", name.getText().toString());
                j.putExtra("room", room.getText().toString());
                startActivity(j);

            }
        });


    }
}
