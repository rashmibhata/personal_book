package com.example.abc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;


public class write_diary extends AppCompatActivity {

    private SQLiteDatabase mDatabase;
    private DiaryAdapter mAdapter;
    TextView dateTV;
    EditText titleET;
    EditText descriptionET;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar actionBar;
        actionBar = getSupportActionBar();

        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#799820"));

        actionBar.setBackgroundDrawable(colorDrawable);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_diary);

        DiaryDBHelper dbHelper = new DiaryDBHelper(this);
        mDatabase = dbHelper.getWritableDatabase();

        titleET = findViewById(R.id.textTitle);
        descriptionET = findViewById(R.id.textDescription);
        dateTV = findViewById(R.id.textDate);

        //------------open old diary from intent --------------------
        Intent mainIntent = getIntent();
        String date_receive = mainIntent.getStringExtra("date");
        String title_receive = mainIntent.getStringExtra("title");
        String description_receive = mainIntent.getStringExtra("description");

        titleET.setText(title_receive);
        dateTV.setText(date_receive);
        descriptionET.setText(description_receive);
        //------------------------------------------------------------

        //---------------- setting date text view (date picker custom dialog) --------------
        dateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(write_diary.this, "Date selector", Toast.LENGTH_LONG).show();
                final AlertDialog.Builder alert = new AlertDialog.Builder(write_diary.this);
                View myView = getLayoutInflater().inflate(R.layout.custom_dialog_date_picker, null);

                final DatePicker datePicker = myView.findViewById(R.id.datePicker);
                final Button cancelBtn = myView.findViewById(R.id.cancel_button);
                final Button okayBtn = myView.findViewById(R.id.ok_button);

                alert.setView(myView);

                final AlertDialog alertDialog = alert.create();
                alertDialog.setCanceledOnTouchOutside(false);

                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                okayBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int day = datePicker.getDayOfMonth();
                        int month = datePicker.getMonth() + 1;
                        int year = datePicker.getYear();
                        String month_str = intToDoubleDigitString(month);
                        String day_str = intToDoubleDigitString(day);
                        String selected_date = day_str + "/" + month_str + "/" + year;
                        dateTV.setText(selected_date);
                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();
            }
        });
        //-----------------------------------------------------------------------------


    }


    // ----------------- inflate toolbar with buttons and menu -------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_diary_activity_menu, menu);
        return true;
    }
    //----------------------------------------------------------------------------------


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.save_diary:
                addDiary();
                return true;

            case R.id.delete_diary:
                deleteDiary();
                return true;

            case R.id.share_diary:
                shareDiary();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addDiary() {
        String date_from_TV = dateTV.getText().toString();
        String title = titleET.getText().toString();
        String description = descriptionET.getText().toString();


        if(title.isEmpty() | description.isEmpty()){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("You can not save an empty diary. Please fill in the missing entries !!!");
            alertDialogBuilder.setTitle("Title/Description missing")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
        else if (date_from_TV.isEmpty()){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("You can not save a diary without date. Please select a date for your diary.");
            alertDialogBuilder.setTitle("Date missing");
            alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
        else{
            String day = date_from_TV.substring(0,2);
            String month = date_from_TV.substring(3,5);
            String year = date_from_TV.substring(6);
            String date = year + "/" + month + "/" + day;

            ContentValues contentValues = new ContentValues();
            contentValues.put(DiaryContract.DiaryEntry.COLUMN_DATE, date);
            contentValues.put(DiaryContract.DiaryEntry.COLUMN_TITLE, title);
            contentValues.put(DiaryContract.DiaryEntry.COLUMN_DESCRIPTION, description);

            long row_num = mDatabase.insert(DiaryContract.DiaryEntry.TABLE_NAME, null, contentValues);
            if(row_num == -1){
                mDatabase.update(
                        DiaryContract.DiaryEntry.TABLE_NAME,
                        contentValues,
                        DiaryContract.DiaryEntry.COLUMN_DATE+"=?",
                        new String[]{date});
                Toast.makeText(getApplicationContext(), "Diary has been updated", Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(getApplicationContext(), "New Diary Saved : " + row_num, Toast.LENGTH_LONG).show();
            }
            mDatabase.close();
            Intent saveDiaryIntent = new Intent(getApplicationContext(), Dairy_main.class);
            startActivity(saveDiaryIntent);
        }

    }

    private void deleteDiary() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Do you want to delete this diary permanently ?");
        alertDialogBuilder.setTitle("Delete Diary");
        alertDialogBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String date_from_TV = dateTV.getText().toString();
                if(date_from_TV.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please select the date for which you want to delete the diary.", Toast.LENGTH_LONG).show();
                }
                else{
                    String day = date_from_TV.substring(0,2);
                    String month = date_from_TV.substring(3,5);
                    String year = date_from_TV.substring(6);
                    String date = year + "/" + month + "/" + day;
                    long delete_int = mDatabase.delete(
                            DiaryContract.DiaryEntry.TABLE_NAME,
                            DiaryContract.DiaryEntry.COLUMN_DATE + " = ?",
                            new String[] {date});
                    mDatabase.close();
                    if(delete_int > 0){
                        Toast.makeText(getApplicationContext(), "Diary has been deleted", Toast.LENGTH_LONG).show();
                        Intent deletedDiaryIntent = new Intent(getApplicationContext(), Dairy_main.class);
                        startActivity(deletedDiaryIntent);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Diary with this date does not exist", Toast.LENGTH_LONG).show();
                    }

                }

            }
        });
        alertDialogBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void shareDiary() {
        shareOnWhatsApp();

    }

    private String intToDoubleDigitString(int number){
        String num_str = number + "";
        if(number == 1)
            num_str = "0"+number;
        else if (number == 2)
            num_str = "0"+number;
        else if (number == 3)
            num_str = "0"+number;
        else if (number == 4)
            num_str = "0"+number;
        else if (number == 5)
            num_str = "0"+number;
        else if (number == 6)
            num_str = "0"+number;
        else if (number == 7)
            num_str = "0"+number;
        else if (number == 8)
            num_str = "0"+number;
        else if (number == 9)
            num_str = "0"+number;
        else if (number == 0)
            num_str = "0"+number;
        else
            num_str = ""+number;

        return num_str;
    }

    public void shareOnWhatsApp() {

        PackageManager pm=getPackageManager();
        try {
            String date_from_TV = dateTV.getText().toString();
            String title = titleET.getText().toString();
            String description = descriptionET.getText().toString();
            String day = date_from_TV.substring(0,2);
            String month = date_from_TV.substring(3,5);
            String year = date_from_TV.substring(6);
            String date = day + " " + month_num_to_string(month) + " " + year;

            Intent waIntent = new Intent(Intent.ACTION_SEND);
            waIntent.setType("text/plain");
            String text =
                    "Date : " + date + "\n" +
                            "Title : " + title + "\n" +
                            "Description : " + description + "\n";

            PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
            //Check if package exists or not. If not then code
            //in catch block will be called
            waIntent.setPackage("com.whatsapp");

            waIntent.putExtra(Intent.EXTRA_TEXT, text);
            startActivity(Intent.createChooser(waIntent, "Share with"));

        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(this, "WhatsApp not Installed", Toast.LENGTH_SHORT)
                    .show();
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
}