package cindy.ghost;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.StringTokenizer;

public class RankingActivity extends Activity {

    TextView rankingTextView,namesTextView, scoresTextView;
    HighScores highScores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        Intent intent = getIntent();
        highScores = (HighScores) intent.getSerializableExtra("highScores");

        rankingTextView = (TextView) findViewById(R.id.rankingNumbers);
        namesTextView = (TextView) findViewById(R.id.rankingNames);
        scoresTextView = (TextView) findViewById(R.id.rankingScores);

        display();
    }

    public void display(){
        String names_scores, names, scores;

        names_scores = highScores.makeString(true);
        StringTokenizer stringTokenizer = new StringTokenizer(names_scores, highScores.delimiter);

        names = stringTokenizer.nextToken();
        scores = stringTokenizer.nextToken();
        int numberOfPlayers = Math.min(highScores.numberOfPlayers, highScores.MAX_SHOWN);

        namesTextView.setText(names);
        scoresTextView.setText(scores);

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < numberOfPlayers; i++){

            stringBuilder.append(i + 1).append(". \n");
        }

        rankingTextView.setText(stringBuilder.toString());
    }
}


