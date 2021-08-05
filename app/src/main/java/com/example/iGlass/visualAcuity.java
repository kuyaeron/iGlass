package com.example.iGlass;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import com.example.iGlass.data.VisualAcuityResult;


public class visualAcuity extends AppCompatActivity {
    public static final char[] candidates = {'C', 'D', 'H', 'K', 'N', 'O', 'R', 'S', 'V', 'Z'};
    public static final float[] fontSizes = {90, 75, 64, 54, 46, 40, 35, 33, 29, 26, 24};
    public static final String [] logmarDisp = {"1.0","0.9","0.8","0.7","0.6","0.5","0.4","0.3","0.2","0.1","0.0"};
    public static final float[] fontSizes1 = {90, 75, 64, 54, 46, 40, 35, 33, 29, 26, 24};
    public static final String [] logmarDisp1 = {"1.0","0.9","0.8","0.7","0.6","0.5","0.4","0.3","0.2","0.1","0.0"};
    public static final int MAX_RETRY_COUNT = 3;
    private final int SPEECH_RECOGNITION_CODE = 1;
    Button voice_button;
    TextView iv_letter, logmarDisplay, result_text, ins;
    List<VisualItem> list;
    Random r;
    int row=0, triesInRow=0;
    double lettersCompletedInRow=0, wrongLettersInRow=0;
    double logmarScoreLeft = 0.0;
    double logmarScoreRight = 0.0;
    //int leftEye =1;
    int retryCount=0, candidateIndex=0;
    double rowCompleted=1.1, correctLettersIncomplete=0.0;
    Boolean isFinished = false, rightDone = false;
    String[] scoreDisplay;
    String alts[] = {"each", "hate", "age", "siri"};
    String alt[] = {"h", "h", "h", "c"};

    //private static final String LETTERS_SEARCH = "letters";

    /* Keyword we are looking for to activate menu */
//    private static final String KEYPHRASE = "oh mighty computer";

    /* Used to handle permission request */
    //private static final int PERMISSIONS_REQUEST_RECORD_AUDIO = 1;

    SpeechRecognizer recognizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visual_acuity);

        r = new Random();

        iv_letter = (TextView) findViewById(R.id.iv_letter);
        logmarDisplay = (TextView) findViewById(R.id.logmar_display);
        result_text = findViewById(R.id.result_text1);
        ins = findViewById(R.id.instruction);
        voice_button = findViewById(R.id.voice_button);

        list = new ArrayList<>();


        // Check if user has given permission to record audio
        /*int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSIONS_REQUEST_RECORD_AUDIO);
            return;
        }*/

        //iv_letter.setEnabled(true);
        //add all the plates and names to the list
        for(int i = 0; i < new VisualDB().answers.length; i++) {
            list.add(new VisualItem(new VisualDB().answers[i], new VisualDB().logmar[i]));
        }

        //shuffle the letters
        Collections.shuffle(list);
        //newQuestion(turn);//

        candidateIndex = r.nextInt(candidates.length);
        iv_letter.setText(Character.toString(candidates[candidateIndex]));
        iv_letter.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSizes[row]);
        logmarDisplay.setText(logmarDisp[row]);
        ins.setText("Please cover your LEFT EYE\n\nThe number below the letters is the row number. It changes after you complete 5 letters in a row.");

        voice_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSpeechToText();
            }
        });
    }

    /*public void popup(View view){
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_va_layout, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(relativeLayout, Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }*/

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

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String text = result.get(0);

                    for (int i = 0; i < alts.length; i++){
                        if (text.equals(alts[i])){
                            text = alt[i];
                            break;
                        }
                    }
                    char letter = Character.toUpperCase(text.charAt(0));
                    if (letter == candidates[candidateIndex]) {
                        // success
                        result_text.setText("Correct! You said: " + text);
                        lettersCompletedInRow++;
                        if (wrongLettersInRow != 0 && lettersCompletedInRow < 5){
                            correctLettersIncomplete = lettersCompletedInRow - wrongLettersInRow;
                            nextLetterInRow();
                        }
                        else if (lettersCompletedInRow < 5) nextLetterInRow();
                        else if (lettersCompletedInRow >= 5 && wrongLettersInRow!=0){
                            correctLettersIncomplete = lettersCompletedInRow - wrongLettersInRow;
                            computeScore();
                        }
                        else nextRow();
                    }
                    else {
                        // failed
                        result_text.setText("Wrong! You said: " + text);
                        lettersCompletedInRow++;
                        wrongLettersInRow++;
                        //triesInRow++;
                        if (lettersCompletedInRow < 5) retry();
                        else computeScore();
                    }
                } else {
                    Toast.makeText(this, "There is no speech detected, please try to say something again.", Toast.LENGTH_SHORT).show();
                }
            }
        //}
        /*catch (NoSpeechResultException e) {
            e.fix(getApplicationContext());
        }*/
    }

    public void nextLetterInRow(){
        candidateIndex = r.nextInt(candidates.length);
        iv_letter.setText(Character.toString(candidates[candidateIndex]));
    }

    public void nextRow(){
        rowCompleted=rowCompleted-0.1;
        row++;
        lettersCompletedInRow = 0;
        if (row < fontSizes.length-1){
            candidateIndex = r.nextInt(candidates.length);
            iv_letter.setText(Character.toString(candidates[candidateIndex]));
            iv_letter.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSizes[row]);
            logmarDisplay.setText(logmarDisp[row]);
        }
        else computeScore();
    }

    public void retry(){
        retryCount = retryCount + 1;
        if (retryCount >= MAX_RETRY_COUNT && lettersCompletedInRow < 5) computeScore();
        else nextLetterInRow();
    }

    public void computeScore(){
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        if (!rightDone){
            logmarScoreRight = rowCompleted - (0.02 * correctLettersIncomplete);
            scoreDisplay = add_element(0, scoreDisplay, decimalFormat.format(logmarScoreRight));
            nextEye();
        }

        else if (rightDone == true){
            logmarScoreLeft = rowCompleted - (0.02 * correctLettersIncomplete);
            scoreDisplay = add_element(1, scoreDisplay, decimalFormat.format(logmarScoreLeft));
            addToDB(isFinished = true, logmarScoreRight, logmarScoreLeft);
        }
    }
    public void nextEye(){
        row = 0;
        rowCompleted = 1.1;
        lettersCompletedInRow = 0.0;
        wrongLettersInRow = 0.0;
        correctLettersIncomplete = 0.0;
        retryCount = 0;
        rightDone = true;
        candidateIndex = r.nextInt(candidates.length);
        iv_letter.setText(Character.toString(candidates[candidateIndex]));
        iv_letter.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSizes[row]);
        logmarDisplay.setText(logmarDisp[row]);
        ins.setText("Please cover your RIGHT EYE\n\nThe number below the letters is the row number. It changes after you complete 5 letters in a row.");
    }

    public static String[] add_element(int n, String myarray[], String ele)
    {
        int i;

        // create a new array of size n+1
        String newarr[] = new String[n + 1];

        // insert the elements from
        // the old array into the new array
        // insert all elements till n
        // then insert x at n+1
        for (i = 0; i < n; i++)
            newarr[i] = myarray[i];

        newarr[n] = ele;

        return newarr;
    }

    public void addToDB(Boolean isFinished, double logmarScoreRight, double logmarScoreLeft){
        if(isFinished){
            iGlassDatabase db = iGlassDatabase.getDatabase(this.getApplicationContext());
            VisualAcuityResult vaResult = new VisualAcuityResult();

            vaResult.ScoreRight = logmarScoreRight;
            vaResult.ScoreLeft = logmarScoreLeft;

            Date date = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("MMMM dd, yyyy hh:mm a", Locale.getDefault());
            vaResult.DateCompleted = df.format(date);

            db.vaDao().insertAll(vaResult);
            openActivityTestResult();
        }
    }

    public void openActivityTestResult(){
        Intent intent = new Intent(this, TestResult.class);
        Bundle bundle = new Bundle();
        bundle.putStringArray("scoreDisplay", scoreDisplay);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

}