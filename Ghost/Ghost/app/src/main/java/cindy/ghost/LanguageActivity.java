package cindy.ghost;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import java.util.HashMap;
import java.util.Map;

public class LanguageActivity extends Activity {

    RadioButton dutchRadio, englishRadio;
    String result_language;
    Map<String, RadioButton> language_to_radio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);

        dutchRadio = (RadioButton) findViewById(R.id.radio_dutch);
        englishRadio = (RadioButton) findViewById(R.id.radio_english);

        language_to_radio = new HashMap<String, RadioButton>();
        initRadio(dutchRadio, "nl");
        initRadio(englishRadio, "en");

        Intent intent = getIntent();
        result_language = intent.getStringExtra("current_language");
        language_to_radio.get(result_language).toggle();
    }

    public void initRadio(final RadioButton radio, final String language){

        language_to_radio.put(language, radio);

        radio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radio.toggle();
                result_language = language;
                sendBackResult();
            }
        });
    }

    public void sendBackResult(){
        Intent intent = new Intent();
        intent.putExtra("result_language", result_language);
        setResult(RESULT_OK, intent);
        finish();
    }
}
