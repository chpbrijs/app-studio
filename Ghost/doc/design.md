# Design



#### Screen 1: OpeningScreen

<img src='IMAG1028.jpg' width = '200', height = '350'/>

Views:
* TextView: title
* Button: 'Start'. Calls onStartButtonClick()

Methods:
* void onStartButtonClick(). Goes to SetNamesScreen 

#### Screen 2: SetNamesScreen

<img src='IMAG1029.jpg' width = '200', height = '350'/>

Views:
* TextView: 'Enter your names'
* Spinner 1: contains all names ever used
* Spinner 2: contains all names ever used, except for the one in spinner 1.
* Button: 'Go'. Calls startNewGameWithNames()

Methods:
* void startNewGameWithNames(). Saves the names and calls startNewGame().
* void startNewGame(). Chooses random player, initializes word and goes to MainActivity.

#### Screen 3: MainActivity

<img src='IMAG1030.jpg' width = '200', height = '350'/>

Views:
* TextView: 'John's turn' or 'Hans' turn' if it ends with a 's', 'z' or 'x'.
* TextView: display of the current word
* EditText: new letter. 
    The length is forced to 1.
* Button: 'Go'. 
    Calls makeMove(letter).

Methods:
* void makeMove(int letter). 

#### Screen 4: WinScreen

<img src='IMAG1033.jpg' width = '200', height = '350'/>

Views:

Methods:

#### Screen 5: HighscoresScreen

<img src='IMAG1034.jpg' width = '200', height = '350'/>

Views:

Methods:
