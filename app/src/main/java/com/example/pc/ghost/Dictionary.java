package com.example.pc.ghost;

import android.content.Context;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class Dictionary{
    String currentWord;
    Set<String> dict;
    Set<String> dictCopy;
    InputStream ips;

    Dictionary(Context ctx, String language) {

        dict = new HashSet<String>();
        if (language == "Dutch")
        {
            ips = ctx.getResources().openRawResource(R.raw.dutch);
        }
        else
        {
            ips = ctx.getResources().openRawResource(R.raw.english);
        }
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(ips));
        Log.d("Joren", "Attempting to construct the dictionary");
        try {
            String line = bufferedReader.readLine();
            while (line != null){
                dict.add(line);
                line = bufferedReader.readLine();
            }
        } catch (IOException e){
            dict.clear();
            Log.d("Joren", "Construction failed");
        }
        Log.d("Joren", "Construction complete");
        dictCopy = new HashSet<String>(dict);
    }




    public void filter(String letter){


        // check if a word starts with letter string, remove from list if it doesn't
        Iterator itr = dict.iterator();
        while (itr.hasNext())
        {
            currentWord = itr.next().toString();
            if(!currentWord.startsWith(letter)){
                itr.remove();
            }
        }
    }

    // count number of words in list after filtering
    public int count()
    {
        return dict.size();
    }

    // return single word in list after filtering
    public Boolean result(String word)
    {
        return dict.contains(word);

    }

    // return original list of words used
    public void reset()
    {
        dict = new HashSet<String>(dictCopy);
    }

}




