# Ghost app for Android

<br>Joren de Bruin</br>
<br>10631267 </br>
<br>Native App Studio</br>

## Code Summary

### Starting up

On startup, the application tries to load a hashmap from internal memory containing the user names and scores as key/value pairs respectively,
if these have been saved in a previous session. This is done by way of a fileinputstream.
It subsequently determines through SharedPreferences whether a game was in progress when the application was shut down the last time.

The Boolean used to check for this, 'first', behooves some explanation: the idea is that it will only load a state if the application was quit while 
in an unfinished state(so with a partial word entered), but if the user quit the app during a victory or on a reset, the application will simply start
up as if no game was in progress at that point. In other words, the application only loads a state if at least one letter was entered at the time of shutdown.

If no game was in progress, the application immediately jumps to the game setup screen, called NamesActivity. 
The NamesActivity screen, as displayed in the screenshot below, will often be the first thing a user sees after starting the app.

<img src="https://cloud.githubusercontent.com/assets/11808883/7773972/1f397606-00a8-11e5-9e45-ae6a17cb4414.png" width="220" height="400" />
<img src="https://cloud.githubusercontent.com/assets/11808883/7773976/221b9570-00a8-11e5-80ae-dcf3d01bdbcd.png" width="220" height="400" />

The NamesActivity screen allows a user to set up the game by entering names for both players and selecting a language. If a list of existing names
was found when the program starts, these will be displayed as spinner widgets next to the name entry EditText widgets to allow users to select an existing name.
The names inside these spinners also display a players score (or amount of wins).

The EditText widgets validate the text input in a number of ways to prevent duplicates, null value entries, and names that are too long (> 10 characters).
In addition, the activity can not be finished until both users have selected a name (the dictionary takes English as a default language, so it will
activate even if the user didn't specify a language). New names are added to the existing list and their score is set to 0.

On specifying both names and (possibly) a dictionary language, NamesActivity can be finished by tapping the "Start Game" button or by swiping to the right. This returns the user to MainActivity, which controls much of the main game,
while also passing along the player names and the dictionary language through putExtra(). 
At this point the application creates instances of the dictionary and game classes based on the provided parameters.


### Playing the Game

The EditText widget in MainActivity is heavily limited and only allows a user to enter a single, lowercase character at a time. Text entry requires a single confirmation by tapping the enter button on the keyboard.
Entering a letter triggers a fairly robust function call: the entered letter is appended to the word that is being built and displayed in the textView, the dictionary instance uses that word to filter itself,
and the game instance 'advances' a turn, changing the textView to prompt the appropriate player to enter a letter. 
Additionally, the current state of the game is saved at this point by storing the language, player names, and turn in preferences. The final step of entering a letter is a function call to
victoryChecker(), which, as the name might imply, checks if a victory or loss condition has been satisfied.

<img src="https://cloud.githubusercontent.com/assets/11808883/7773977/24004354-00a8-11e5-8f34-d948fec3b423.png" width="220" height="400" />
<img src="https://cloud.githubusercontent.com/assets/11808883/7773989/39250d1e-00a8-11e5-90df-8308121b4861.png" width="220" height="400" />

If a victory condition is satisfied, the game is 'stopped'. More specifically, the editText widget is disabled to prevent players from entering additional characters, and the textViews are set to 
declare the winner, their total score, and their reason for winning. The players saved score is immediately updated.
At this point (but really at any point during the game), users can swipe to the right to access the highscore screen (ScoreActivity), swipe left to access the game setup, or tap the "quick reset" button to instantly start a new game with the same player names and dictionary language.
Accessing NamesActivity during a game will reset the game.

### Highscores and saving
 
The HashMap ScoreList, containing player names and associated scores, is used to create the ArrayList NameList, which presents the users and their scores 
in an ordered fashion, descending from highest to lowest. Starting ScoreActivity, in turn, fills a listview with the data in this arraylist, to display
all the players' scores. This list is non-interactive, but the button on the bottom of the screen does allow the user to delete all saved data.

<img src="https://cloud.githubusercontent.com/assets/11808883/7773994/3fa15bc0-00a8-11e5-84e3-737f1363be2d.png" width="220" height="400" />

Tapping this button removes the listview, as well as deleting all data in SharedPreferences and the saved HashMap of playerscores, essentially causing a full
reset of the application. Finishing this activity with a swipe to the left immediately takes the user back to NamesActivity rather than the game, so as to prevent
bugs caused by having an empty list of names but trying to play a game with them nonetheless.

Saving the HashMap ScoreList is done by a fileoutputstream, and is generally done whenever the HashMap is modified or one of its values is updated.
