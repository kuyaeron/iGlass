package com.example.iGlass;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.example.iGlass.data.ColorBlindnessResult;

public class ColorBlindnessResultListAdapter extends RecyclerView.Adapter<com.example.iGlass.ColorBlindnessResultListAdapter.MyViewHolder>{
    private Context context;
    private List<ColorBlindnessResult> cb_list;
    public ColorBlindnessResultListAdapter(Context context) {
        this.context = context;
    }

    public void setCB_List(List<ColorBlindnessResult> cb_list) {
        this.cb_list = cb_list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public com.example.iGlass.ColorBlindnessResultListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_row, parent, false);

        return new com.example.iGlass.ColorBlindnessResultListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull com.example.iGlass.ColorBlindnessResultListAdapter.MyViewHolder holder, int position) {
        holder.tvFirstName.setText(this.cb_list.get(position).Finding);
        if(holder.tvFirstName.getText().toString().equals("PASSED")){
            holder.tvFirstName.setTextColor(Color.rgb(94,160,67));
        }
        if(holder.tvFirstName.getText().toString().equals("FAILED")){
            holder.tvFirstName.setTextColor(Color.rgb(206,53,52));
        }
        holder.tvLastName.setText(this.cb_list.get(position).DateCompleted);
    }

    @Override
    public int getItemCount() {
        return this.cb_list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvFirstName;
        TextView tvLastName;

        public MyViewHolder(View view) {
            super(view);
            tvFirstName = view.findViewById(R.id.tvFirstName);
            tvLastName = view.findViewById(R.id.tvLastName);

        }
    }
}
