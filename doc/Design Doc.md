# Design Document for Ghost App

<br>Joren de Bruin</br>
<br>10631267 </br>
<br>Native App Studio</br>

## Sketches
![sketch3008](https://cloud.githubusercontent.com/assets/11808883/7221031/aa19ab64-e6dc-11e4-9395-a91649c5b1a4.jpg)
![sketch2007](https://cloud.githubusercontent.com/assets/11808883/7221030/a64ff2c2-e6dc-11e4-9e43-a7bc30f83e3a.jpg)
![sketch1006](https://cloud.githubusercontent.com/assets/11808883/7220993/a35ee29e-e6dc-11e4-80c0-c5370a9ed67f.jpg)



## Classes and Methods
###Player.java Activity
* Receives player input through EditText prompt
* Passes player names as String intents to MainActivity.java, starts MainActivity.java once 2 names have been selected or entered.
* Can be accessed through MainActivity.java->Restart.java or through MainActivity.java when a game has ended.


###MainActivity.java
* passes player names and scores as String intents to Highscore.java activity
* settings on action bar-> intents to start Highscore.java, Language.java, Player.java, or Restart.java
* Can restart itself when a game has ended or go to Player.java to enter new names


###Classes called within this activity:

**Dictionary.java** (takes a string of dictionary words as a constructor parameter):
* public String filter (takes word string as argument, returns filtered dictionary string)
* public int count (takes word string as argument, returns amount of words in filtered dictionary)
* public String result (takes word string as argument, returns the words in filtered dictionary)
* public String reset (returns original set of dictionary words)

**Game.java:**
* public void guess (takes user-entered letter as an argument)
* public boolean turn (returns True if it's player 1s turn, passes turn to next player if called)
* public boolean ended (returns True if the current word does not exist in the dictionary or if it completely matches a single remaining word in the filtered dictionary) 
* public boolean winner (returns True if the game ended during player 1s turn)


###Highscore.java
* receives intents from MainActivity.java
* Displays usernames and scores as a listview of textviews

###Language.java

* checkboxes to select Dictionary language.
* Passes appropriate Dictionary string to Dictionary.java
* Accessed through Settings in MainActivity.java->Restart.java.

###Restart.java

* called from MainActivity.java (either as a discrete option in the settings list or by going to Language.java or Player.java through settings)
* Throws up a confirmation prompt with two buttons (yes/no), (re)starts Language.java, Player.java or MainActivity.java on a 'yes'.
* Returns to the current MainActivity.java on a 'no'.



