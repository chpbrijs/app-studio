package cindy.ghost;

import android.util.Log;
import android.widget.MultiAutoCompleteTextView;

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
    public String delimiter;

    public HighScores(){
        nameToScore = new HashMap<String, Integer>();
        ranking = new ArrayList<String>();
        numberOfPlayers = 0;
        delimiter = "!";

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
        if (nameToScore.containsKey(name)){

            int score = nameToScore.get(name);
            nameToScore.put(name, score + 1);
            updateRankingFor(name);
        }
        else{
            Log.e("HighScores", "could not increment score: name unknown");
        }
    }

    private void updateRankingFor(String name){

        int myRanking, myScore, otherScore, index;
        myRanking = ranking.indexOf(name);

        oldRankingPlace = myRanking;
        newRankingPlace = myRanking;

        if (oldRankingPlace == 0) {
            return;
        }

        myScore = nameToScore.get(name);

        index = oldRankingPlace;
        do {
            index --;
            String otherName = ranking.get(index);
            otherScore = nameToScore.get(otherName);
            if (myScore > otherScore){
                Log.i("", "increased in ranking!");
                Collections.swap(ranking, index, myRanking);
                myRanking = index;
                newRankingPlace = myRanking;
            }
        }
        while(myScore > otherScore && index > 0);
    }

    public Integer getScoreOf(String name){
        return nameToScore.get(name);
    }

    public Boolean contains(String name){
        return nameToScore.containsKey(name);
    }

    public String makeString(Boolean setMaximum){
        int n = numberOfPlayers;
        if (n == 0) return "";

        if (setMaximum){
            if (n > 10) n = 10;
        }

        StringBuilder sb_names = new StringBuilder();
        StringBuilder sb_scores = new StringBuilder();

        for (int i = 0; i < n; i++) {
            sb_names.append(i + 1);
            String currentName = ranking.get(i);

            if (i + 1 < 10) {
                sb_names.append(".  ").append(currentName).append("\n");
            }
            else{
                sb_names.append(". ").append(currentName).append("\n");
            }

            sb_scores.append(getScoreOf(currentName)).append("\n");
        }

        String nameString = sb_names.toString().trim();
        String nameScores = sb_scores.toString().trim();

        return nameString.concat(delimiter).concat(nameScores);
    }

    public void findHighScoresFromString(String names_scores){
        if (names_scores.equals("")) return;

        StringTokenizer stringTokenizer = new StringTokenizer(names_scores, delimiter);
        String nameString = stringTokenizer.nextToken();
        String scoresString = stringTokenizer.nextToken();

        String[] list_names = nameString.split("\n");
        String[] list_scores = scoresString.split("\n");

        int n = list_names.length;
        if (n != list_scores.length){
            Log.e("Class HighScores", "lengths differ in strings for names and scores");
            return;
        }

        nameToScore.clear();
        ranking.clear();
        numberOfPlayers = 0;

        for (int i = 0; i < n; i++){
            String name = list_names[i].substring(3).trim();
            int score = Integer.parseInt(list_scores[i]);
            nameToScore.put(name, score);
            ranking.add(name);
            numberOfPlayers++;
        }
    }
}
