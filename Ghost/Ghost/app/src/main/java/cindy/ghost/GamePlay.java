package cindy.ghost;

import android.util.Log;

import java.io.Serializable;
import java.util.Random;

public class GamePlay implements Serializable{

    private Dictionary dictionary;
    private Node current_node;
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
        player1begins = !player1begins;
        player1turn = player1begins;

        word_fragment = "";
        current_node = dictionary.firstNode;
        game = true;
    }

    public void restartCurrentGame(){
        player1turn = player1begins;

        word_fragment = "";
        current_node = dictionary.firstNode;
    }

    public void makeMove(Character letter){

        checkForLost(letter);
        word_fragment = word_fragment.concat(letter.toString());

        if (!game){
            player1wins = !player1turn;
            return;
        }

        current_node = current_node.childAt(letter);
        player1turn = !player1turn;
    }

    private void checkForLost(Character letter){

        int length_new_word = word_fragment.length() + 1;
        game = false;

        if (dictionary.wordNotPossible(current_node, letter)){
            wonByMakingWord = false;
        }

        else if (length_new_word > 3 && dictionary.formsWord(current_node, letter)){
            wonByMakingWord = true;
        }

        else if (dictionary.makesNextLetterImpossible(current_node, letter)){
            wonByMakingWord = false;
        }

        else{
            game = true;
        }
    }

    public void makeGamePlay(String input_word_fragment,
                             Boolean input_player1turn, Boolean input_player1wins){

        word_fragment = input_word_fragment;
        player1turn = input_player1turn;
        player1wins = input_player1wins;
        game = true;

        int length_word_fragment = word_fragment.length();
        if (length_word_fragment == 0) return;

        String former_word = word_fragment.substring(0, length_word_fragment - 1);
        Character last_letter = word_fragment.charAt(length_word_fragment - 1);

        int n = former_word.length();

        current_node = dictionary.firstNode;
        for(int i = 0 ; i < n; i++){
            Character myChar = word_fragment.charAt(i);
            current_node = current_node.childAt(myChar);
            if (current_node == null){
                Log.e("Class GamePlay","this node does not exist");
            }
        }

        checkForLost(last_letter);
        if (game){
            current_node = current_node.childAt(last_letter);
        }
    }
}
