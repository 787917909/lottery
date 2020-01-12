package com.example.qiang.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.qiang.R;
import com.example.qiang.activity.EditNoteActivity;
import com.example.qiang.activity.LetteryActivity;
import com.example.qiang.entity.Mainlottery;
import com.example.qiang.gson.Note;
import com.example.qiang.tool.BundleTemp;

import java.util.ArrayList;
import java.util.List;

/**
 * @author andrew
 */
public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> implements Filterable {
    private static final String TAG = "NoteAdapter";
    private List<Mainlottery> notes ;
    private List<Mainlottery> noteListFiltered ;
//    private final ActionCallback callback;

    public interface ContactsAdapterListener {
        void onContactSelected(Note note);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    noteListFiltered = notes;
                } else {
                    List<Mainlottery> filteredList = new ArrayList<>();
                    for (Mainlottery row : notes) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getTitle().contains(charString)) {
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
                noteListFiltered = (ArrayList<Mainlottery>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public NoteAdapter(List<Mainlottery> notes) {
        this.notes = notes;
        this.noteListFiltered = notes;
//        this.callback = callback;
    }

    public String inttostring(int temp){
        if (temp==0){
            return "0";
        }else
            return String.valueOf(temp);
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parent.getContext(), LetteryActivity.class);
                intent.putExtras(BundleTemp.newInstanceBundle(notes.get(viewHolder.getAdapterPosition()).getId(),notes.get(viewHolder.getAdapterPosition()).getMeetingid()));
//                Log.d(TAG, "onClick: "+notes.get(viewHolder.getAdapterPosition()).getId());
                parent.getContext().startActivity(intent);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Mainlottery note = noteListFiltered.get(position);
        holder.title.setText(note.getTitle());
        holder.date.setText(note.getDate().split(" ")[0]);
        holder.renshu.setText("人数");
        holder.totalpeople.setText(inttostring(note.getTotalpeople()));
        holder.firstaward.setText(note.getAward2());
        holder.award.setText(note.getWinjson().equals("")?"等待抽奖":"已抽奖");
    }

    @Override
    public int getItemCount() {
        return noteListFiltered.size();
    }

    public Mainlottery getNote(int position) {
        return notes.get(position);
    }

    public void setNotes(@NonNull List<Mainlottery> notes) {
        this.notes = notes;
        this.noteListFiltered = notes;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView date;
        TextView renshu;
        TextView totalpeople;
        TextView award;
        TextView firstaward;
        ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            date = itemView.findViewById(R.id.date);
            totalpeople = itemView.findViewById(R.id.totalpeople);
            renshu = itemView.findViewById(R.id.renshu);
            firstaward = itemView.findViewById(R.id.firstaward);
            award = itemView.findViewById(R.id.adward);
        }
    }
}
