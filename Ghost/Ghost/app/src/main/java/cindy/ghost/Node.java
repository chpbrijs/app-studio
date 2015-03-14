package cindy.ghost;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class Node {

    public Character letter;
    public Boolean endOfWord;
    public Boolean endOfTrie;

    private Map<Character, Node> letterToNodes;

    public Node(Character assign_letter){
        letter = assign_letter;
        letterToNodes= new HashMap<Character, Node>();
        endOfWord = false;
        endOfTrie = true;
    }

    public void add_child(Character new_letter){
        Node new_node = new Node(new_letter);
        letterToNodes.put(new_letter, new_node);
        endOfTrie = false;
    }

    public Boolean isChild(Character candidate){
        return (letterToNodes.containsKey(candidate));
    }

    public Node childAt(Character child_letter){
        if (letterToNodes.containsKey(child_letter)) {
            return letterToNodes.get(child_letter);
        }
        else{
            return null;
        }
    }
}
