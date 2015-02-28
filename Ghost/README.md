# Ghost

#### Summary
This app is the game Ghost which is a word game for two players on the same device. A player loses the game by choosing an invalid letter or by making an existing word, consisting of four or more letters.

#### Features
This app has the following features:

1.  Remembering of player's names in following games.
2.  A highscore board where scores will be added automatically
3.  The players take turns on choosing a letter via an on-screen keyboard. 
4.  There is a choice of dictionaries: English and Dutch.
5.  The option menu provides possibilities to change names, change the language or restart the game.
7.  The app can be used on smartphones and tablets.

#### Sketches

Screen 1: Opening screen

<img src='doc/IMAG1028.jpg' width = '200', height = '350'/>

Screen 2: Choose names for the players

<img src='doc/IMAG1029.jpg' width = '200', height = '350'/>

Screen 3: Playing the game. The current player's name is shown above the current state of the word. The current player must choose a certain alphabetical character. The screens of both player have different appearances (colors et cetera).

<img src='doc/IMAG1030.jpg' width = '200', height = '350'/>

In the options menu the game can be restarted. Also the names and the language can be changed.

<img src='doc/IMAG1031.jpg' width = '200', height = '350'/> <img src='doc/IMAG1032.jpg' width = '200', height = '350'/>

Screen 4: End of game. A player has won and will know the place in the highscores list. The game can be restarted immediately.

<img src='doc/IMAG1033.jpg' width = '200', height = '350'/>

Screen 5: Highscores. If the winning player wants to view the highscores, the corresponding button in screen 4 can be clicked, which leads to the following screen.

<img src='doc/IMAG1034.jpg' width = '200', height = '350'/>

#### Frameworks

1.  Relative layout will be used.
2.  There will be a need for functionality for buttons, edittexts, spinners and textviews.
3.  There are 5 different screens, so there are 5 activities. Functionality for changing activities will be needed.
4.  The names, scores and the current game must be saved within the application. Also after closing.
5.  The action bar will provide a menu with options corresponding to the current screen.
