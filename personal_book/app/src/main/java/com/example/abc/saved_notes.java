package com.example.abc;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class saved_notes extends AppCompatActivity {
    TextView tv;
    EditText et;
    Button btn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_notes);
        ActionBar actionBar;
        actionBar = getSupportActionBar();

        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#799820"));

        actionBar.setBackgroundDrawable(colorDrawable);
        tv=findViewById(R.id.tv_saved_note);
        et=findViewById(R.id.findfile);
        btn=findViewById(R.id.btn);
      //  Intent i=getIntent();
//String s_notes=i.getStringExtra("notes");
//tv.setText(s_notes);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String filename=et.getText().toString();
                if(filename.isEmpty())
                {
                    Toast.makeText(saved_notes.this, "Enter File Name", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    filename=filename+".txt";
                   String str= readfromfile(filename);
                   tv.setText(str);

                }
            }
        });



    }

    private String readfromfile(String filename) {

        File path=getApplicationContext().getFilesDir();
        File readfrom=new File(path,filename);
        byte[] content=new byte[(int)readfrom.length()];
        try {
            FileInputStream stream=new FileInputStream(readfrom);
            stream.read(content);
            return new String(content);
        } catch (Exception e) {
         //   e.printStackTrace();
            Toast.makeText(this, "File Not Found", Toast.LENGTH_LONG).show();
            return "";
        }

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater m=getMenuInflater();
        m.inflate(R.menu.menu_note2,menu);


        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==R.id.share_wapp)
        {
            PackageManager pm=getPackageManager();
            try {
               String con=tv.getText().toString();

                Intent waIntent = new Intent(Intent.ACTION_SEND);
                waIntent.setType("text/plain");
                String text =con;


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


        return super.onOptionsItemSelected(item);
    }
}
