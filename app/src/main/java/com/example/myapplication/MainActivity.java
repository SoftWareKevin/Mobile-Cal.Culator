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

    ArrayList<String> foodList = new ArrayList<>();
    ArrayList<Integer> calorieList = new ArrayList<>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        //this code handles the database object instantiation
        RecordsDatabase db = Room.databaseBuilder(getApplicationContext(), RecordsDatabase.class, "Kevin's-uber-cool-db").build();

        settings_button = findViewById(R.id.settings_button);
        record_button = findViewById(R.id.record_button);
        add_food_button = findViewById(R.id.add_food_button);
        removeBtn = findViewById(R.id.removeBtn);
        listView = findViewById(R.id.listView);
        caloriesLeft = findViewById(R.id.caloriesLeft);

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
                adapter.notifyDataSetChanged();
                listView.clearChoices();
                updateCaloriesLeft();
            }
        });

        updateCaloriesLeft();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCaloriesLeft();
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
            Toast toast = Toast.makeText(this, "Add Food clicked", Toast.LENGTH_SHORT);
            toast.show();
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
        caloriesLeft.setText("Calories Left: " + remaining);
    }
}
