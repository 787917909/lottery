package com.example.qiang.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.qiang.R;
import com.example.qiang.activity.LotteryActivity;
import com.example.qiang.entity.Mainlottery;
import com.example.qiang.entity.Theme;
import com.example.qiang.tool.BundleTemp;

import java.util.ArrayList;
import java.util.List;

public class LotteryAdapter extends RecyclerView.Adapter<LotteryAdapter.ViewHolder> implements Filterable {

    private Context mContext;

    private List<Theme> mThemeList;

    private List<Theme> noteListFiltered ;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.lottery_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, LotteryActivity.class);
                intent.putExtras(BundleTemp.ThemeIdBundle(mThemeList.get(holder.getAdapterPosition()).getId(),mThemeList.get(holder.getAdapterPosition()).getTheme()));
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Theme Theme = noteListFiltered.get(position);
        holder.Theme.setText(Theme.getTheme());
        holder.Date.setText(Theme.getDate().split(" ")[0]);
        Glide.with(mContext).load(Theme.getImageid()).into(holder.ThemeImage);
    }

    @Override
    public int getItemCount() {
        return noteListFiltered.size();
    }

    public Theme getNote(int position) {
        return mThemeList.get(position);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    noteListFiltered = mThemeList;
                } else {
                    List<Theme> filteredList = new ArrayList<>();
                    for (Theme row : mThemeList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getTheme().contains(charString)) {
                            filteredList.add(row);
                        }
                    }
                    noteListFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = noteListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                noteListFiltered = (ArrayList<Theme>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
    CardView cardView;
    ImageView ThemeImage;
    TextView Theme;
    TextView Date;

    public ViewHolder(View view){
        super(view);
        cardView = (CardView) view;
        ThemeImage = view.findViewById(R.id.lottery_image);
        Theme = view.findViewById(R.id.lottery_name);
        Date = view.findViewById(R.id.lottery_date);
      }
    }
    public LotteryAdapter(List<Theme> ThemeList){
     mThemeList = ThemeList;
        this.noteListFiltered = ThemeList;
    }

}
