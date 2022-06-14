package com.example.abc;

import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class savenote {
   public static String getStick(Context context)
    {
        File file=new File(context.getFilesDir().getPath()+"/savenotee.txt");
        StringBuilder text=new StringBuilder();
        try{
            BufferedReader br=new BufferedReader(new FileReader(file));
            String line;
            while((line=br.readLine())!=null)
            {
                text.append(line);
                text.append("\n");
            }
            br.close();


        }catch (IOException e)
        {

        }
        return text.toString();
    }
    void setStick(String texttobesaved,Context context){
        String text=texttobesaved;
        FileOutputStream fos=null;
        try{
            fos=context.getApplicationContext().openFileOutput("savenotee.txt",Context.MODE_APPEND);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fos);
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
            bufferedWriter.write(text);
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        finally{
            if(fos!=null)
            {
                try{
                    fos.close();
                }
                catch(IOException e){
                    e.printStackTrace();
            }
            }
        }

    }
}
