package com.example.myapplication;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Records extends AppCompatActivity implements View.OnClickListener {

    Button deleteBtn;
    Button viewBtn;

    ArrayList<String> reportList = new ArrayList<>();
    List<RecordEntry> records;
    ExecutorService executorService;
    RecordsDatabase db;
    RecordsDao recordsDao;
    CustomAdapter myAdapter;
    RecyclerView myRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_records);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = Room.databaseBuilder(getApplicationContext(), RecordsDatabase.class, "appDataBase").build();
        recordsDao = db.recordsDao();
        executorService = Executors.newSingleThreadExecutor();

        myRecycler = findViewById(R.id.reportListView);
        myAdapter = new CustomAdapter(records);
        myRecycler.setAdapter(myAdapter);
        myRecycler.setLayoutManager(new LinearLayoutManager(this));

        deleteBtn = findViewById(R.id.deleteReportBtn);
        viewBtn = findViewById(R.id.viewReportBtn);

        deleteBtn.setOnClickListener(this);
        viewBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        if (v.getId() == R.id.deleteReportBtn){
            Toast.makeText(this, "Report Deleted", Toast.LENGTH_SHORT).show();
        }
        else if (v.getId() == R.id.viewReportBtn){

            new Thread(()->{
                List<RecordEntry> fetchedRecords = recordsDao.getAllRecords();
                runOnUiThread(() -> {
                    records = fetchedRecords;
                    myAdapter.updateData(records);
                });
            }).start();

            Toast.makeText(this, "Report Viewed", Toast.LENGTH_SHORT).show();
        }
    }
}
