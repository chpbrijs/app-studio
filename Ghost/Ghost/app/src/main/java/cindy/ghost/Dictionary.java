package cindy.ghost;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.StringTokenizer;

public class Dictionary implements Serializable {

    public HashSet<String> all_words, all_fragments;
    public final String language;

    public Dictionary(String country_code, Context ctx){

        all_words = new HashSet<String>();
        all_fragments = new HashSet<String>();
        language = country_code;

        makeDictionary(ctx);
    }

    public void makeDictionary(Context ctx){

        InputStream rawList;
        switch(language){
            case "en":
                rawList = ctx.getResources().openRawResource(R.raw.englishwords);
                break;
            case "nl":
                rawList = ctx.getResources().openRawResource(R.raw.dutchwords);
                break;
            default:
                return;
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(rawList));

        String line;

        do {
            try {
                line = reader.readLine();
            }catch (IOException iox) {
                line = null;
            }

            if (line != null && isValidWord(line)){
                addToDictionary(line.toLowerCase());
            }

        }while (line != null);
    }

    private Boolean isValidWord(String word){

        int length = word.length();

        Character current_char;

        for (int i = 0; i < length; i++){
            current_char = word.charAt(i);

            if (current_char < 'a' || current_char > 'z'){
                return false;
            }
        }

        return true;
    }

    private void addToDictionary(String word){

        int word_length = word.length();

        for (int i = 1; i < word_length - 1; i++){

            String fragment = word.substring(0, i);

            if (i > 3 && all_words.contains(fragment)){
                return;
            }

            if (!all_fragments.contains(fragment)) {
                all_fragments.add(fragment);
            }
        }

        if (word_length > 3){
            all_words.add(word);
        }
    }

    public Boolean wordNotPossible(String fragment){

        return (!all_fragments.contains(fragment));
    }

    public Boolean formsWord (String word){
        return all_words.contains(word);
    }

}
