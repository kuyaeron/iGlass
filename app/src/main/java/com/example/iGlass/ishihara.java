package com.example.iGlass;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import com.example.iGlass.data.*;


//https://www.youtube.com/watch?v=DbkBB3jga_g&t=662s

public class ishihara extends AppCompatActivity {

    Button b_answer1,  b_answer2,  b_answer3, voice;
    TextView test, ins_cb;
    private final int SPEECH_RECOGNITION_CODE = 1;
    ImageView cb_plate;
    List<PlatesItem> list;
    List<PlatesItem2>list2;
    Random r;
    int turn =0, turn2 = 0;
    int mistakes, protanopia, deuteranopia;
    Boolean finalFour = false, isFinished = false, firstPlateDone=false;
    String  finding;
    Integer plate1 = R.drawable.p1;
    String plate1ans = "12";
    String alts[] = {"one", "two", "three", "tree", "four", "five", "six", "seven", "ate", "it", "nine", "ten"};
    String alt[] = {"1", "2", "3", "3", "4", "5", "6", "7", "8", "8", "9", "10"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ishihara);

        r = new Random();

        cb_plate = findViewById(R.id.iv_flag);
        test = findViewById(R.id.testing);
        ins_cb = findViewById(R.id.ins_cb);

        voice = findViewById(R.id.voice_button2);

        list = new ArrayList<>();
        list2 = new ArrayList<>();

        //add all the plates and names to the list
        for(int i = 0; i < new PlatesDatabase().answers.length; i++)
            list.add(new PlatesItem(new PlatesDatabase().answers[i], new PlatesDatabase().plates[i]));

        for(int i = 0; i < new PlatesDatabase2().answers2.length; i++)
            list2.add(new PlatesItem2(new PlatesDatabase2().answers2[i], new PlatesDatabase2().plates2[i],
                    new PlatesDatabase2().protanopia[i], new PlatesDatabase2().deuteranopia[i]));

        //shuffle the plates
        Collections.shuffle(list);
        Collections.shuffle(list2);

        //newQuestion(turn);

        cb_plate.setImageResource(plate1);
        ins_cb.setText("If you cannot see what is shown, please say 'NOTHING'.");

        voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSpeechToText();
            }
        });
    }

    public void startSpeechToText(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Speak something...");
        try {
            startActivityForResult(intent, SPEECH_RECOGNITION_CODE);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! Speech recognition is not supported in this device.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Callback for speech recognition activity
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //try {
        if (requestCode == SPEECH_RECOGNITION_CODE) {
            if (resultCode == RESULT_OK && null != data) {

                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String text = result.get(0);

                for (int i = 0; i < alts.length; i++){
                    if (text.equals(alts[i])){
                        text = alt[i];
                        break;
                    }
                }

                if (!firstPlateDone){ //first plate
                    if (text.equals(plate1ans)) {
                        firstPlateDone = true;
                        test.setText("Correct! You said "+ text);
                        nextPlate(); //pass
                    }
                    else{
                        Toast.makeText(this, "You are unable to proceed to the test because you failed the first item.",
                                Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
                else if (!finalFour){ // plates 2-21
                    if (text.equals(list.get(turn).getName())){
                        test.setText("Correct! You said "+ text);
                        // success
                        turn++;
                        if (turn < list.size()){
                            nextPlate();
                        }
                        else{
                            finalFour = true;
                            nextPlate2();
                        }
                    }
                    else if (!text.equals(list.get(turn).getName())){
                        test.setText("Wrong! You said "+ text);
                        turn++;
                        mistakes++;
                        if (turn < list.size()){
                            nextPlate();
                        }
                        else{
                            finalFour = true;
                            nextPlate2();
                        }
                    }
                }
                else if (finalFour){ //final 4 plates
                    if (text.equals(list2.get(turn2).getName())){
                        test.setText("Correct! You said "+ text);
                        turn2++;
                        if (turn2 < list2.size()) nextPlate2();
                        else addToDB(isFinished=true);
                    }
                    else if (!text.equals(list2.get(turn2).getName())){
                        if (text.equals(list2.get(turn2).getProtanopia())){
                            test.setText("You said "+ text);
                            turn2++;
                            protanopia++;
                            if (turn2 < list2.size()) nextPlate2();
                            else addToDB(isFinished=true);
                        }
                        else if (text.equals(list2.get(turn2).getDeuteranopia()) && turn2 < list2.size()){
                            test.setText("You said "+ text);
                            turn2++;
                            deuteranopia++;
                            if (turn2 < list2.size()) nextPlate2();
                            else addToDB(isFinished=true);
                        }
                        else {
                            test.setText("Wrong! You said "+ text);
                            mistakes++;
                            nextPlate2();
                        }
                    }
                }
            }
            else {
                Toast.makeText(this, "There is no speech detected, please try to say something again.", Toast.LENGTH_SHORT).show();
            }
        }
        //}
        /*catch (NoSpeechResultException e) {
            e.fix(getApplicationContext());
        }*/
    }

    private void nextPlate(){
        cb_plate.setImageResource(list.get(turn).getImage());
    }

    private void nextPlate2(){
        ins_cb.setText("Please say the number that is clearer to you.");
        cb_plate.setImageResource(list2.get(turn2).getImage());
    }

    public void addToDB(Boolean isFinished){
        if(isFinished){
            iGlassDatabase db = iGlassDatabase.getDatabase(this.getApplicationContext());
            ColorBlindnessResult cbResult = new ColorBlindnessResult();

            if (mistakes >= 8) {
                if (protanopia > deuteranopia)
                    finding = "Red-green color blind with protanopia";
                else if (deuteranopia > protanopia)
                    finding = "Red-green color blind with deuteranopia";
            }
            else finding = "Not color blind";

            cbResult.Finding = finding;

            Date date = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("MMMM dd, yyyy hh:mm a", Locale.getDefault());
            cbResult.DateCompleted = df.format(date);

            db.colorBlindnessResultDAO().insertAll(cbResult);
            openActivityTestResult();
        }
    }

    public void openActivityTestResult(){
        Intent intent = new Intent(this, TestResult2.class);
        Bundle bundle = new Bundle();
        bundle.putString("finding", finding);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }


}
