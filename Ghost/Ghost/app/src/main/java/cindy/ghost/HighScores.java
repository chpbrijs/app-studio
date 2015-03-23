package cindy.ghost;

import android.content.SharedPreferences;

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
    final public String delimiter = "!";

    public final int MAX_SHOWN = 15;

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

        if (numberOfPlayers == 0) return "";

        int N_string = numberOfPlayers;
        if (setMaximum){
            if (N_string > MAX_SHOWN) N_string = MAX_SHOWN;
        }

        StringBuilder sb_names = new StringBuilder();
        StringBuilder sb_scores = new StringBuilder();

        for (int i = 0; i < N_string; i++) {

            String currentName = ranking.get(i);

            sb_names.append(currentName).append("\n");
            sb_scores.append(getScoreOf(currentName)).append("\n");
        }

        String nameString = sb_names.toString().trim();
        String scoreString = sb_scores.toString().trim();

        return nameString.concat(delimiter).concat(scoreString);
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
            return;
        }

        for (int i = 0; i < n_players; i++){
            String name = list_names[i].trim();
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
}
