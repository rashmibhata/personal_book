package com.example.abc;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.DiaryViewHolder> {

    private Cursor mCursor;
    private Context mContext;
    private RecyclerViewClickListener mListener;

    public DiaryAdapter(Context context, Cursor cursor, RecyclerViewClickListener listener) {
        mContext = context;
        mCursor = cursor;
        mListener = listener;
    }


    public class DiaryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView dayText;
        public TextView monthText;
        public TextView yearText;
        public TextView titleText;
        public TextView descriptionText;
        public CardView diary_card;
        public Context context;

        public DiaryViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            dayText = itemView.findViewById(R.id.diary_day);
            monthText = itemView.findViewById(R.id.diary_month);
            yearText = itemView.findViewById(R.id.diary_year);
            titleText = itemView.findViewById(R.id.diary_title);
            descriptionText = itemView.findViewById(R.id.diary_description);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v, getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public DiaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater =LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.diary_entry_layout, parent, false);
        return new DiaryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DiaryViewHolder holder, int position) {
        if(!mCursor.moveToPosition(position)){
            return;
        }
        @SuppressLint("Range") String title = mCursor.getString(mCursor.getColumnIndex(DiaryContract.DiaryEntry.COLUMN_TITLE));
        @SuppressLint("Range")String description = mCursor.getString(mCursor.getColumnIndex(DiaryContract.DiaryEntry.COLUMN_DESCRIPTION));
        @SuppressLint("Range") String date = mCursor.getString(mCursor.getColumnIndex(DiaryContract.DiaryEntry.COLUMN_DATE));
        int first_separator_index = date.indexOf("/");
        int second_separator_index = date.lastIndexOf("/");
        String year = date.substring(0, first_separator_index);
        String month_num = date.substring(first_separator_index+1, second_separator_index);
        String day = date.substring(second_separator_index+1);
        String month = month_num_to_string(month_num);

        holder.titleText.setText(title);
        holder.descriptionText.setText(description);
        holder.dayText.setText(day);
        holder.monthText.setText(month);
        holder.yearText.setText(year);

    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor){
        if(mCursor != null){
            mCursor.close();
        }
        mCursor = newCursor;
        if(newCursor != null){
            notifyDataSetChanged();
        }
    }

    public String month_num_to_string(String month_num) {
        switch (month_num) {
            case "01":
                return "JAN";
            case "02":
                return "FEB";
            case "03":
                return "MAR";
            case "04":
                return "APR";
            case "05":
                return "MAY";
            case "06":
                return "JUN";
            case "07":
                return "JUL";
            case "08":
                return "AUG";
            case "09":
                return "SEP";
            case "10":
                return "OCT";
            case "11":
                return "NOV";
            case "12":
                return "DEC";

        }
        return month_num;
    }

    public interface RecyclerViewClickListener{
        void onClick(View v, int position);
    }


}

