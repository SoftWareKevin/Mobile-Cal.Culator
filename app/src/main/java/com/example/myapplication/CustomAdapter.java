package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
    private List<RecordEntry> recordList;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView foodName;
        TextView calories;
        TextView carbs;
        TextView fat;
        TextView protein;

        public MyViewHolder(View itemView){
            super(itemView);
            this.foodName = itemView.findViewById(R.id.foodSpot);
            this.calories = itemView.findViewById(R.id.CalorieSpot);
            this.carbs = itemView.findViewById(R.id.CarbSpot);
            this.fat = itemView.findViewById(R.id.FatSpot);
            this.protein = itemView.findViewById(R.id.ProteinSpot);
        }
    }

    public CustomAdapter(List<RecordEntry> recordList){
        this.recordList = recordList;
    }

    public void updateData(List<RecordEntry> newList) {
        this.recordList = newList;
        notifyDataSetChanged();
    }

    @Override
    public CustomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.MyViewHolder holder, int position) {
        RecordEntry currentItem = recordList.get(position);
        holder.foodName.setText(currentItem.foodName);
        holder.calories.setText(String.valueOf(currentItem.calories));
        holder.carbs.setText(String.valueOf(currentItem.carbs));
        holder.fat.setText(String.valueOf(currentItem.fat));
        holder.protein.setText(String.valueOf(currentItem.protein));
    }

    @Override
    public int getItemCount() {
        return recordList == null ? 0 : recordList.size();
    }
}
