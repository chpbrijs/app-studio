package cindy.ghost;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.StringTokenizer;

public class RankingActivity extends Activity {

    TextView namesTextView, scoresTextView;
    HighScores highScores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        Intent intent = getIntent();
        highScores = (HighScores) intent.getSerializableExtra("highScores");

        namesTextView = (TextView) findViewById(R.id.rankingNames);
        scoresTextView = (TextView) findViewById(R.id.rankingScores);

        highScores.display(namesTextView, scoresTextView);
    }
}


