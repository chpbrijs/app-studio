# Design



#### Screen 1: OpeningScreen

<img src='IMAG1028.jpg' width = '200', height = '350'/>

Views:

1.  TextView: title
2.  Button: 'Start'. Calls onStartButtonClick()

Methods:

1.  void onStartButtonClick(). 
    1.  Goes to SetNamesScreen

#### Screen 2: SetNamesScreen

<img src='IMAG1029.jpg' width = '200', height = '350'/>

Views:

1.  TextView: 'Enter your names'
2.  Spinner 1: contains all names ever used
3.  Spinner 2: contains all names ever used
4.  Button: 'Go'. Calls startNewGameWithNames()

Methods:

1.  void startNewGameWithNames(). 
    1.   Save the names (if valid and not the same) in public strings list
    2.   Call startNewGame()
2.  void startNewGame(). 
    1.   Choose random player
    2.   Initialize word 
    3.   Show the right name and colour
    4.   Go to MainActivity

#### Screen 3: MainActivity

<img src='IMAG1030.jpg' width = '200', height = '350'/>

Variables:

1.  public String current_word
2.  public String[] current_player_names
3.  public Boolean player2turn 
    1.  0 if it's player1's turn and 1 if it's player2's turn
4.  public final List<String> dictionary_en
5.  public final List<String> dictionary_nl
6.  public Hashtable name_score_pairs
7.  public List<String> player_ranking

Views:

1.  TextView: 'John's turn'
    1.  Or 'Hans' turn' if it ends with a 's', 'z' or 'x'.
2.  TextView: display of the current word
3.  EditText: new letter.
    1.  The length is forced to 1.
4.  Button: 'Go'. Calls makeMove(letter).

Methods:

1.  void onCreate(Bundle savedInstanceState)
    1. if the static lists of dictionaries (English and Dutch) are empty: setDictionaries(). 
2.  void setDictionaries()
    1.  for each of the empty lists: 
        1.  make a scanner
        2.  read in the words from a file
        3.  save the words in the list of strings
3.  void makeMove(int letter). 
    1.  call checkValidLetter(letter)
    2.  call addLetterToWord(letter)
    3.  call checkForLost()
        1.  if not lost:
            1.  setNextPlayer()
        2.  else:
            1.  call editHighScores()
            2.  create intent containing the winner's name and the boolean isWholeWord
            3.  go to WinScreen with this intent
4.  Boolean checkValidLetter(int letter)
5.  void addLetterToWord() 
6.  Boolean checkForLost()
    1.  find the word in the dictionary that is the next word in alphabetical order
    2.  if the current word is not a fragment of this word (beginning at the first letter), return true
    3.  else if it is equal to the current word, return true
    4.  else return false
7.  void setNextPlayer()
    1.  change the name in the TextView
    2.  change the colour
8. void editHighScores()
    1.  increment the score in name_score_pairs
    2.  move the name in player_ranking forward if necessary

#### Screen 4: WinScreen

<img src='IMAG1033.jpg' width = '200', height = '350'/>

Views:

1.  TextView: display the winner's name
2.  TextView: state the reason for winning
3.  TextView: show the current place in the high score list
4.  Button: 'view high score'. Calls toHighScoresScreen().
5.  Button: 'new game'. Calls startNewGame()

Methods:

1.  void toHighScoresScreen()
    1.  Use an intent to go to HighScoresScreen
2.  void startNewGame(). 
    1.   Choose random player
    2.   Initialize word 
    3.   Show the right name and colour
    4.   Go to MainActivity

#### Screen 5: HighScoresScreen

<img src='IMAG1034.jpg' width = '200', height = '350'/>

Variables:

private String content

Views:

1.  TextView: 'High scores'
2.  TextView: position, name, score
    1.  only the five highest scores are shown
    2.  if there are less than five available, only show those available
3.  Button: 'new game'. Calls startNewGame().

Methods:

1. void fillHighScoreTextViews(). This function gets called when the activity receives the intent from WinScreen.
    1.  make the string 'content' empty
    2.  for the indices 0 to 4:
        1.  find the name in player_ranking at this index
        2.  find the score in name_score_pairs using this name
        3.  make a string consisting of (index + 1), name and score
        4.  add '\n' and this string to content
    3.  set text in the corresponding TextView to content
2.  void startNewGame(). 
    1.   Choose random player
    2.   Initialize word 
    3.   Show the right name and colour
    4.   Go to MainActivity 
