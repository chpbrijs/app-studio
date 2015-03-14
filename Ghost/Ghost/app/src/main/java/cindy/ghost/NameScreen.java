package cindy.ghost;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NameScreen extends Activity {

    EditText player1EditText, player2EditText;
    Spinner spinner1, spinner2;
    Button player1nameButton, player2nameButton;
    String player1name, player2name;
    ArrayAdapter<String> adapter1, adapter2;
    HighScores highScores;
    int comesFromOnCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("NameScreen","In onCreate");

        super.onCreate(savedInstanceState);
        comesFromOnCreate = 2;
        setContentView(R.layout.names);

        player1EditText = (EditText) findViewById(R.id.player1name);
        player2EditText = (EditText) findViewById(R.id.player2name);
        player1nameButton = (Button) findViewById(R.id.player1nameButton);
        player2nameButton = (Button) findViewById(R.id.player2nameButton);
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        spinner1.setVisibility(View.VISIBLE);
        spinner2.setVisibility(View.VISIBLE);

        player1name = "";
        player2name = "";

        Intent playerNamesIntent = getIntent();

        int numberOfPlayers;
        List<String> allNames;

        try {
            highScores = (HighScores) playerNamesIntent.getSerializableExtra("highScores");
            allNames = highScores.ranking;
            Collections.sort(allNames);
            numberOfPlayers = highScores.numberOfPlayers;
            Log.i("NameScreen", "highScore received in intent");

        } catch (NullPointerException npExc){
            allNames = new ArrayList<String>();
            Log.i("NameScreen", "No highScore received in intent");
            numberOfPlayers = 0;
        }

        if (numberOfPlayers <= 2){
            spinner1.setVisibility(View.INVISIBLE);
            spinner2.setVisibility(View.INVISIBLE);
        }
        else {
            initSpinners(allNames);
        }

        try {
            player1name = playerNamesIntent.getStringExtra("player1name");
            player2name = playerNamesIntent.getStringExtra("player2name");
            Log.i("NameScreen","In onCreate: found player names from intent: " + player1name +
            " and " + player2name);

        } catch (NullPointerException npExc){
            Log.i("NameScreen", "No names received in intent");
        }
    }

    public void initSpinners(List<String> allNames){

        Log.i("NameScreen", "In initSpinners");

        adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, allNames);
        adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, allNames);

        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner1.setAdapter(adapter1);
        spinner2.setAdapter(adapter2);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Log.i("NameScreen", "Selected in spinner 1: " +
                        parent.getItemAtPosition(position).toString());

                if (comesFromOnCreate > 0) {
                    comesFromOnCreate--;
                    return;
                }

                player1name = parent.getItemAtPosition(position).toString();
                player1EditText.setText(player1name);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.i("", "Nothing Selected 1");
            }
        });

        spinner1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                player1name = spinner1.getSelectedItem().toString();
                player1EditText.setText(player1name);
                return false;
            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Log.i("NameScreen","Selected in spinner 2: " +
                        parent.getItemAtPosition(position).toString());

                if (comesFromOnCreate > 0){
                    comesFromOnCreate --;
                    return;
                }

                player2name = parent.getItemAtPosition(position).toString();
                player2EditText.setText(player2name);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {Log.i("","Nothing Selected 2");}

        });

        spinner2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                player2name = spinner2.getSelectedItem().toString();
                player2EditText.setText(player2name);
                return false;
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("NameScreen", "in onStart");

        if (highScores.contains(player1name)){
            spinner1.setSelection(adapter1.getPosition(player1name));
        }
        if (highScores.contains(player2name)){
            spinner2.setSelection(adapter2.getPosition(player2name));
        }

        player1EditText.setText(player1name);
        player2EditText.setText(player2name);
    }

    public void startGame(View view){
        player1name = player1EditText.getText().toString();
        player2name = player2EditText.getText().toString();

        if (isValidName(player1name)&& isValidName(player2name) &&
                !player1name.toLowerCase().equals( player2name.toLowerCase() ) ) {

            Log.i("NameScreen", "Names are valid");

            Intent namesIntent = new Intent();

            namesIntent.putExtra("player1name", toNameStyle(player1name));
            namesIntent.putExtra("player2name", toNameStyle(player2name));

            setResult(RESULT_OK, namesIntent);

            Log.i("NameScreen", "Going back to main");
            finish();
        }
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

    public Boolean isValidName(String name){
        int n = name.length();

        if (n == 0){
            return false;
        }

        for (int i = 0; i < n; i++){
            Character myChar = name.charAt(i);

            if (!Character.isLetter(myChar) && myChar != ' '){
                return false;
            }
        }

        return true;
    }

    public void clearEditText(View view){
        if (view == player1nameButton){
            player1EditText.setText("");
        }
        else if (view == player2nameButton){
            player2EditText.setText("");
        }
    }
}
