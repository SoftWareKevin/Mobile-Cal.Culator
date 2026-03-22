package com.example.myapplication;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

public class Records extends AppCompatActivity {

    Button deleteBtn;
    Button viewBtn;
    ListView reportListView;

    ArrayList<String> reportList = new ArrayList<>();
    ArrayAdapter<String> reportAdapter;

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

        deleteBtn=findViewById(R.id.deleteReportBtn);
        viewBtn=findViewById(R.id.viewReportBtn);
        reportListView=findViewById(R.id.reportListView);

        reportAdapter=new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, reportList);
        reportListView.setAdapter(reportAdapter);
        reportListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        deleteBtn.setOnClickListener(v -> {
            int pos=reportListView.getCheckedItemPosition();

            if(pos!=ListView.INVALID_POSITION) {
                reportList.remove(pos);
                reportAdapter.notifyDataSetChanged();
                reportListView.clearChoices();
            }
        });
        
        viewBtn.setOnClickListener(v -> {
            int pos=reportListView.getCheckedItemPosition();
            if (pos != ListView.INVALID_POSITION) {
                String r=reportList.get(pos);
                Toast.makeText(this, r, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
