package cindy.tictactoe;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends ActionBarActivity {

    public Button restart_button, menu_button, one_player_button, two_player_button;
    public TextView advice_text;
    Boolean player1_X, game, two_player, timer_on;
    int player1start;

    public List<Integer> buttons_for_1, buttons_for_2, winning_for_1, winning_for_2,
            computer_options;
    public List<Button> used_buttons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        restart_button = (Button) findViewById(R.id.restart_button);
        menu_button = (Button) findViewById(R.id.to_menu);
        one_player_button = (Button) findViewById(R.id.one_player_button);
        two_player_button = (Button) findViewById(R.id.two_player_button);

        advice_text = (TextView) findViewById(R.id.advice_text);

        game = false;
        timer_on = false;
        player1start = 0;

        advice_text.setText(R.string.welcome);
        used_buttons = new ArrayList<>();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void player_chosen (View view){
        if (game){return;}

        one_player_button.setVisibility(View.INVISIBLE);
        two_player_button.setVisibility(View.INVISIBLE);
        menu_button.setVisibility(View.VISIBLE);

        Button current_button = (Button) view;

        two_player = (current_button == two_player_button);

        restart_button.setVisibility(View.VISIBLE);
        initialize_board();
    }

    public void restart_button_clicked(View view){
        if (timer_on) return;

        initialize_board();
    }

    public void go_to_menu(View view){
        if (timer_on) return;

        initialize_board();

        for (Button button : used_buttons){
            button.setText(" ");
            button.setTextColor(0xFF000000);
        }

        restart_button.setVisibility(View.INVISIBLE);
        game = false;
        menu_button.setVisibility(View.INVISIBLE);

        advice_text.setText(R.string.welcome);

        one_player_button.setVisibility(View.VISIBLE);
        two_player_button.setVisibility(View.VISIBLE);
    }

    public void initialize_board(){

        player1start = 1 - player1start;
        player1_X = (player1start == 1);
        game = true;
        timer_on = false;

        buttons_for_1 = new ArrayList<>();
        buttons_for_2 = new ArrayList<>();
        winning_for_1 = new ArrayList<>();
        winning_for_2 = new ArrayList<>();
        computer_options = new ArrayList<>(Arrays.asList(11, 12, 13, 21, 22, 23, 31, 32, 33));

        for (Button button : used_buttons){
            button.setText(" ");
            button.setTextColor(0xFF000000);
        }

        used_buttons = new ArrayList<>();

        advice_text.setText(R.string.player1turn);
        if (! player1_X){
            advice_text.setText(R.string.player2turn);
        }

        if (!two_player) {
            if (!player1_X) {
                computer_turn();
            }
            advice_text.setText(R.string.your_turn);
        }

    }

    public void button_pressed(View view) {
        if (!game || timer_on) return;

        int i = view.getTag().toString().charAt(0) - '0';
        int j = view.getTag().toString().charAt(1) - '0';

        Button current_button = (Button) view;
        make_a_move(current_button, 10 * i + j);
    }

    public void make_a_move(Button current_button, int button_nr){

        String text_on_button = current_button.getText().toString();

        if (text_on_button.length() > 0) {
            if (text_on_button.charAt(0) != ' '){
                return;
            }
        }

        used_buttons.add(current_button);
        computer_options.remove(Integer.valueOf(button_nr));

        if (player1_X) {
            current_button.setText(R.string.set_x);

            player1_X = false;
            buttons_for_1.add(button_nr);
            if (check_for_win(1)) {
                game = false;
                return;
            }
        }
        else  {
            current_button.setText(R.string.set_o);

            player1_X = true;
            buttons_for_2.add(button_nr);
            if (check_for_win(2)) {
                game = false;
                return;
            }
        }

        if(used_buttons.size() == 9){
            game = false;
            advice_text.setText(R.string.its_a_tie);
            return;
        }

        if (two_player) {

            if (player1_X) {
                advice_text.setText(R.string.player1turn);
                return;
            }
            advice_text.setText(R.string.player2turn);
        }
        else if (!player1_X && game) {

            advice_text.setText(R.string.comp_turn);
            new CountDownTimer(500, 100)
            {
                @Override
                public void onFinish()
                {
                    computer_turn();
                    if (game) advice_text.setText(R.string.your_turn);
                    timer_on = false;
                }

                @Override
                public void onTick(long millisUntilFinished){
                    timer_on = true;
                }
            }.start();
        }
    }

    public void computer_turn(){
        int button_nr = find_button_for_computer();

        String name_button = "button".concat(Integer.toString(button_nr));
        int resID = getResources().getIdentifier(name_button, "id", getPackageName());
        Button button_chosen = (Button) findViewById(resID);

        make_a_move(button_chosen, button_nr);
    }

    public int find_button_for_computer(){

        for (int nr : winning_for_2){
            if (computer_options.indexOf(nr) >= 0){
                return nr;
            }
        }

        for (int nr : winning_for_1){
            if (computer_options.indexOf(nr) >= 0){
                return nr;
            }
        }

        int random_index = (int) (Math.random() * computer_options.size());
        return computer_options.get(random_index);
    }

    public boolean check_for_win(int player_nr){

        List buttons_list;

        if (player_nr == 1) {
            buttons_list = buttons_for_1;
        }
        else{
            buttons_list = buttons_for_2;
        }
        int num_button = buttons_list.size();
        int buttonA, buttonB, needed;

        if (num_button < 2){
            return false;
        }

        for (int i = 0 ; i < num_button - 1; i ++){
            for (int j = i + 1; j < num_button; j++){

                buttonA = (int) buttons_list.get(i);
                buttonB = (int) buttons_list.get(j);
                needed = in_line_with(buttonA, buttonB);
                if (needed != -1){
                    if (player_nr == 1) {
                        winning_for_1.add(needed);
                    }
                    else{
                        winning_for_2.add(needed);
                    }
                }

                if (buttons_list.indexOf(needed) >= 0){
                    display_win(buttonA, buttonB, needed, player_nr);
                    return true;
                }
            }
        }
        return false;
    }

    public void display_win(int a, int b, int c, int player_nr){

        List<Integer> winning_list = new ArrayList<>(Arrays.asList(a,b,c));

        for (int nr : winning_list) {
            String name_button = "button".concat(Integer.toString(nr));
            int resID = getResources().getIdentifier(name_button, "id", getPackageName());
            Button current_button = (Button) findViewById(resID);
            current_button.setTextColor(0xFFFF0000);
        }

        if (player_nr == 1) {
            advice_text.setText(R.string.player1win);
            if (!two_player) advice_text.setText(R.string.you_win);
        }
        else {
            advice_text.setText(R.string.player2win);
            if (!two_player) advice_text.setText(R.string.you_lose);
        }
    }

    public int in_line_with(int a, int b){
        int rowA, rowB, colA, colB, missing;

        rowA = a / 10;
        rowB = b / 10;

        colA = a % 10;
        colB = b % 10;

        if (rowA == rowB){
            missing = 6 - colA - colB;
            return (rowA * 10 + missing);
        }
        else if(colA == colB){
            missing = 6 - rowA - rowB;
            return (missing * 10 + colA);
        }
        else if ((rowA == colA) && (rowB == colB)){
            missing = 6 - rowA - rowB;
            return (missing * 10 + missing);
        }
        else if((rowA + colA == rowB + colB) && (rowA + colA == 4)){
            missing = 6 - rowA - rowB;
            return (missing * 10 + (4 - missing));
        }
        else{
            return -1;
        }
    }
}
