package com.example.abc;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;


public class StickyNote extends AppCompatActivity {
EditText noteEdt,filename;
Button increasesizebtn,decresesizebtn,boldbtn,underlinebtn,italicbtn;
TextView sizeTV;
float currentSize;
savenote note=new savenote();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar;
        actionBar = getSupportActionBar();

        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#799820"));

        actionBar.setBackgroundDrawable(colorDrawable);

        
        setContentView(R.layout.notepadxml);
        noteEdt=findViewById(R.id.idedt);
        increasesizebtn=findViewById(R.id.idincreasesize);
        decresesizebtn=findViewById(R.id.idreducesize);
        boldbtn=findViewById(R.id.idbtnbold);
        underlinebtn=findViewById(R.id.idbtnunderln);
        italicbtn=findViewById(R.id.idbtnitalic);
        sizeTV=findViewById(R.id.idtvsize);
        currentSize=noteEdt.getTextSize();
        sizeTV.setText(""+currentSize);
        filename=findViewById(R.id.notehead);
        increasesizebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sizeTV.setText(""+currentSize);
                currentSize++;
                noteEdt.setTextSize(currentSize);
            }
        });
        decresesizebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sizeTV.setText(""+currentSize);
                currentSize--;
                noteEdt.setTextSize(currentSize);
            }
        });
    boldbtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            italicbtn.setTextColor(getResources().getColor(R.color.white));
            italicbtn.setBackgroundColor(getResources().getColor(R.color.olive));
            if(noteEdt.getTypeface().isBold())
            {
                noteEdt.setTypeface(Typeface.DEFAULT);
                boldbtn.setTextColor(getResources().getColor(R.color.white));
                boldbtn.setBackgroundColor(getResources().getColor(R.color.olive));
            }
            else{

                boldbtn.setTextColor(getResources().getColor(R.color.olive));
                boldbtn.setBackgroundColor(getResources().getColor(R.color.white));
                noteEdt.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            }
        }
    });
    underlinebtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
           if(noteEdt.getPaintFlags()==0)
           {
               underlinebtn.setTextColor(getResources().getColor(R.color.white));
               underlinebtn.setBackgroundColor(getResources().getColor(R.color.olive));
               noteEdt.setPaintFlags(noteEdt.getPaintFlags() & (-Paint.UNDERLINE_TEXT_FLAG));
           }
           else{
               underlinebtn.setTextColor(getResources().getColor(R.color.olive));
               underlinebtn.setBackgroundColor(getResources().getColor(R.color.white));
               noteEdt.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
           }
        }
    });

    italicbtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            boldbtn.setTextColor(getResources().getColor(R.color.white));
            boldbtn.setBackgroundColor(getResources().getColor(R.color.olive));
            if(noteEdt.getTypeface().isItalic())
            {
                noteEdt.setTypeface(Typeface.DEFAULT);
                italicbtn.setTextColor(getResources().getColor(R.color.white));
                italicbtn.setBackgroundColor(getResources().getColor(R.color.olive));
            }
            else{

                italicbtn.setTextColor(getResources().getColor(R.color.olive));
                italicbtn.setBackgroundColor(getResources().getColor(R.color.white));
                noteEdt.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
            }
        }
    });







    }
    public void saveButton(View view)
    {
       /* note.setStick(noteEdt.getText().toString(),this);
 updateWidget();
        Toast.makeText(this, "Note has been updated", Toast.LENGTH_SHORT).show();/*

        */
        String fname=filename.getText().toString();
        if(fname.isEmpty())
        {
            Toast.makeText(this, "Please Enter The Name of file", Toast.LENGTH_SHORT).show();
        }
        else
        {
            fname=fname+".txt";
            String data=noteEdt.getText().toString();
            writeTofile(data,fname);


        }
    }

    private void writeTofile(String data, String fname) {
        File path=getApplicationContext().getFilesDir();
        try {
            FileOutputStream writer=new FileOutputStream(new File(path,fname));
            writer.write(data.getBytes());
            writer.close();
            Toast.makeText(this, "Note has been updated", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateWidget(){
        AppWidgetManager appWidgetManager=AppWidgetManager.getInstance(this);
        RemoteViews remoteViews=new RemoteViews(this.getPackageName(),R.layout.widget_layout);
        ComponentName thisWidget=new ComponentName(this,Notepad.class);
        remoteViews.setTextViewText(R.id.idtvwidget,noteEdt.getText().toString());
        appWidgetManager.updateAppWidget(thisWidget,remoteViews);


    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater m=getMenuInflater();
        m.inflate(R.menu.menu_note,menu);


        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

       if(item.getItemId()==R.id.saved_note)
       {
         Intent i2=new Intent(this,saved_notes.class);
          // String snote=note.getStick(this);
          // System.out.println(snote);
          // Toast.makeText(getApplicationContext(),snote,Toast.LENGTH_LONG);
        //   i2.putExtra("notes","snote" );
           startActivity(i2);
       }


        return super.onOptionsItemSelected(item);
    }





}


