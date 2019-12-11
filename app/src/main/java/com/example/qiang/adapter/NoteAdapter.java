package com.example.qiang.adapter;

import android.content.Intent;
import android.util.Log;
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
import com.example.qiang.gson.Note;

import java.util.ArrayList;
import java.util.List;

/**
 * @author andrew
 */
public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> implements Filterable {
    private static final String TAG = "NoteAdapter";
    private List<Note> notes ;
    private List<Note> noteListFiltered ;
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
                    List<Note> filteredList = new ArrayList<>();
                    for (Note row : notes) {

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
                noteListFiltered = (ArrayList<Note>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public NoteAdapter(List<Note> notes) {
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
                intent.putExtras(EditNoteActivity.newInstanceBundle(notes.get(viewHolder.getAdapterPosition()).getId()));
//                Log.d(TAG, "onClick: "+notes.get(viewHolder.getAdapterPosition()).getId());
                parent.getContext().startActivity(intent);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Note note = noteListFiltered.get(position);
        holder.title.setText(note.getTitle());
        holder.date.setText(note.getDate());
        holder.renshu.setText("人数");
        holder.totalpeople.setText(inttostring(note.getTotalpeople()));
        holder.firstaward.setText("一等奖");
        holder.award.setText(note.getAward1());
    }

    @Override
    public int getItemCount() {
        return noteListFiltered.size();
    }

    public Note getNote(int position) {
        return notes.get(position);
    }

    public void setNotes(@NonNull List<Note> notes) {
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
