# Design



#### Screen 1: OpeningScreen

<img src='IMAG1028.jpg' width = '200', height = '350'/>

Views:<br /> 
1.  TextView: title<br /> 
2.  Button: 'Start'. Calls onStartButtonClick()<br /> 

Methods:<br /> 
1.  void onStartButtonClick(). <br /> 
    - Goes to SetNamesScreen <br /> 

#### Screen 2: SetNamesScreen

<img src='IMAG1029.jpg' width = '200', height = '350'/>

Views:<br /> 
1.  TextView: 'Enter your names'<br /> 
2.  Spinner 1: contains all names ever used<br /> 
3.  Spinner 2: contains all names ever used, except for the one in spinner 1.<br /> 
4.  Button: 'Go'. Calls startNewGameWithNames()<br /> 

Methods:<br /> 
1.  void startNewGameWithNames(). <br /> 
    -   Saves the names in public strings list<br /> 
    -   Calls startNewGame()<br /> 
2.  void startNewGame(). <br /> 
    -   Chooses random player<br /> 
    -   Initializes word<br /> 
    -   Goes to MainActivity<br /> 

#### Screen 3: MainActivity

<img src='IMAG1030.jpg' width = '200', height = '350'/>

Views:
1.  TextView: 'John's turn'<br /> 
    -   Or 'Hans' turn' if it ends with a 's', 'z' or 'x'.<br /> 
2.  TextView: display of the current word<br /> 
3.  EditText: new letter.<br /> 
    -   The length is forced to 1.<br /> 
4.  Button: 'Go'. Calls makeMove(letter).<br /> 

Methods:
1.  void onCreate(Bundle savedInstanceState)<br /> 
    -   if the static lists of dictionaries (English and Dutch) are empty: setDictionaries().<br /> 
2.  void setDictionaries()<br /> 
    -   for each of the empty lists: <br /> 
        -   make a scanner<br /> 
        -   read in the words from a file<br /> 
        -   save the words in the list of strings<br /> 
3.  void makeMove(int letter). <br /> 
    -   call checkValidLetter(letter)<br /> 
    -   call addLetterToWord(letter)<br /> 
    -   call checkForLost()<br /> 
        -   if not lost:<br /> 
            -   setNextPlayer()<br /> 
            else:<br /> 
            -   call editHighScores()<br /> 
            -   create intent containing the winner's name and the boolean isWholeWord<br /> 
            -   go to WinScreen with this intent<br /> 
4.  Boolean checkValidLetter(int letter)<br /> 
5.  void addLetterToWord()<br /> 
6.  Boolean checkForLost()<br /> 
    -   find the word in the dictionary that is the next word in alphabetical order<br /> 
    -   if the current word is not a fragment of this word (beginning at the first letter), return true<br /> 
    -   else if it is equal to the current word, return true<br /> 
    -   else return false<br /> 
7.  void setNextPlayer()<br /> 
    -   change the name in the TextView<br /> 
    -   change the colour<br /> 

#### Screen 4: WinScreen

<img src='IMAG1033.jpg' width = '200', height = '350'/>

Views:<br /> 

Methods:<br /> 

#### Screen 5: HighscoresScreen

<img src='IMAG1034.jpg' width = '200', height = '350'/>

Views:<br /> 

Methods:<br /> 
