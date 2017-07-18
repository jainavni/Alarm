package com.example.avnijain.alarms;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Avni Jain on 7/16/2017.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.NoteViewHolder>{

     private Context mContext;
    private ArrayList<Expense> mNotes;
    // MainActivity mainActivity;
     NotesClickListener mListener;

    public interface NotesClickListener {
        void onItemClick(View view,int position);
    }

    public RecyclerAdapter(Context context, ArrayList<Expense> notes,NotesClickListener listener){
        mContext = context;
        mNotes = notes;
        mListener=listener;
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.list_item,parent,false);
        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, final int position) {
        Expense expense = mNotes.get(position);
        holder.name.setText(expense.title);
//        if(expense.category!= null)
//            expenseViewHolder.category.setText(""+expense.category);

        Date d = new Date(expense.timeStamp);
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1 ;
        int day = c.get(Calendar.DATE);

        String setDate = day + "/" + month + "/"  + year ;
        int hour = c.get(Calendar.HOUR);
        int min = c.get(Calendar.MINUTE);
        String setTime = hour + ":" + min;


            holder.date.setText(setDate);

            holder.time.setText(setTime);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position != RecyclerView.NO_POSITION) {
                    // mainActivity.itemClicked(position);
                    mListener.onItemClick(view, position);

                }
            }
        });
        }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }
//    @Override
//   public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//
//    }



    public  class NoteViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView category;
        TextView date;
        TextView time;
        MainActivity mainActivity;

        public NoteViewHolder(View itemView) {
            super(itemView);
             name =  itemView.findViewById(R.id.expenseNametextView);
             category =  itemView.findViewById(R.id.editCategory);
             date =  itemView.findViewById(R.id.expenseDatetextView);
             time =  itemView.findViewById(R.id.expenseTimetextView);

        }
    }
}

/*
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.NoteViewHolder> {

    private Context mContext;
    private ArrayList<Note> mNotes;
    private NotesClickListener mListener;

    public interface NotesClickListener {
        void onItemClick(View view,int position);
        void onRemoveClicked(int position);
    }


    public RecyclerAdapter(Context context, ArrayList<Note> notes,NotesClickListener listener){
        mContext = context;
        mNotes = notes;
        mListener = listener;
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.layout_item_note,parent,false);
        return new NoteViewHolder(itemView,mListener);
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position) {
        Note note = mNotes.get(position);
        holder.titleTextView.setText(note.getTitle());
        holder.descTextView.setText(note.getDescription());
    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView titleTextView;
        TextView descTextView;
        Button removeButton;
        NotesClickListener mNotesClickListener;

        public NoteViewHolder(View itemView,NotesClickListener listener) {
            super(itemView);
            itemView.setOnClickListener(this);
            mNotesClickListener = listener;
            titleTextView = itemView.findViewById(R.id.title_text);
            descTextView = itemView.findViewById(R.id.desc_text);
            removeButton = itemView.findViewById(R.id.remove_button);
            removeButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int id = view.getId();
            int position = getAdapterPosition();
            if(position != RecyclerView.NO_POSITION){
                if(id == R.id.note_layout){
                    mNotesClickListener.onItemClick(view,position);
                }
                else if(id == R.id.remove_button){
                    mNotesClickListener.onRemoveClicked(position);
                }
            }

        }
    }


}
*/