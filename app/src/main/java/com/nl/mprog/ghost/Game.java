package com.nl.mprog.ghost;

// Joren de Bruin
// Minor Programmeren App Studio
// Studentnr. 10631267


public class Game
{
    private String cWord, Reason;
    private Dictionary dictionary;
    private boolean player1Turn = true;
    private boolean firstLetter = true;
    Game(Dictionary dict)
    {
        dictionary = dict;

    }

    public String guess(String cLetter)
    {
        // Builds the current word out of the letter inputs
        StringBuilder cBuild = new StringBuilder();

        if (firstLetter)
        {
            cBuild.append(cLetter);
            firstLetter = false;
        }
        else
        {
            cBuild.append(cWord);
            cBuild.append(cLetter);
        }

        cWord = cBuild.toString();


        return cWord;

    }
    // returns true if player 1's turn is up, switches turns
    public boolean turn()
    {
        if (player1Turn)
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
        // if the filtered dictionary contains the current word game ends
        if (dictionary.result(cWord))
        {
            Reason = cWord + " is an existing word";
            return true;
        }
        // if the filter found no words, game ends
        else if (dictionary.count() == 0)
        {
            Reason = cWord + " does not lead to an existing word";
            return true;

        }
        else
        {
            return false;
        }
    }
    // checks whose turn it was when the game ended
    public boolean winner()
    {
        if (player1Turn)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    public void customEntry(String word)
    {
        cWord = word;
    }

    public String reason()
    {
        return Reason;
    }

    // treat as game in progress
    public void resume()
    {
        firstLetter = false;
    }


}
