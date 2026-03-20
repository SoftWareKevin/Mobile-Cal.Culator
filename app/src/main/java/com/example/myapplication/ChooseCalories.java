package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Spinner;
import android.widget.Toast;

public class ChooseCalories {

    private Context context;
    private Spinner spinnerCalories;
    private SharedPreferences sharedPreferences;

    public ChooseCalories(Context context, Spinner spinnerCalories) {
        this.context = context;
        this.spinnerCalories = spinnerCalories;
        this.sharedPreferences = context.getSharedPreferences("UserSettings", Context.MODE_PRIVATE);
    }

    public void saveCaloriesGoal() {
        String selectedGoal = spinnerCalories.getSelectedItem().toString();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("calorieGoal", selectedGoal);
        editor.apply();

        Toast.makeText(context, "Calorie goal saved: " + selectedGoal, Toast.LENGTH_SHORT).show();
    }

    public void loadSavedCaloriesGoal() {
        String savedGoal = sharedPreferences.getString("calorieGoal", "2000 kcal");

        switch (savedGoal) {
            case "2000 kcal":
                spinnerCalories.setSelection(0);
                break;
            case "2500 kcal":
                spinnerCalories.setSelection(1);
                break;
            case "3000 kcal":
                spinnerCalories.setSelection(2);
                break;
            case "3500 kcal":
                spinnerCalories.setSelection(3);
                break;
            default:
                spinnerCalories.setSelection(0); // Default to 2000 kcal
                break;
        }
    }
}

