package cindy.ghost;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;

public class Dictionary implements Serializable {

    public Node firstNode;

    public Dictionary(Context ctx, String country_code){

        InputStream rawList;
        switch(country_code){
            case "en":
                rawList = ctx.getResources().openRawResource(R.raw.englishwords);
                break;
            case "nl":
                rawList = ctx.getResources().openRawResource(R.raw.dutchwords);
                break;
            default:
                return;
        }

        firstNode = new Node(' ');

        BufferedReader reader = new BufferedReader(new InputStreamReader(rawList));
        String line;

        do {
            try {
                line = reader.readLine();
            }catch (IOException iox){
                line = null;
            }

            if (line != null && isValidWord(line)){
                addToDictionary(line.toLowerCase());
            }
        }while (line != null);
    }

    private Boolean isValidWord(String word){

        int length = word.length();
        Boolean checksForContainingFragment = false;
        Node current_node = firstNode;

        if (length > 4){
            checksForContainingFragment = true;
        }

        Character myChar;
        word = word.toLowerCase();

        for (int i = 0; i < length; i++){
            myChar = word.charAt(i);

            if (myChar < 'a' || myChar > 'z'){
                return false;
            }

            if (checksForContainingFragment){
                current_node = current_node.childAt(myChar);
                if (current_node == null){
                    checksForContainingFragment = false;
                }
                else{
                    if (i > 3 && current_node.endOfWord){
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private void addToDictionary(String word){
        Node current_node = firstNode;

        int length = word.length();
        Character myChar;

        for (int i = 0; i < length; i++){

            myChar = word.charAt(i);

            if (!current_node.isChild(myChar)) {

                current_node.add_child(myChar);
            }

            current_node = current_node.childAt(myChar);
        }

        current_node.endOfWord = true;
    }

    public Boolean wordNotPossible(Node current_node, Character new_letter){

        /* return true if the new letter is not a child node */
        return (!current_node.isChild(new_letter));
    }

    public Boolean formsWord (Node current_node, Character new_letter){

        /* return true if the child node (new_letter) is the end of the word */
        Node nextNode = current_node.childAt(new_letter);
        return nextNode.endOfWord;
    }

    public Boolean makesNextLetterImpossible(Node current_node, Character new_letter){

        /* return true if this letter makes it impossible to become a word of >3 letters */
        return (current_node.childAt(new_letter).endOfTrie);
    }
}
