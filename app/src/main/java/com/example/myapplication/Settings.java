package com.example.myapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Settings extends AppCompatActivity {

    EditText inputName, inputWeight, inputHeight, inputAge, inputGender, inputGoalWeight, inputActivityLevel;
    Button saveSettingsButton, calculateCaloriesButton;
    Spinner spinnerCalories;

    ArrayList<String> calorieOptions = new ArrayList<>();
    ArrayList<Integer> calorieValues = new ArrayList<>();

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

        calculateCaloriesButton = findViewById(R.id.calculateCaloriesButton);
        spinnerCalories = findViewById(R.id.spinnerCalories);
        saveSettingsButton = findViewById(R.id.saveSettingsButton);

        loadSavedData();

        calculateCaloriesButton.setOnClickListener(v -> generateCalorieOptions());
        saveSettingsButton.setOnClickListener(v -> saveData());
    }

    private void generateCalorieOptions() {
        String weightStr = inputWeight.getText().toString().trim();
        String heightStr = inputHeight.getText().toString().trim();
        String ageStr = inputAge.getText().toString().trim();
        String gender = inputGender.getText().toString().trim().toLowerCase();
        String activityLevelStr = inputActivityLevel.getText().toString().trim();

        if (TextUtils.isEmpty(weightStr) || TextUtils.isEmpty(heightStr) || TextUtils.isEmpty(ageStr)
                || TextUtils.isEmpty(gender) || TextUtils.isEmpty(activityLevelStr)) {
            Toast.makeText(this, "Enter weight, height, age, gender, and activity level first", Toast.LENGTH_SHORT).show();
            return;
        }

        double weightLbs = Double.parseDouble(weightStr);
        double heightInches = Double.parseDouble(heightStr);
        int age = Integer.parseInt(ageStr);
        int activityLevel = Integer.parseInt(activityLevelStr);

        double maintenance = calculateMaintenanceCalories(weightLbs, heightInches, age, gender, activityLevel);

        int lose2 = Math.max((int)Math.round(maintenance - 1000), 1200);
        int lose1 = Math.max((int)Math.round(maintenance - 500), 1200);
        int maintain = (int)Math.round(maintenance);
        int gain1 = (int)Math.round(maintenance + 500);
        int gain2 = (int)Math.round(maintenance + 1000);

        calorieOptions.clear();
        calorieValues.clear();

        calorieOptions.add("Lose 2 lb/week - " + lose2 + " cal/day");
        calorieValues.add(lose2);

        calorieOptions.add("Lose 1 lb/week - " + lose1 + " cal/day");
        calorieValues.add(lose1);

        calorieOptions.add("Maintain weight - " + maintain + " cal/day");
        calorieValues.add(maintain);

        calorieOptions.add("Gain 1 lb/week - " + gain1 + " cal/day");
        calorieValues.add(gain1);

        calorieOptions.add("Gain 2 lb/week - " + gain2 + " cal/day");
        calorieValues.add(gain2);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                calorieOptions
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCalories.setAdapter(adapter);

        Toast.makeText(this, "Calorie options calculated", Toast.LENGTH_SHORT).show();
    }

    private double calculateMaintenanceCalories(double weightLbs, double heightInches, int age, String gender, int activityLevel) {
        double weightKg = weightLbs * 0.453592;
        double heightCm = heightInches * 2.54;

        double bmr;

        if (gender.equals("male")) {
            bmr = 10 * weightKg + 6.25 * heightCm - 5 * age + 5;
        } else {
            bmr = 10 * weightKg + 6.25 * heightCm - 5 * age - 161;
        }

        double multiplier;
        switch (activityLevel) {
            case 1:
                multiplier = 1.2;
                break;
            case 2:
                multiplier = 1.375;
                break;
            case 3:
                multiplier = 1.55;
                break;
            case 4:
                multiplier = 1.725;
                break;
            default:
                multiplier = 1.2;
                break;
        }

        return bmr * multiplier;
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

        if (spinnerCalories.getAdapter() == null || spinnerCalories.getSelectedItemPosition() < 0) {
            Toast.makeText(this, "Calculate and choose a calorie option first", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedPosition = spinnerCalories.getSelectedItemPosition();
        String selectedLabel = calorieOptions.get(selectedPosition);
        int selectedCalories = calorieValues.get(selectedPosition);

        SharedPreferences prefs = getSharedPreferences("UserSettings", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString("name", name);
        editor.putString("weight", weight);
        editor.putString("height", height);
        editor.putString("age", age);
        editor.putString("gender", gender);
        editor.putString("goalWeight", goalWeight);
        editor.putString("activityLevel", activityLevel);

        editor.putString("calorieGoalLabel", selectedLabel);
        editor.putInt("calorieGoal", selectedCalories);

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