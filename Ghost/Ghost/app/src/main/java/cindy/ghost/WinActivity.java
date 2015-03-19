package cindy.ghost;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class WinActivity extends Activity {

    HighScores highScores;
    String winnerName, reasonForWinning;

    Button goToRankingButton, newGameButton;
    TextView winnerNameTextView, wordResultTextView, newPlaceTextView;

    Boolean newNames;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i("WinScreen", "In onCreate");

        super.onCreate(null);
        setContentView(R.layout.activity_win);

        intent = getIntent();
        highScores = (HighScores) intent.getSerializableExtra("highScores");
        winnerName = intent.getStringExtra("winnerName");
        reasonForWinning = intent.getStringExtra("reasonForWinning");

        newGameButton = (Button) findViewById(R.id.newGameButton);
        goToRankingButton = (Button) findViewById(R.id.goToRankingButton);

        winnerNameTextView = (TextView) findViewById(R.id.winnerNameTextView);
        wordResultTextView = (TextView) findViewById(R.id.wordResultTextView);
        newPlaceTextView = (TextView) findViewById(R.id.newPlaceTextView);

        newNames = false;

        display();
    }

    private void display() {

        Log.i("WinActivity", "In display()");

        winnerNameTextView.setText(winnerName + " wins!");
        wordResultTextView.setText(reasonForWinning);

        int newPlace = highScores.newRankingPlace + 1;
        int oldPlace = highScores.oldRankingPlace + 1;

        if (newPlace == oldPlace) {
            newPlaceTextView.setText(winnerName + " is at place " +
                    Integer.toString(newPlace));
        } else {
            newPlaceTextView.setText(winnerName + " went from place " +
                    Integer.toString(oldPlace) + " to " + Integer.toString(newPlace) + "!");
        }
    }

    public void backToMain(){
        intent = new Intent();
        intent.putExtra("newNames", newNames);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void newGameButtonClicked(View view) {
        Log.i("WinScreen", "In toMainActivity");
        newNames = false;
        backToMain();
    }

    public void toRankingScreen(View view) {
        Log.i("WinScreen", "In toRankingScreen");

        intent = new Intent(this, RankingActivity.class);
        intent.putExtra("highScores", highScores);
        startActivity(intent);
    }

    public void changePlayerNames(View view){
        newNames = true;
        backToMain();
    }
}
