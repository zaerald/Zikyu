package zero.zd.zikyu;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import zero.zd.zikyu.model.QnA;

public class QnaAnswerActivity extends AppCompatActivity {

    private static final String TAG = QnaAnswerActivity.class.getSimpleName();

    private static final String EXTRA_IS_MISTAKE_LOADED = "EXTRA_IS_MISTAKE_LOADED";

    private static final String SAVED_QNA_INDEX = "SAVED_QNA_INDEX";
    private static final String SAVED_ANSWER_LOCATION_INDEX = "SAVED_ANSWER_LOCATION_INDEX";
    private static final String SAVED_CORRECT_ANSWER = "SAVED_CORRECT_ANSWER";
    private static final String SAVED_MISTAKE_ANSWER = "SAVED_MISTAKE_ANSWER";
    private static final String SAVED_IS_INITIALIZED = "SAVED_IS_INITIALIZED";

    private static final String KEY_SOUND_ENABLED = "KEY_SOUND_ENABLED";
    private static final String PREFS_SOUND = "PREFS_SOUND";

    final int DELAY_BACK_EXIT = 1500;

    private SharedPreferences mPreferences;

    RadioGroup mRadioGroup;
    Button mOkButton;
    TextView mTextQuestion;

    private ArrayList<QnA> qnaList;
    private ArrayList<QnA> mistakeQnaList;
    private ArrayList<RadioButton> radioList;
    private String[] randomAnswers;
    private int qnaIndex;
    private int answerLocationIndex;
    private int correct;
    private int mistake;
    private boolean isSoundEnabled;
    private boolean isInitialized;
    private boolean isBackPressed;

    public static Intent getStartIntent(Context context, boolean isMistakesLoaded) {
        Intent intent = new Intent(context, QnaAnswerActivity.class);
        intent.putExtra(EXTRA_IS_MISTAKE_LOADED, isMistakesLoaded);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qna_answer);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(QnaAnswerState.getInstance().getQnaSubject().getSubjectName());
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        initViewObjects();

        mPreferences = getSharedPreferences(PREFS_SOUND, MODE_PRIVATE);
        isSoundEnabled = mPreferences.getBoolean(KEY_SOUND_ENABLED, true);

        qnaList = new ArrayList<>();
        mistakeQnaList = new ArrayList<>();

        // check if mistake is loaded
        boolean isMistakesLoaded = getIntent()
                .getBooleanExtra(EXTRA_IS_MISTAKE_LOADED, false);
        if (isMistakesLoaded)  loadMistakes();

        // set qna list
        qnaList = QnaAnswerState.getInstance().getQnaList(!isMistakesLoaded);

        // retrieve saved instances
        if (savedInstanceState != null) {
            updateInstances(savedInstanceState);

            updateTextProgress();
            updateQuestionText();
            updateRadioText();
            Log.d(TAG, "Activity recreated.");
        } else {
            qnaIndex = 0;
            Collections.shuffle(qnaList);

            initQnA();
            Log.d(TAG, "Activity initialized.");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        QnaAnswerState.getInstance().setQnaList(qnaList);
        QnaAnswerState.getInstance().setMistakeQnaList(mistakeQnaList);
        QnaAnswerState.getInstance().setRandomAnswers(randomAnswers);

        outState.putInt(SAVED_QNA_INDEX, qnaIndex);
        outState.putInt(SAVED_ANSWER_LOCATION_INDEX, answerLocationIndex);
        outState.putInt(SAVED_CORRECT_ANSWER, correct);
        outState.putInt(SAVED_MISTAKE_ANSWER, mistake);
        outState.putBoolean(SAVED_IS_INITIALIZED, isInitialized);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_qna_answer, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem soundItem = menu.findItem(R.id.action_sound);
        soundItem.setChecked(isSoundEnabled);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                break;

            case R.id.action_reset:
                resetQnA();
                break;

            case R.id.action_sound:
                toggleSound(item);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (isBackPressed) {
            super.onBackPressed();
            return;
        }

        isBackPressed = true;
        Toast.makeText(this, R.string.msg_confirm_exit, Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isBackPressed = false;
            }
        }, DELAY_BACK_EXIT);
    }

    public void onClickOk(View view) {

        // get radio location
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        int selectedIndexRadioButton = radioGroup.indexOfChild(radioGroup
                .findViewById(radioGroup.getCheckedRadioButtonId()));

        if (selectedIndexRadioButton != answerLocationIndex) {
            // add QnA to mistake list
            mistakeQnaList.add(qnaList.get(qnaIndex));

            showMistakeDialog();
            mistake++;

            if (isSoundEnabled)
                MediaPlayer.create(this, R.raw.mistake).start();
        } else {
            Snackbar.make(view, R.string.msg_correct, Snackbar.LENGTH_SHORT).show();
            correct++;
            updateQna();

            if (isSoundEnabled)
                MediaPlayer.create(this, R.raw.correct).start();
        }

        animateUI();
    }

    private void loadMistakes() {
        qnaList = new ArrayList<>(QnaAnswerState.getInstance().getMistakeQnaList());
        QnaAnswerState.getInstance().setQnaList(qnaList);
        Log.d(TAG, "qnaList Size: " + qnaList.size());
        mistakeQnaList.clear();
        Log.d(TAG, "Clean mistakeList");
        Log.d(TAG, "qnaList Size: " + qnaList.size());
        Log.d(TAG, "Mistakes Loaded!");
    }

    private void updateInstances(Bundle savedInstanceState) {
        mistakeQnaList = new ArrayList<>();
        qnaList = new ArrayList<>();
        mistakeQnaList = QnaAnswerState.getInstance().getMistakeQnaList();
        qnaList = QnaAnswerState.getInstance().getQnaList(false);
        randomAnswers = QnaAnswerState.getInstance().getRandomAnswers();

        qnaIndex = savedInstanceState.getInt(SAVED_QNA_INDEX);
        answerLocationIndex = savedInstanceState.getInt(SAVED_ANSWER_LOCATION_INDEX);
        correct = savedInstanceState.getInt(SAVED_CORRECT_ANSWER);
        mistake = savedInstanceState.getInt(SAVED_MISTAKE_ANSWER);
        isInitialized = savedInstanceState.getBoolean(SAVED_IS_INITIALIZED);
    }

    private void initQnA() {
        updateQuestionText();

        randomAnswers = qnaList.get(qnaIndex).getRandomAnswers();
        if (randomAnswers.length == 0)
            randomAnswers = generateRandomAnswers();

        Random random = new Random();
        answerLocationIndex = random.nextInt(4);
        updateRadioText();

        updateTextProgress();
        mRadioGroup.clearCheck();
        isInitialized = true;
    }

    private void initViewObjects() {
        mOkButton = (Button) findViewById(R.id.btn_ok);
        mTextQuestion = (TextView) findViewById(R.id.text_question);

        mRadioGroup = (RadioGroup) findViewById(R.id.radio_group);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (isInitialized) mOkButton.setEnabled(true);
            }
        });

        RadioButton btnOne = (RadioButton) findViewById(R.id.radio_one);
        RadioButton btnTwo = (RadioButton) findViewById(R.id.radio_two);
        RadioButton btnThree = (RadioButton) findViewById(R.id.radio_three);
        RadioButton btnFour = (RadioButton) findViewById(R.id.radio_four);

        radioList = new ArrayList<>();
        radioList.add(btnOne);
        radioList.add(btnTwo);
        radioList.add(btnThree);
        radioList.add(btnFour);
    }

    private String[] generateRandomAnswers() {
        String[] answerArray = new String[3];

        ArrayList<QnA> originalList = QnaAnswerState.getInstance().getQnaList(true);
        String answer = qnaList.get(qnaIndex).getAnswer();

        Random random = new Random();
        for (int i = 0; i < answerArray.length; i++) {
            String randomAnswer = "";
            while (randomAnswer.equals("")
                    || randomAnswer.equalsIgnoreCase(answer)
                    || doesRandomAnswerExists(answerArray, randomAnswer)) {
                int rand = random.nextInt(originalList.size());
                randomAnswer = originalList.get(rand).getAnswer();
            }

            answerArray[i] = randomAnswer;
        }
        return answerArray;
    }

    private boolean doesRandomAnswerExists(String[] answerArray, String randomAnswer) {
        for (String answer : answerArray) {
            if (answer == null) return false;

            if (answer.equalsIgnoreCase(randomAnswer))
                return true;
        }
        return false;
    }

    private void updateRadioText() {
        radioList.get(answerLocationIndex).setText(qnaList.get(qnaIndex).getAnswer());
        int randIndex = 0;
        for (int i = 0; i < 4; i++) {
            if (i == answerLocationIndex)
                continue;
            radioList.get(i).setText(randomAnswers[randIndex]);
            randIndex++;
        }
    }

    private void resetQnA() {
        qnaIndex = 0;
        correct = 0;
        mistake = 0;

        qnaList = QnaAnswerState.getInstance().getQnaList(true);
        Collections.shuffle(qnaList);
        initQnA();

        Snackbar.make(getWindow().getDecorView().getRootView(),
                R.string.msg_reset, Snackbar.LENGTH_SHORT).show();

        animateUI();
    }

    private void updateQna() {
        qnaIndex++;
        if (qnaIndex == qnaList.size()) {
            showResultActivity();
            return;
        }

        initQnA();
        mOkButton.setEnabled(false);
    }

    private void updateQuestionText() {
        mTextQuestion.setText(qnaList.get(qnaIndex).getQuestion());
    }

    private void updateTextProgress() {
        TextView txtProgress = (TextView) findViewById(R.id.text_progress);
        txtProgress.setText(String.format(getResources().getString(R.string.msg_progress),
                qnaIndex + 1, qnaList.size(), correct, mistake));
    }

    private void showMistakeDialog() {
        String msg = "Correct Answer: \n" + qnaList.get(qnaIndex).getAnswer();
        new AlertDialog.Builder(QnaAnswerActivity.this)
                .setTitle(R.string.msg_mistake)
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateQna();
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void showResultActivity() {
        // update mistake list
        QnaAnswerState.getInstance().setQnaList(qnaList);
        QnaAnswerState.getInstance().setMistakeQnaList(mistakeQnaList);

        // get passing
        String assessment = "Failed!";
        if (correct >= getPassingScore() && correct != 0)
            assessment = "Passed!";

        startActivity(QnaResultActivity
                .getStartIntent(this, assessment, correct));
        finish();
    }

    private int getPassingScore() {
        int passing;
        int total = qnaList.size();
        if (total % 2 == 0)
            passing = total / 2;
        else
            passing = (total / 2) + 1;

        return passing;
    }

    private void toggleSound(MenuItem soundItem) {
        isSoundEnabled = !isSoundEnabled;
        soundItem.setChecked(isSoundEnabled);

        SharedPreferences.Editor prefsEditor = mPreferences.edit();
        prefsEditor.putBoolean(KEY_SOUND_ENABLED, isSoundEnabled);
        prefsEditor.apply();
    }

    private void animateUI() {
        Animation questionAnimation = AnimationUtils
                .loadAnimation(this, R.anim.fade_in);

        final int inBetweenDelay = 70;
        for (int i = 0; i < radioList.size(); i++) {
            final RadioButton radioButton = radioList.get(i);

            int delay = i * inBetweenDelay;
            mRadioGroup.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Animation radioAnimation = AnimationUtils
                            .loadAnimation(QnaAnswerActivity.this, R.anim.fade_in);
                    radioButton.startAnimation(radioAnimation);
                }
            }, delay);
        }

        mTextQuestion.startAnimation(questionAnimation);
    }
}
