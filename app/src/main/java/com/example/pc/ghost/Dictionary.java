package com.example.pc.ghost;





import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;



public class Dictionary{
    private String dict;
    String currentWord;
    HashSet wordList = new HashSet();


    Dictionary(Context context, String words) throws IOException {

        dict = words;
        InputStream ips;
        ips = context.getResources().openRawResource(R.raw.english);
        BufferedReader br = new BufferedReader(new InputStreamReader(ips));

        String current = br.readLine();
        while (current != null);{
            wordList.add(current);
            current = br.readLine();
            }


    }

    public void filter(String letter){

        // check if a word starts with letter string, remove from list if it doesn't
        Iterator itr = wordList.iterator();
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
        return wordList.size();
    }

    // return single word in list after filtering
    public String result()
    {
        Iterator iter = wordList.iterator();

        String resultWord = iter.next();
        return resultWord;


    }

    // return original list of words used
    public String reset()
    {
        return dict;
    }



}




