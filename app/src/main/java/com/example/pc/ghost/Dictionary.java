package com.example.pc.ghost;





import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;



public class Dictionary{
    private String dict;
    String wordToCheck;

    Dictionary(String words){

        dict = words;
    }

    public String filter(String letter){
        // split dictionary into Linked List
        List<String> wordList = new LinkedList<String>(Arrays.asList(dict.split("[\r\n]+")));
        // check if a word starts with letter string, remove from list if it doesn't
        Iterator<String> itr = wordList.iterator();
        while (itr.hasNext())
        {
            wordToCheck = itr.next();
            if(!wordToCheck.startsWith(letter)){
                itr.remove();
            }
        }
        // convert filtered List back to string
        StringBuilder fBuilder = new StringBuilder();
        for(String s : wordList)
        {
            fBuilder.append(s);
        }
        return fBuilder.toString();

    }

    // count number of words in list after filtering
    public int count(String letter)
    {
        String filterList = filter(letter);
        String[] temp = filterList.split("[\r\n]+");
        return temp.length;
    }

    // return words left in list after filtering
    public String result(String cWord)
    {
        return filter(cWord);
    }

    // return original list of words used
    public String reset()
    {
        return dict;
    }



}




