package com.example.pc.ghost;



public class Game
{
    private String cWord;
    private Dictionary dictionary;
    private boolean player1Turn = true;
    private boolean firstLetter = true;
    Game(Dictionary dict)
    {
        dictionary = dict;
    }

    public void guess(String cLetter)
    {
        // Builds the current word out of the letter inputs
        StringBuilder cBuild = new StringBuilder();

        if (firstLetter == true)
        {
            cBuild.append(cLetter);
            firstLetter = false;
        }
        else if (!firstLetter)
        {
            cBuild.append(cWord);
            cBuild.append(cLetter);
        }

        cWord = cBuild.toString();

        dictionary.filter(cWord);

    }
    // returns true if player 1's turn is up, switches turns
    public boolean turn()
    {
        if (player1Turn == true)
        {
            player1Turn = false;
            return true;
        }
        else if (player1Turn == false)
        {
            player1Turn = true;
            return false;
        }
    return false;
    }

    public boolean ended()
    {
        // if the filtered dictionary result equals the current word game ends
        if (dictionary.result(cWord).equals(cWord))
        {
            System.out.println(dictionary.result(cWord));
            return true;
        }
        // if the filter found no words, game ends
        else if (dictionary.result(cWord).isEmpty())
        {
            System.out.println(dictionary.result(cWord));
            return true;
        }
        else
        {
            System.out.println(dictionary.result(cWord));
            return false;
        }
    }
    // checks whose turn it was when the game ended
    public boolean winner()
    {
        if (player1Turn == true)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    // returns current word
    public String word()
    {
        return cWord;
    }
}
