package cindy.ghost;

import android.content.SharedPreferences;
import android.util.Log;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class HighScores implements Serializable{

    private Map<String, Integer> nameToScore;
    public List<String> ranking;
    public int numberOfPlayers;
    public int oldRankingPlace, newRankingPlace;
    private String delimiter = "!";

    private final int MAX_SHOWN = 15;

    public HighScores(){
        nameToScore = new HashMap<String, Integer>();
        ranking = new ArrayList<String>();
        numberOfPlayers = 0;

        newRankingPlace = -1;
        oldRankingPlace = -1;
    }

    public void addPlayer(String name){
        if (nameToScore.containsKey(name)) return;

        nameToScore.put(name, 0);
        ranking.add(name);
        numberOfPlayers ++;
    }

    public void incrementScoreFor(String name){
        if (!nameToScore.containsKey(name)) return;

        int score = nameToScore.get(name);
        nameToScore.put(name, score + 1);
        updateRankingFor(name);
    }

    private void updateRankingFor(String name){

        int myRanking, myScore, otherScore, index;

        myRanking = ranking.indexOf(name);

        oldRankingPlace = myRanking;
        newRankingPlace = myRanking;

        myScore = nameToScore.get(name);

        index = oldRankingPlace;
        while (index > 0){
            index -- ;
            String otherName = ranking.get(index);
            otherScore = nameToScore.get(otherName);

            if (myScore > otherScore){
                Collections.swap(ranking, index, myRanking);
                myRanking = index;
            }
            else{
                break;
            }
        }

        newRankingPlace = myRanking;
    }

    public Integer getScoreOf(String name){
        return nameToScore.get(name);
    }

    public String makeString(Boolean setMaximum){

        int N_string = numberOfPlayers;
        if (N_string == 0) return "";

        if (setMaximum){
            if (N_string > MAX_SHOWN) N_string = MAX_SHOWN;
        }

        StringBuilder sb_names = new StringBuilder();
        StringBuilder sb_scores = new StringBuilder();

        for (int i = 0; i < N_string; i++) {

            int ranking_nr = i + 1;
            sb_names.append(ranking_nr);
            String currentName = ranking.get(i);

            sb_names.append(". ");

            if (ranking_nr < 10) {
                sb_names.append(" ");
            }

            sb_names.append(currentName).append("\n");
            sb_scores.append(getScoreOf(currentName)).append("\n");
        }

        String nameString = sb_names.toString().trim();
        String nameScores = sb_scores.toString().trim();

        return nameString.concat(delimiter).concat(nameScores);
    }

    private void findHighScoresFromString(String names_scores){

        nameToScore.clear();
        ranking.clear();
        numberOfPlayers = 0;

        if (names_scores.equals("")) return;

        StringTokenizer stringTokenizer = new StringTokenizer(names_scores, delimiter);
        String nameString = stringTokenizer.nextToken();
        String scoresString = stringTokenizer.nextToken();

        String[] list_names = nameString.split("\n");
        String[] list_scores = scoresString.split("\n");

        int n_players = list_names.length;
        if (n_players != list_scores.length){
            Log.e("Class HighScores", "lengths differ in strings for names and scores");
            return;
        }

        for (int i = 0; i < n_players; i++){
            String name = list_names[i].substring(3).trim();
            int score = Integer.parseInt(list_scores[i]);
            nameToScore.put(name, score);
            ranking.add(name);
            numberOfPlayers++;
        }
    }

    public void saveGameState(SharedPreferences.Editor spEditor){

        String highScoresString = makeString(false);
        spEditor.putString("highScoresString", highScoresString);
        spEditor.putInt("oldRankingPlace", oldRankingPlace);
        spEditor.putInt("newRankingPlace", newRankingPlace);
    }

    public void recallGameState(SharedPreferences sharedPreferences){

        oldRankingPlace = sharedPreferences.getInt("oldRankingPlace", oldRankingPlace);
        newRankingPlace = sharedPreferences.getInt("newRankingPlace", newRankingPlace);

        String highScoresString = sharedPreferences.getString("highScoresString","");
        findHighScoresFromString(highScoresString);
    }

    public void display(TextView namesTextView, TextView scoresTextView){

        String names_scores, names, scores;

        names_scores = makeString(true);
        StringTokenizer stringTokenizer = new StringTokenizer(names_scores, delimiter);
        names = stringTokenizer.nextToken();
        scores = stringTokenizer.nextToken();

        namesTextView.setText(names);
        scoresTextView.setText(scores);
    }
}
