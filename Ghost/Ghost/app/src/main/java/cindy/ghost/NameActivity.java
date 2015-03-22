package cindy.ghost;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NameActivity extends Activity {

    EditText player1EditText, player2EditText;
    Spinner spinner1, spinner2;
    Button player1clearButton, player2clearButton;
    String player1name, player2name;
    HighScores highScores;
    int comesFromOnCreate;
    List<String> allNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("NameScreen","In onCreate");

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_name);

        player1EditText = (EditText) findViewById(R.id.player1name);
        player2EditText = (EditText) findViewById(R.id.player2name);

        player1clearButton = (Button) findViewById(R.id.player1clearButton);
        player2clearButton = (Button) findViewById(R.id.player2clearButton);

        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner2 = (Spinner) findViewById(R.id.spinner2);

        spinner1.setVisibility(View.INVISIBLE);
        spinner2.setVisibility(View.INVISIBLE);

        Intent playerNamesIntent = getIntent();
        int numberOfPlayers = 0;

        try {
            highScores = (HighScores) playerNamesIntent.getSerializableExtra("highScores");
            allNames = highScores.ranking;
            Collections.sort(allNames);
            numberOfPlayers = highScores.numberOfPlayers;

        } catch (NullPointerException npExc){
            allNames = new ArrayList<String>();
        }

        try {
            player1name = playerNamesIntent.getStringExtra("player1name");
            player2name = playerNamesIntent.getStringExtra("player2name");

        } catch (NullPointerException npExc){
            player1name = "";
            player2name = "";
        }

        setupEditText(player1EditText, player1name);
        setupEditText(player2EditText, player2name);

        if (numberOfPlayers > 0){
            comesFromOnCreate = 2;
            setupSpinner(spinner1, player1EditText);
            setupSpinner(spinner2, player2EditText);
        }
    }

    private void setupEditText(EditText editText, String player_name){
        editText.setText(player_name);
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_DOWN &&
                        keyCode == KeyEvent.KEYCODE_ENTER){
                    startGame();
                    return true;
                }
                return false;
            }
        });
    }

    private void setupSpinner(final Spinner spinner, final EditText nameEditText){

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, allNames);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setVisibility(View.VISIBLE);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (comesFromOnCreate > 0){
                    comesFromOnCreate --;
                    return;
                }

                String playerName = parent.getItemAtPosition(position).toString();
                nameEditText.setText(playerName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        spinner.setOnTouchListener( new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                String playerName = spinner.getSelectedItem().toString();
                nameEditText.setText(playerName);
                return false;
            }
        });

        String playerName = nameEditText.getText().toString();
        if (allNames.contains(playerName)){
            spinner.setSelection(adapter.getPosition(playerName));
        }
    }

    private void startGame(){
        player1name = player1EditText.getText().toString().toLowerCase();
        player2name = player2EditText.getText().toString().toLowerCase();

        if (isValidName(player1name) && isValidName(player2name) &&
                !player1name.equals(player2name) ) {

            Intent namesIntent = new Intent();

            namesIntent.putExtra("player1name", toNameStyle(player1name));
            namesIntent.putExtra("player2name", toNameStyle(player2name));

            setResult(RESULT_OK, namesIntent);

            finish();
        }
    }

    private Boolean isValidName(String name){

        int n = name.length();

        if (n == 0) return false;

        for (int i = 0; i < n ; i++){

            Character letter = name.charAt(i);
            if (!Character.isLetter(letter) && letter != ' '){
                return false;
            }

        }

        return true;
    }

    public String toNameStyle(String name){

        name = name.trim();

        String[] words = name.split(" ");
        StringBuilder sb_name = new StringBuilder();
        for (String name_part : words){
            sb_name.append(name_part.toUpperCase().charAt(0));
            sb_name.append(name_part.substring(1, name_part.length()).toLowerCase());
            sb_name.append(" ");
        }
        return sb_name.toString().trim();
    }

    public void clearEditText(View view){
        if (view == player1clearButton){
            player1EditText.setText("");
        }
        else if (view == player2clearButton){
            player2EditText.setText("");
        }
    }

    public void startButtonClicked(View view){
        startGame();
    }
}
