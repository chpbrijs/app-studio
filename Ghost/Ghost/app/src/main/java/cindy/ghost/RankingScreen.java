package cindy.ghost;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.StringTokenizer;

public class RankingScreen extends Activity {

    TextView namesTextView, scoresTextView;
    HighScores highScores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.high_scores);

        Intent intent = getIntent();
        highScores = (HighScores) intent.getSerializableExtra("highScores");

        namesTextView = (TextView) findViewById(R.id.rankingNames);
        scoresTextView = (TextView) findViewById(R.id.rankingScores);
        display();
    }

    public void display() {
        String names, scores, names_scores;

        names_scores = highScores.makeString(true);
        StringTokenizer stringTokenizer = new StringTokenizer(names_scores, highScores.delimiter);
        names = stringTokenizer.nextToken();
        scores = stringTokenizer.nextToken();

        namesTextView.setText(names);
        scoresTextView.setText(scores);
    }
}


