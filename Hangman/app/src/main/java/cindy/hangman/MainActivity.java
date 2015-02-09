package cindy.hangman;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

    // about the word and the asked characters
    public String the_word;
    public int word_length;
    public char[] word_copy, wrong_chars;

    // about the list of words
    public String[] word_list;
    public int numWords;

    // about the widgets
    private EditText guessEditText;
    private TextView solutionText, scoreText, wrongLetters;
    private ImageView imageHangman;

    // about the variables
    public int triesCount;
    public Boolean game;

    protected void onCreate(Bundle savedInstanceState) {
        // Define all the widgets and initialize the game

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        guessEditText = (EditText) findViewById(R.id.guessEditText);
        solutionText = (TextView) findViewById(R.id.solutionWord);
        scoreText = (TextView) findViewById(R.id.scoreText);
        imageHangman = (ImageView) findViewById(R.id.imageHangman);
        wrongLetters = (TextView) findViewById(R.id.wrongLetters);

        initList();
        initializeGame();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void enterGuess(View view) {
        // when the user clicks on the 'go'-button

        String guessString = String.valueOf(guessEditText.getText());
        guessEditText.setText("");
        Boolean good_guess = false;

        // check if the user typed anything and if the game is not ended
        if (guessString.length() != 1 || !game){
            return;
        }

        int guessChar = (int) guessString.charAt(0);

        // check whether the input is a letter
        if (guessChar < 'a' || guessChar > 'z'){
            if (guessChar >= 'A' && guessChar <= 'Z'){
                guessChar += ('a' - 'A');
            }
            else {
                return;
            }
        }

        // check whether the letter has not already been asked
        for (int i = 0; i < triesCount; i++){
            if (guessChar == (int) wrong_chars[i]){
                return;
            }
        }

        // the letter is either a success or a failure
        for (int i = 0; i < word_length; i++) {

            if (word_copy[i] == '.' && the_word.charAt(i) == guessChar) {

                word_copy[i] = (char) guessChar;
                good_guess = true;
            }

            // invalid input if the letter is already found in the word
            else if (word_copy[i] == guessChar){
                return;
            }
        }

        // if it is a bad guess, the figure changes and the number of tries increments
        if (!good_guess) {
            // save the bad letter
            wrong_chars[triesCount] = (char) guessChar;

            triesCount++;
            changeHangmanFigure();
        }

        // update the word and the wrong guesses
        display_word();
        display_wrong_chars();

        // check whether the game is won, else lost
        checkForWon();
        checkForLost();
    }

    public void checkForWon(){
        // check whether the user has won the game

        Boolean finished = true;
        for (int i = 0; i < word_length; i++) {
            if (word_copy[i] == '.') {
                finished = false;
                break;
            }
        }

        if (finished) {
            scoreText.setText(R.string.you_win);
            scoreText.setTextColor(0xff00ff00);
            game = false;
            imageHangman.setImageResource(R.drawable.hangman_won);
            guessEditText.setHint(R.string.hint_restart);
        }
    }

    public void checkForLost(){
        // check whether the user has lost the game

        if (triesCount == 10 && game) {
            scoreText.setText(R.string.you_lose);
            scoreText.setTextColor(0xffff0000);
            game = false;
            imageHangman.setImageResource(R.drawable.hangman_lost);
            solutionText.setText(the_word);
            guessEditText.setHint(R.string.hint_restart);
        }
    }

    public void restartGame(View view) {
        // when the user clicks on 'Restart'

        initializeGame();
    }

    public void initializeGame() {
        // initialize the game

        // the solution
        the_word = findNewWord();
        word_length = the_word.length();

        // the shown word as '....'
        word_copy = new char[word_length];
        for (int i = 0; i < word_length; i++) {
            word_copy[i] = '.';
        }

        // initialize variables
        game = true;
        triesCount = 0;
        wrong_chars = new char[10];

        // display the initialized state
        display_word();
        scoreText.setText("");
        imageHangman.setImageResource(R.drawable.hangman0);
        guessEditText.setHint(R.string.hint_new_letter);
        display_wrong_chars();
    }

    public void display_word() {
        // display the word with the letters that are known

        String word_shown = "";

        for (int i = 0; i < word_length; i++) {
            char c = word_copy[i];
            word_shown = word_shown.concat(c + " ");
        }

        solutionText.setText(word_shown);
    }

    public void display_wrong_chars(){
        // display the bad guesses which cannot be chosen again

        String wrong_char_string = "";
        for (int i = 0; i < triesCount; i++){
            char c = wrong_chars[i];
            wrong_char_string = wrong_char_string.concat(c + "\n");
        }

        wrongLetters.setText(wrong_char_string);
    }

    public void initList() {
        // initialize the list of words that the program chooses the solution from

        String allWords = "ACTUALLY EXPECTED PROPERTY ADDITION FOLLOWED PROVIDED ALTHOUGH " +
                "AMERICAN INCREASE RECEIVED ANYTHING INDUSTRY RELIGION BUILDING INTEREST REMEMBER " +
                "BUSINESS INVOLVED REQUIRED CHILDREN NATIONAL SERVICES COMPLETE ORGANIZE SOUTHERN " +
                "CONSIDER PERSONAL STANDARD CONTINUE PLANNING STRENGTH POSITION STUDENTS DECISION " +
                "POSSIBLE SUDDENLY DIRECTLY PRESSURE THINKING HAPPENED QUESTION DISTRICT PROBABLY " +
                "TOGETHER ECONOMIC PROBLEMS TRAINING EVIDENCE PROGRAMS";

        word_list = allWords.split(" ");
        numWords = word_list.length;
    }

    public String findNewWord(){
        // choose a word from the list randomly

        int index = (int) (Math.random() * numWords);
        String new_word = word_list[index];
        return makeLowerCase(new_word);
    }

    public String makeLowerCase(String word){
        // turn a (partly) upper case string into a lower case string

        int length = word.length();
        String new_word = "";

        for (int i = 0; i < length; i++){
            char c = word.charAt(i);
            if (c >= 'A' && c <= 'Z'){
                c = (char) (c + ('a' - 'A'));
            }
            new_word = new_word.concat(Character.toString(c));
        }

        return new_word;
    }

    public void changeHangmanFigure(){
        // change the figure of Hangman according to the current number of tries

        String imageName = "hangman" + triesCount;
        imageHangman.setImageResource(getImageId(this, imageName));
    }

    public static int getImageId(Context context, String imageName){
        // get the id of a certain image

        return context.getResources().getIdentifier("drawable/" + imageName, null, context.getPackageName());
    }
}

