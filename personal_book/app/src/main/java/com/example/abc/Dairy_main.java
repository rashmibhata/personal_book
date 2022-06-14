package com.example.abc;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class Dairy_main extends AppCompatActivity {

    private SQLiteDatabase database_main;
    private DiaryAdapter adapter_main;
    private DiaryAdapter.RecyclerViewClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dairy);

        ActionBar actionBar;
        actionBar = getSupportActionBar();

        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#799820"));

        actionBar.setBackgroundDrawable(colorDrawable);

        DiaryDBHelper dbHelper = new DiaryDBHelper(this);
        database_main = dbHelper.getWritableDatabase();

        setOnClickListener();

        RecyclerView recyclerView = findViewById(R.id.all_diaries_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter_main = new DiaryAdapter(this, getAllItems(), listener);
        recyclerView.setAdapter(adapter_main);



    }

    private void setOnClickListener() {
        listener = new DiaryAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(getApplicationContext(), write_diary.class);
                TextView titleTV = v.findViewById(R.id.diary_title);
                TextView descriptionTV = v.findViewById(R.id.diary_description);
                TextView dayTV = v.findViewById(R.id.diary_day);
                TextView monthTV = v.findViewById(R.id.diary_month);
                TextView yearTV = v.findViewById(R.id.diary_year);

                String title = titleTV.getText().toString();
                String description = descriptionTV.getText().toString();
                String day = dayTV.getText().toString();
                String month = monthTV.getText().toString();
                String year = yearTV.getText().toString();
                String month_num = monthNameToNum(month);

                intent.putExtra("title", title);
                intent.putExtra("description", description);
                intent.putExtra("date", day+"/"+month_num+"/"+year);
                startActivity(intent);
            }
        };
    }

    private String monthNameToNum(String month) {
        switch (month) {
            case "JAN":
                return "01";
            case "FEB":
                return "02";
            case "MAR":
                return "03";
            case "APR":
                return "04";
            case "MAY":
                return "05";
            case "JUN":
                return "06";
            case "JUL":
                return "07";
            case "AUG":
                return "08";
            case "SEP":
                return "09";
            case "OCT":
                return "10";
            case "NOV":
                return "11";
            case "DEC":
                return "12";

        }
        return month;
    }

    @Override
    protected void onResume() {
        super.onResume();
        RecyclerView recyclerView = findViewById(R.id.all_diaries_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter_main = new DiaryAdapter(this, getAllItems(), listener);
        recyclerView.setAdapter(adapter_main);
    }

    private Cursor getAllItems() {
        return database_main.query(
                DiaryContract.DiaryEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                DiaryContract.DiaryEntry.COLUMN_DATE + " DESC"
        );
    }

    public void new_diary(View view){
        Intent intent = new Intent(view.getContext(), write_diary.class);
        startActivity(intent);
    }


}
