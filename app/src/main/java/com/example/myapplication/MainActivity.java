package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button settings_button;
    Button record_button;
    Button add_food_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        settings_button = findViewById(R.id.settings_button);
        record_button = findViewById(R.id.record_button);
        add_food_button = findViewById(R.id.add_food_button);

        settings_button.setOnClickListener(this);
        record_button.setOnClickListener(this);
        add_food_button.setOnClickListener(this);


    }

    @Override
    public void onClick(View v){
        Intent myIntent;
        if (v.getId() == R.id.settings_button){
            myIntent = new Intent(this, Settings.class);
            startActivity(myIntent);
        }
        else if (v.getId() == R.id.record_button){
            myIntent = new Intent(this, Records.class);
            startActivity(myIntent);
            //open the records activity
        }
        else if (v.getId() == R.id.add_food_button){
            Toast toast = Toast.makeText(this, "Add Food clicked", Toast.LENGTH_SHORT);
            toast.show();
            //activate the add food functionality
        }
    }
}