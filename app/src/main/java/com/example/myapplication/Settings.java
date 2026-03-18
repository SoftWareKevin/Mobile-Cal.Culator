package com.example.myapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Settings extends AppCompatActivity {

    EditText inputName, inputWeight, inputHeight, inputAge, inputGender, inputGoalWeight, inputActivityLevel;
    Button saveSettingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        inputName = findViewById(R.id.inputName);
        inputWeight = findViewById(R.id.inputWeight);
        inputHeight = findViewById(R.id.inputHeight);
        inputAge = findViewById(R.id.inputAge);
        inputGender = findViewById(R.id.inputGender);
        inputGoalWeight = findViewById(R.id.inputGoalWeight);
        inputActivityLevel = findViewById(R.id.inputActivityLevel);
        saveSettingsButton = findViewById(R.id.saveSettingsButton);

        loadSavedData();

        saveSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });
    }

    private void saveData() {
        String name = inputName.getText().toString().trim();
        String weight = inputWeight.getText().toString().trim();
        String height = inputHeight.getText().toString().trim();
        String age = inputAge.getText().toString().trim();
        String gender = inputGender.getText().toString().trim();
        String goalWeight = inputGoalWeight.getText().toString().trim();
        String activityLevel = inputActivityLevel.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(weight) || TextUtils.isEmpty(height)
                || TextUtils.isEmpty(age) || TextUtils.isEmpty(gender)
                || TextUtils.isEmpty(goalWeight) || TextUtils.isEmpty(activityLevel)) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences prefs = getSharedPreferences("UserSettings", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString("name", name);
        editor.putString("weight", weight);
        editor.putString("height", height);
        editor.putString("age", age);
        editor.putString("gender", gender);
        editor.putString("goalWeight", goalWeight);
        editor.putString("activityLevel", activityLevel);

        editor.apply();

        Toast.makeText(this, "Settings saved", Toast.LENGTH_SHORT).show();
    }

    private void loadSavedData() {
        SharedPreferences prefs = getSharedPreferences("UserSettings", MODE_PRIVATE);

        inputName.setText(prefs.getString("name", ""));
        inputWeight.setText(prefs.getString("weight", ""));
        inputHeight.setText(prefs.getString("height", ""));
        inputAge.setText(prefs.getString("age", ""));
        inputGender.setText(prefs.getString("gender", ""));
        inputGoalWeight.setText(prefs.getString("goalWeight", ""));
        inputActivityLevel.setText(prefs.getString("activityLevel", ""));
    }
}