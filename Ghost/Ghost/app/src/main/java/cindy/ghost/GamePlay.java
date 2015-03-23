package cindy.ghost;

import android.content.SharedPreferences;

import java.io.Serializable;
import java.util.Random;

public class GamePlay implements Serializable{

    private Dictionary dictionary;
    private Boolean player1begins;

    public String word_fragment;

    public Boolean player1turn;
    public Boolean player1wins;

    public Boolean game;
    public Boolean wonByMakingWord;

    public GamePlay (Dictionary dict){

        dictionary = dict;

        double prob = 0.5;
        player1begins = new Random().nextFloat() > prob;

        player1wins = true;
        wonByMakingWord = true;

        reset();
    }

    public void reset(){
        player1turn = player1begins;

        word_fragment = "";
        game = true;
    }

    public void swapBeginPlayer(){
        player1begins = !player1begins;
    }

    public void makeMove(Character letter){

        word_fragment = word_fragment.concat(letter.toString());
        checkForLost();

        if (!game){
            player1wins = !player1turn;
            return;
        }

        player1turn = !player1turn;
    }

    private void checkForLost(){

        int length_new_word = word_fragment.length();
        game = false;

        if (length_new_word > 3 && dictionary.formsWord(word_fragment)){
            wonByMakingWord = true;
        }

        else if (dictionary.wordNotPossible(word_fragment)){
            wonByMakingWord = false;
        }

        else{
            game = true;
        }
    }

    public void changeDictionary(Dictionary new_dict){
        dictionary = new_dict;
        reset();
    }

    public void saveGameState(SharedPreferences.Editor spEditor){
        spEditor.putString("word_fragment", word_fragment);
        spEditor.putBoolean("player1turn", player1turn);
        spEditor.putBoolean("player1begins", player1begins);
        spEditor.putBoolean("player1wins", player1wins);
        spEditor.putBoolean("game", game);
    }

    public void recallGameState(SharedPreferences sharedPreferences){
        word_fragment = sharedPreferences.getString("word_fragment", word_fragment);
        player1turn = sharedPreferences.getBoolean("player1turn", player1turn);
        player1begins = sharedPreferences.getBoolean("player1begins", player1begins);
        player1wins = sharedPreferences.getBoolean("player1wins", player1wins);
        game = sharedPreferences.getBoolean("game", false);
    }
}
