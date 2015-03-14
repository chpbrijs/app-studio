package cindy.ghost;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

    EditText newLetterEditText;
    TextView nameView, wordView;
    Button goButton;

    GamePlay gamePlay;
    Dictionary dictionary;
    HighScores highScores;

    String player1name, player2name, winnerName, reasonForWinning, bad_string;
    Boolean comesFromOnCreate, resetGame;
    int GET_NAMES_MAIN = 101;
    int GET_NAMES_WIN_SCREEN = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i("MainActivity", "In onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadDictionaries();
        highScores = new HighScores();
        gamePlay = new GamePlay(dictionary);

        newLetterEditText = (EditText) findViewById(R.id.edit_text_new_letter);
        nameView = (TextView) findViewById(R.id.current_player_name);
        wordView = (TextView) findViewById(R.id.word_display);
        goButton = (Button) findViewById(R.id.go_button);

        newLetterEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_DOWN &&
                        keyCode == KeyEvent.KEYCODE_ENTER){
                    enterLetter();
                    return true;
                }
                return false;
            }
        });

        comesFromOnCreate = true;
        bad_string = "__not_good__";
        player1name = bad_string;
        player2name = bad_string;

        resetGame = false;
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
        if (id == R.id.menu_restart_game) {
            gamePlay.restartCurrentGame();
            display();
            return true;
        }
        else if(id == R.id.menu_change_language){
            gamePlay.restartCurrentGame();
            display();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void loadDictionaries(){

        Log.i("","Loading dictionaries...");

        String language = "nl";

        long t0, t1, delta_t;
        t0 = System.currentTimeMillis();
        dictionary = new Dictionary(getApplicationContext(), language);
        t1 = System.currentTimeMillis();
        delta_t = t1 - t0;

        Log.i("", "Loaded dictionary in " + delta_t + " milliseconds");
    }

    public void changeNames(){
        Log.i("MainActivity", "In changeNames");
        Intent getNamesIntent = new Intent(this, NameScreen.class);

        if (!player1name.equals(bad_string) && !player2name.equals(bad_string)) {
            getNamesIntent.putExtra("player1name", player1name);
            getNamesIntent.putExtra("player2name", player2name);
        }

        if (highScores.numberOfPlayers > 0){
            getNamesIntent.putExtra("highScores", highScores);
        }

        startActivityForResult(getNamesIntent, GET_NAMES_MAIN);
    }

    public void change_name_button_clicked(View view){
        changeNames();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("MainActivity", "In onActivityResult, code: " + requestCode);
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) return;

        if (requestCode == GET_NAMES_MAIN) {
            Log.i("","intent came from NameScreen");
            player1name = data.getStringExtra("player1name");
            player2name = data.getStringExtra("player2name");

            Log.i("MainActivity", "Found player names");
        }
        else if (requestCode == GET_NAMES_WIN_SCREEN){
            Log.i("","intent came from WinScreen");
            Boolean newNames = data.getBooleanExtra("newNames", false);
            if (newNames) changeNames();
        }

        resetGame = true;
    }

    @Override
    protected void onStart() {
        Log.i("MainActivity", "In onStart");
        super.onStart();

        if (resetGame) gamePlay.reset();
        resetGame = false;

        if (comesFromOnCreate) {
            recallGameState();
            Log.i("MainActivity", "Game state recalled in onStart");
        }

        if (player1name.equals(bad_string) || player2name.equals(bad_string)){
            Log.i("MainActivity","In onStart, but there are no names yet");
            changeNames();
        }

        if(!gamePlay.game){
            toWinScreen();
        }

        else{
            display();
        }

        comesFromOnCreate = false;
    }

    public void enterLetter(){
        String something_entered = newLetterEditText.getText().toString().toLowerCase();
        if (something_entered.length() == 0) {
            return;
        }

        Character letter = something_entered.charAt(0);
        if (!Character.isLetter(letter)){
            return;
        }

        if (player1name.equals(bad_string)) return;

        gamePlay.makeMove(letter);

        if (gamePlay.game) {
            display();
        }
        else {
            toWinScreen();
        }
    }

    public void onGoButtonClicked(View view){   enterLetter();    }

    public void display(){

        Log.i("MainActivity", "In display()");

        newLetterEditText.setText("");
        wordView.setText(gamePlay.word_fragment);
        newLetterEditText.setHint("Enter a letter");
        newLetterEditText.setText("");

        if (player1name.equals(bad_string)){
            nameView.setText("");
            return;
        }

        String currentPlayer;
        if (gamePlay.player1turn){
            currentPlayer = player1name;
        }
        else{
            currentPlayer = player2name;
        }

        Character last_char = (currentPlayer.charAt(currentPlayer.length() - 1));
        if (last_char != 's' && last_char != 'z' && last_char != 'x'){
            nameView.setText(currentPlayer.concat("'s turn"));
            return;
        }
        nameView.setText(currentPlayer.concat("' turn"));
    }

    public void toWinScreen(){
        Log.i("MainActivity","In toWinScreen");

        resetGame = true;

        highScores.addPlayer(player1name);
        highScores.addPlayer(player2name);

        if (gamePlay.player1wins){
            winnerName = player1name;
        }
        else{
            winnerName = player2name;
        }

        if (!comesFromOnCreate) {
            highScores.incrementScoreFor(winnerName);
            Log.i("MainActivity","Incremented high score for winner");
        }

        if (gamePlay.wonByMakingWord){
            reasonForWinning = gamePlay.word_fragment.concat(" is a word!");
        }
        else {
            reasonForWinning = gamePlay.word_fragment.concat(" cannot form a word!");
        }

        Intent intent = new Intent(this, WinScreen.class);

        intent.putExtra("highScores", highScores);
        intent.putExtra("winnerName", winnerName);
        intent.putExtra("reasonForWinning", reasonForWinning);
        startActivityForResult(intent, GET_NAMES_WIN_SCREEN);
    }

    @Override
    protected void onStop() {
        Log.i("MainActivity","In onStop");
        saveGameState();
        super.onStop();
    }

    public void saveGameState(){
        Log.i("MainActivity", "Saving...");

        SharedPreferences.Editor spEditor = getPreferences(Context.MODE_PRIVATE).edit();

        spEditor.putString("player1name", player1name);
        spEditor.putString("player2name", player2name);

        String highScoresString = highScores.makeString(false);
        if (highScoresString.equals("")){highScoresString = bad_string;}
        spEditor.putString("highScoresString", highScoresString);
        int ranking_old_new = (highScores.oldRankingPlace + 1) * 100 +
                (highScores.newRankingPlace + 1);
        spEditor.putInt("ranking_old_new", ranking_old_new);

        String word_fragment = gamePlay.word_fragment;
        if (word_fragment.equals("")){word_fragment = bad_string;}
        spEditor.putString("word_fragment", word_fragment);
        spEditor.putBoolean("player1turn", gamePlay.player1turn);
        spEditor.putBoolean("player1wins",gamePlay.player1wins);

        spEditor.apply();
    }

    public void recallGameState(){
        Log.i("MainActivity","In recallGameState");
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);

        player1name = sharedPreferences.getString("player1name", bad_string);
        player2name = sharedPreferences.getString("player2name", bad_string);

        Log.i("","Found player names:" + player1name + " and " + player2name);

        String highScoresString = sharedPreferences.getString("highScoresString", bad_string);
        int ranking_old_new = sharedPreferences.getInt("ranking_old_new", -1);

        Log.i("","Found high score components");

        String word_fragment = sharedPreferences.getString("word_fragment", bad_string);
        Boolean player1turn = sharedPreferences.getBoolean("player1turn", true);
        Boolean player1wins = sharedPreferences.getBoolean("player1wins", true);

        Log.i("","Found game play components");

        if (!highScoresString.equals(bad_string)){
            highScores.findHighScoresFromString(highScoresString);
            Log.i("MainActivity","Found high scores");

            if (ranking_old_new > 0){
                highScores.oldRankingPlace = ranking_old_new / 100 - 1;
                highScores.newRankingPlace = ranking_old_new % 100 - 1;
                Log.i("MainActivity","Found last changed rankings");
            }
            else{
                Log.i("","ranking not found");
            }
        }
        else{
            Log.i("","No high score string found");
        }

        if (!word_fragment.equals(bad_string)){
            gamePlay.makeGamePlay(word_fragment, player1turn, player1wins);
            Log.i("MainActivity","Found game situation");
        }
        else{
            Log.i("","word fragment not found");
        }

        Log.i("","GameState successfully recalled");
    }
}
