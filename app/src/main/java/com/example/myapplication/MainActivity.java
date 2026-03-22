package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button settings_button;
    Button record_button;
    Button add_food_button;

    ArrayList<String> foodRecords = new ArrayList<>();

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
            myIntent.putStringArrayListExtra("records", foodRecords);
            startActivity(myIntent);
            //open the records activity
        }
        else if (v.getId() == R.id.add_food_button){
            showAddFoodDialog();
            //activate the add food functionality
        }
    }
    private void showAddFoodDialog() {


        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_food, null);

        EditText foodInput = dialogView.findViewById(R.id.foodInput);
        EditText caloriesInput = dialogView.findViewById(R.id.caloriesInput);
        EditText proteinInput = dialogView.findViewById(R.id.proteinInput);
        EditText carbsInput = dialogView.findViewById(R.id.carbsInput);
        EditText fatInput = dialogView.findViewById(R.id.fatInput);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Food");
        builder.setView(dialogView);

        builder.setPositiveButton("Submit", (dialog, which) -> {

            String foodName = foodInput.getText().toString();
            int calories = Integer.parseInt(caloriesInput.getText().toString());

            HashMap<String, Integer> macros = new HashMap<>();
            macros.put("protein", Integer.parseInt(proteinInput.getText().toString()));
            macros.put("carbs", Integer.parseInt(carbsInput.getText().toString()));
            macros.put("fat", Integer.parseInt(fatInput.getText().toString()));

            String record = foodName + " | " + calories + " cal | "
                    + "P:" + macros.get("protein")
                    + " C:" + macros.get("carbs")
                    + " F:" + macros.get("fat");

            foodRecords.add(record);

            Toast.makeText(this, "Food Saved!", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Cancel", null);

        builder.show();
    }
}