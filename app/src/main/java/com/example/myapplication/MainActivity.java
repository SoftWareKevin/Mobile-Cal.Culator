package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button settings_button;
    Button record_button;
    Button add_food_button;
    Button removeBtn;
    ListView listView;
    TextView caloriesLeft;
    TextView proteinText;
    TextView carbsText;
    TextView fatText;
    RecordsDatabase db;
    RecordsDao recordsDao;


    ArrayList<String> foodList = new ArrayList<>();
    ArrayList<Integer> calorieList = new ArrayList<>();
    ArrayList<Integer> proteinList = new ArrayList<>();
    ArrayList<Integer> carbsList = new ArrayList<>();
    ArrayList<Integer> fatList = new ArrayList<>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        db = Room.databaseBuilder(
                getApplicationContext(),
                RecordsDatabase.class,
                "appDataBase"
        ).build();
        recordsDao = db.recordsDao();


        settings_button = findViewById(R.id.settings_button);
        record_button = findViewById(R.id.record_button);
        add_food_button = findViewById(R.id.add_food_button);
        removeBtn = findViewById(R.id.removeBtn);
        listView = findViewById(R.id.listView);
        caloriesLeft = findViewById(R.id.caloriesLeft);
        proteinText = findViewById(R.id.proteinText);
        carbsText = findViewById(R.id.carbsText);
        fatText = findViewById(R.id.fatText);

        settings_button.setOnClickListener(this);
        record_button.setOnClickListener(this);
        add_food_button.setOnClickListener(this);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, foodList);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        removeBtn.setOnClickListener(v -> {
            int pos = listView.getCheckedItemPosition();
            if (pos != ListView.INVALID_POSITION) {
                foodList.remove(pos);
                calorieList.remove(pos);
                proteinList.remove(pos);
                carbsList.remove(pos);
                fatList.remove(pos);

                adapter.notifyDataSetChanged();
                listView.clearChoices();
                updateCaloriesLeft();
                displayMacros();
            }
        });

        updateCaloriesLeft();
        displayMacros();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCaloriesLeft();
        displayMacros();
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
        }
        else if (v.getId() == R.id.add_food_button){
            showAddFoodDialog();
        }
    }

    private void updateCaloriesLeft() {
        SharedPreferences prefs = getSharedPreferences("UserSettings", MODE_PRIVATE);

        if (!prefs.contains("calorieGoal")) {
            caloriesLeft.setText("Please set your calorie goal in Settings");
            return;
        }

        int maxCalories = prefs.getInt("calorieGoal", 2000);

        int used = 0;
        for (int c : calorieList) {
            used += c;
        }

        int remaining = maxCalories - used;
        caloriesLeft.setText(String.valueOf(remaining));
    }

    private void displayMacros() {
        SharedPreferences prefs = getSharedPreferences("UserSettings", MODE_PRIVATE);

        if (!prefs.contains("calorieGoal") || !prefs.contains("weight")) {
            proteinText.setText("PROTEIN: --");
            carbsText.setText("CARBS: --");
            fatText.setText("FAT: --");
            return;
        }

        int dailyCalories = prefs.getInt("calorieGoal", 2000);

        double weightLbs;
        try {
            weightLbs = Double.parseDouble(prefs.getString("weight", "0"));
        } catch (NumberFormatException e) {
            weightLbs = 0;
        }

        double targetProtein = weightLbs;
        double targetFat = weightLbs * 0.3;
        double targetCarbs = (dailyCalories - (targetProtein * 4) - (targetFat * 9)) / 4.0;

        if (targetCarbs < 0) {
            targetCarbs = 0;
        }

        int eatenProtein = 0;
        int eatenCarbs = 0;
        int eatenFat = 0;

        for (int p : proteinList) {
            eatenProtein += p;
        }
        for (int c : carbsList) {
            eatenCarbs += c;
        }
        for (int f : fatList) {
            eatenFat += f;
        }

        double proteinLeft = targetProtein - eatenProtein;
        double carbsLeft = targetCarbs - eatenCarbs;
        double fatLeft = targetFat - eatenFat;

        proteinText.setText("PROTEIN: " + String.format("%.1f", proteinLeft) + "g");
        carbsText.setText("CARBS: " + String.format("%.1f", carbsLeft) + "g");
        fatText.setText("FAT: " + String.format("%.1f", fatLeft) + "g");
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

            if (foodInput.getText().toString().isEmpty() ||
                    caloriesInput.getText().toString().isEmpty() ||
                    proteinInput.getText().toString().isEmpty() ||
                    carbsInput.getText().toString().isEmpty() ||
                    fatInput.getText().toString().isEmpty()) {

                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            String foodName = foodInput.getText().toString();
            int calories = Integer.parseInt(caloriesInput.getText().toString());
            int protein = Integer.parseInt(proteinInput.getText().toString());
            int carbs = Integer.parseInt(carbsInput.getText().toString());
            int fat = Integer.parseInt(fatInput.getText().toString());

            //adding data to entity object
            RecordEntry myEntry = new RecordEntry();
            myEntry.foodName = foodName;
            myEntry.calories = calories;
            myEntry.protein = protein;
            myEntry.carbs = carbs;
            myEntry.fat = fat;
            myEntry.date = (int)System.currentTimeMillis();
            
            new Thread(()-> recordsDao.insertEntry(myEntry)).start();

            foodList.add(foodName + " | " + calories + " cal | P:" + protein + " C:" + carbs + " F:" + fat);
            calorieList.add(calories);
            proteinList.add(protein);
            carbsList.add(carbs);
            fatList.add(fat);

            adapter.notifyDataSetChanged();
            updateCaloriesLeft();
            displayMacros();

            Toast.makeText(this, "Food Added!", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
}