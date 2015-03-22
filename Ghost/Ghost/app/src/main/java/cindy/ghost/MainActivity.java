package cindy.ghost;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends ActionBarActivity {

    EditText newLetterEditText;
    TextView nameView, wordView;

    GamePlay gamePlay;
    Dictionary dictionary;
    HighScores highScores;

    String player1name, player2name, winnerName, reasonForWinning;

    Boolean comesFromOnCreate;

    List<String> all_languages = Arrays.asList("en", "nl");
    Map<String, Dictionary> all_dictionaries;
    String current_language, default_language;

    final int GET_NAMES_MAIN = 101;
    final int GET_NAMES_WIN_SCREEN = 102;
    final int GET_LANGUAGE = 103;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i("MainActivity", "In onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        default_language = "nl";
        all_dictionaries = new HashMap<String, Dictionary>();

        loadDictionaries();
        initiateGameState();

        newLetterEditText = (EditText) findViewById(R.id.edit_text_new_letter);
        nameView = (TextView) findViewById(R.id.current_player_name);
        wordView = (TextView) findViewById(R.id.word_display);

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
            gamePlay.reset();
            display();
            return true;
        }
        else if(id == R.id.menu_change_language){
            Intent intent = new Intent(this, LanguageActivity.class);
            intent.putExtra("current_language", current_language);
            startActivityForResult(intent, GET_LANGUAGE);
            return true;
        }
        else if (id == R.id.change_players){
            changePlayerNames();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void loadDictionaries(){

        Context ctx = getApplicationContext();

        for (String language : all_languages) {

            Log.i("","Loading dictionary for: " + language);
            Dictionary myDictionary = new Dictionary(language, ctx);

            all_dictionaries.put(language, myDictionary);
        }
    }

    public void changePlayerNames(){
        Log.i("MainActivity", "In changePlayerNames");
        Intent getNamesIntent = new Intent(this, NameActivity.class);

        getNamesIntent.putExtra("player1name", player1name);
        getNamesIntent.putExtra("player2name", player2name);
        getNamesIntent.putExtra("highScores", highScores);

        startActivityForResult(getNamesIntent, GET_NAMES_MAIN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.i("MainActivity", "In onActivityResult, code: " + requestCode);
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) return;

        switch (requestCode){
            case GET_NAMES_MAIN:
                Log.i("","intent came from NameActivity");
                player1name = data.getStringExtra("player1name");
                player2name = data.getStringExtra("player2name");
                gamePlay.reset();
                display();

                Log.i("MainActivity", "Found player names");
                break;

            case GET_NAMES_WIN_SCREEN:
                Log.i("","intent came from WinScreen");
                Boolean newNames = data.getBooleanExtra("newNames", false);
                gamePlay.reset();
                display();
                if (newNames) changePlayerNames();
                break;

            case GET_LANGUAGE:
                Log.i("","intent came from DictionariesActivity");

                String result_language = data.getStringExtra("result_language");
                if (current_language.equals(result_language)) {
                    Log.i("","Did not change language");
                    break;
                }

                current_language = result_language;
                dictionary = all_dictionaries.get(current_language);
                gamePlay.changeDictionary(dictionary);

                Log.i("","Changed language to: " + current_language);
                break;

            default:
                break;
        }
    }

    @Override
    protected void onStart() {

        Log.i("MainActivity", "In onStart");
        super.onStart();

        if (player1name.equals("") || player2name.equals("")){
            Log.i("MainActivity","In onStart, but there are no names yet");
            changePlayerNames();
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

        if (!gamePlay.game) return;

        String something_entered = newLetterEditText.getText().toString().toLowerCase();
        if (something_entered.length() == 0) {
            return;
        }

        Character letter = something_entered.charAt(0);

        if (player1name.equals("") || player2name.equals("")) return;

        gamePlay.makeMove(letter);

        if (gamePlay.game) {
            display();
        }
        else {
            toWinScreen();
        }
    }

    public void display(){

        Log.i("MainActivity", "In display()");

        newLetterEditText.setText("");
        wordView.setText(gamePlay.word_fragment);
        newLetterEditText.setText("");

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

        if (player1name.isEmpty() || player2name.isEmpty() || gamePlay.word_fragment.isEmpty()){
            return;
        }

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
            gamePlay.swapBeginPlayer();
            Log.i("MainActivity","Incremented high score for winner");
        }

        if (gamePlay.wonByMakingWord){
            reasonForWinning = gamePlay.word_fragment.concat(" is a word!");
        }
        else {
            reasonForWinning = gamePlay.word_fragment.concat(" cannot form a word!");
        }

        Intent intent = new Intent(this, WinActivity.class);

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

    @Override
    public void onBackPressed(){
        changePlayerNames();
    }

    public void saveGameState(){

        SharedPreferences.Editor spEditor = getPreferences(Context.MODE_PRIVATE).edit();

        spEditor.putString("player1name", player1name);
        spEditor.putString("player2name", player2name);

        spEditor.putString("current_language", current_language);

        highScores.saveGameState(spEditor);
        gamePlay.saveGameState(spEditor);

        spEditor.apply();
        Log.i("MainActivity", "Saved preferences");
    }

    public void initiateGameState(){

        Log.i("MainActivity","In recallGameState");

        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);

        player1name = sharedPreferences.getString("player1name", "");
        player2name = sharedPreferences.getString("player2name", "");

        current_language = sharedPreferences.getString("current_language", default_language);
        dictionary = all_dictionaries.get(current_language);

        highScores = new HighScores();
        highScores.recallGameState(sharedPreferences);

        gamePlay = new GamePlay(dictionary);
        gamePlay.recallGameState(sharedPreferences);
    }
}
