package com.example.myapplication;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
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
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Records extends AppCompatActivity implements View.OnClickListener {

    Button deleteBtn;
    Button viewBtn;
    Button selectDateBtn;
    TextView selectedDateTv;

    List<RecordEntry> records;
    ExecutorService executorService;
    RecordsDatabase db;
    RecordsDao recordsDao;
    CustomAdapter myAdapter;
    RecyclerView myRecycler;

    Calendar calendar = Calendar.getInstance();

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

        db = Room.databaseBuilder(getApplicationContext(), RecordsDatabase.class, "food_database")
                .fallbackToDestructiveMigration()
                .build();
        recordsDao = db.recordsDao();
        executorService = Executors.newSingleThreadExecutor();

        myRecycler = findViewById(R.id.reportListView);
        records = new ArrayList<>();
        myAdapter = new CustomAdapter(records);
        myRecycler.setAdapter(myAdapter);
        myRecycler.setLayoutManager(new LinearLayoutManager(this));

        deleteBtn = findViewById(R.id.deleteReportBtn);
        viewBtn = findViewById(R.id.viewReportBtn);
        selectDateBtn = findViewById(R.id.selectDateBtn);
        selectedDateTv = findViewById(R.id.selectedDateTv);

        deleteBtn.setOnClickListener(this);
        viewBtn.setOnClickListener(this);
        selectDateBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        if (v.getId() == R.id.deleteReportBtn){
            new Thread(() -> {
                List<RecordEntry> allRecords = recordsDao.getAllRecords();
                for (RecordEntry entry : allRecords) {
                    recordsDao.deleteEntry(entry);
                }
                runOnUiThread(() -> {
                    records.clear();
                    myAdapter.updateData(records);
                    Toast.makeText(this, "All Reports Deleted", Toast.LENGTH_SHORT).show();
                });
            }).start();
        }
        else if (v.getId() == R.id.viewReportBtn){

            // Calculate start and end of the selected day in unix seconds
            Calendar startCal = (Calendar) calendar.clone();
            startCal.set(Calendar.HOUR_OF_DAY, 0);
            startCal.set(Calendar.MINUTE, 0);
            startCal.set(Calendar.SECOND, 0);
            startCal.set(Calendar.MILLISECOND, 0);
            long start = startCal.getTimeInMillis();

            Calendar endCal = (Calendar) calendar.clone();
            endCal.set(Calendar.HOUR_OF_DAY, 23);
            endCal.set(Calendar.MINUTE, 59);
            endCal.set(Calendar.SECOND, 59);
            endCal.set(Calendar.MILLISECOND, 999);
            long end = endCal.getTimeInMillis();

            new Thread(()->{
                List<RecordEntry> fetchedRecords = recordsDao.getRecordsByDate(start, end);
                runOnUiThread(() -> {
                    records.clear();
                    records.addAll(fetchedRecords);
                    myAdapter.updateData(records);
                    if (records.isEmpty()) {
                        Toast.makeText(this, "No records found for this date", Toast.LENGTH_SHORT).show();
                    }
                });
            }).start();
        }
        else if (v.getId() == R.id.selectDateBtn) {
            showDatePicker();
        }
    }

    private void showDatePicker() {
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String dateString = String.format(Locale.getDefault(), "%d-%02d-%02d", year, month + 1, dayOfMonth);
            selectedDateTv.setText(dateString);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
}
