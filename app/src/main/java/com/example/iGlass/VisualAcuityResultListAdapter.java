package com.example.iGlass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.List;

import com.example.iGlass.data.VisualAcuityResult;

public class VisualAcuityResultListAdapter extends RecyclerView.Adapter<VisualAcuityResultListAdapter.MyViewHolder>{
    private Context context;
    private List<VisualAcuityResult> va_list;
    public VisualAcuityResultListAdapter(Context context) {
        this.context = context;
    }

    public void setVa_List(List<VisualAcuityResult> va_list) {
        this.va_list = va_list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VisualAcuityResultListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_row, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VisualAcuityResultListAdapter.MyViewHolder holder, int position) {
        DecimalFormat df = new DecimalFormat("#.##");
        String scoreLeft = df.format(this.va_list.get(position).ScoreLeft);
        String scoreRight = df.format(this.va_list.get(position).ScoreRight);
        String msg = "Left eye: " + scoreLeft + "   Right eye: " + scoreRight;
        holder.tvFirstName.setText(msg);
        /*if(Integer.parseInt(holder.tvFirstName.getText().toString()) > 0.0 &&
                Integer.parseInt(holder.tvFirstName.getText().toString()) < 5.0) {
            holder.tvFirstName.setTextColor(Color.rgb(94,160,67));
        }
        if(Integer.parseInt(holder.tvFirstName.getText().toString()) > 5.1 &&
                Integer.parseInt(holder.tvFirstName.getText().toString()) < 11.0) {
            holder.tvFirstName.setTextColor(Color.rgb(206,53,52));
        }*/
        holder.tvLastName.setText(this.va_list.get(position).DateCompleted);
    }

    @Override
    public int getItemCount() {
        return this.va_list.size();
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
