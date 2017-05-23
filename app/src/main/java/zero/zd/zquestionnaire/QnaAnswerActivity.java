package zero.zd.zquestionnaire;

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

import zero.zd.zquestionnaire.model.QnA;

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

    private ArrayList<QnA> mQnaList;
    private ArrayList<QnA> mMistakeQnaList;
    private ArrayList<RadioButton> mRadioList;
    private String[] mRandomAnswers;
    private int mQnaIndex;
    private int mAnswerLocationIndex;
    private int mCorrect;
    private int mMistake;
    private boolean mIsSoundEnabled;
    private boolean mIsInitialized;
    private boolean mIsBackPressed;

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
        mIsSoundEnabled = mPreferences.getBoolean(KEY_SOUND_ENABLED, true);

        mQnaList = new ArrayList<>();
        mMistakeQnaList = new ArrayList<>();

        // check if mistake is loaded
        boolean isMistakesLoaded = getIntent()
                .getBooleanExtra(EXTRA_IS_MISTAKE_LOADED, false);
        if (isMistakesLoaded)  loadMistakes();

        // set qna list
        mQnaList = QnaAnswerState.getInstance().getQnaList(!isMistakesLoaded);

        // retrieve saved instances
        if (savedInstanceState != null) {
            updateInstances(savedInstanceState);

            updateTextProgress();
            updateQuestionText();
            updateRadioText();
            Log.d(TAG, "Activity recreated.");
        } else {
            mQnaIndex = 0;
            Collections.shuffle(mQnaList);

            initQnA();
            Log.d(TAG, "Activity initialized.");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        QnaAnswerState.getInstance().setQnaList(mQnaList);
        QnaAnswerState.getInstance().setMistakeQnaList(mMistakeQnaList);
        QnaAnswerState.getInstance().setRandomAnswers(mRandomAnswers);

        outState.putInt(SAVED_QNA_INDEX, mQnaIndex);
        outState.putInt(SAVED_ANSWER_LOCATION_INDEX, mAnswerLocationIndex);
        outState.putInt(SAVED_CORRECT_ANSWER, mCorrect);
        outState.putInt(SAVED_MISTAKE_ANSWER, mMistake);
        outState.putBoolean(SAVED_IS_INITIALIZED, mIsInitialized);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_qna_answer, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem soundItem = menu.findItem(R.id.action_sound);
        soundItem.setChecked(mIsSoundEnabled);
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

            case R.id.action_quit:
                quitApplication();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mIsBackPressed) {
            super.onBackPressed();
            return;
        }

        mIsBackPressed = true;
        Toast.makeText(this, R.string.msg_confirm_exit, Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mIsBackPressed = false;
            }
        }, DELAY_BACK_EXIT);
    }

    public void onClickOk(View view) {

        // get radio location
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        int selectedIndexRadioButton = radioGroup.indexOfChild(radioGroup
                .findViewById(radioGroup.getCheckedRadioButtonId()));

        if (selectedIndexRadioButton != mAnswerLocationIndex) {
            // add QnA to mistake list
            mMistakeQnaList.add(mQnaList.get(mQnaIndex));

            showMistakeDialog();
            mMistake++;

            if (mIsSoundEnabled)
                MediaPlayer.create(this, R.raw.mistake).start();
        } else {
            Snackbar.make(view, R.string.msg_correct, Snackbar.LENGTH_SHORT).show();
            mCorrect++;
            updateQna();

            if (mIsSoundEnabled)
                MediaPlayer.create(this, R.raw.correct).start();
        }

        animateUI();
    }

    private void loadMistakes() {
        mQnaList = new ArrayList<>(QnaAnswerState.getInstance().getMistakeQnaList());
        QnaAnswerState.getInstance().setQnaList(mQnaList);
        Log.d(TAG, "mQnaList Size: " + mQnaList.size());
        mMistakeQnaList.clear();
        Log.d(TAG, "Clean mistakeList");
        Log.d(TAG, "mQnaList Size: " + mQnaList.size());
        Log.d(TAG, "Mistakes Loaded!");
    }

    private void updateInstances(Bundle savedInstanceState) {
        mMistakeQnaList = new ArrayList<>();
        mQnaList = new ArrayList<>();
        mMistakeQnaList = QnaAnswerState.getInstance().getMistakeQnaList();
        mQnaList = QnaAnswerState.getInstance().getQnaList(false);
        mRandomAnswers = QnaAnswerState.getInstance().getRandomAnswers();

        mQnaIndex = savedInstanceState.getInt(SAVED_QNA_INDEX);
        mAnswerLocationIndex = savedInstanceState.getInt(SAVED_ANSWER_LOCATION_INDEX);
        mCorrect = savedInstanceState.getInt(SAVED_CORRECT_ANSWER);
        mMistake = savedInstanceState.getInt(SAVED_MISTAKE_ANSWER);
        mIsInitialized = savedInstanceState.getBoolean(SAVED_IS_INITIALIZED);
    }

    private void initQnA() {
        updateQuestionText();

        mRandomAnswers = mQnaList.get(mQnaIndex).getRandomAnswers();
        if (mRandomAnswers.length == 0)
            mRandomAnswers = generateRandomAnswers();

        Random random = new Random();
        mAnswerLocationIndex = random.nextInt(4);
        updateRadioText();

        updateTextProgress();
        mRadioGroup.clearCheck();
        mIsInitialized = true;
    }

    private void initViewObjects() {
        mOkButton = (Button) findViewById(R.id.btn_ok);
        mTextQuestion = (TextView) findViewById(R.id.text_question);

        mRadioGroup = (RadioGroup) findViewById(R.id.radio_group);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (mIsInitialized) mOkButton.setEnabled(true);
            }
        });

        RadioButton btnOne = (RadioButton) findViewById(R.id.radio_one);
        RadioButton btnTwo = (RadioButton) findViewById(R.id.radio_two);
        RadioButton btnThree = (RadioButton) findViewById(R.id.radio_three);
        RadioButton btnFour = (RadioButton) findViewById(R.id.radio_four);

        mRadioList = new ArrayList<>();
        mRadioList.add(btnOne);
        mRadioList.add(btnTwo);
        mRadioList.add(btnThree);
        mRadioList.add(btnFour);
    }

    private String[] generateRandomAnswers() {
        String[] answerArray = new String[3];

        ArrayList<QnA> originalList = QnaAnswerState.getInstance().getQnaList(true);
        String answer = mQnaList.get(mQnaIndex).getAnswer();

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
        mRadioList.get(mAnswerLocationIndex).setText(mQnaList.get(mQnaIndex).getAnswer());
        int randIndex = 0;
        for (int i = 0; i < 4; i++) {
            if (i == mAnswerLocationIndex)
                continue;
            mRadioList.get(i).setText(mRandomAnswers[randIndex]);
            randIndex++;
        }
    }

    private void resetQnA() {
        mQnaIndex = 0;
        mCorrect = 0;
        mMistake = 0;

        mQnaList = QnaAnswerState.getInstance().getQnaList(true);
        Collections.shuffle(mQnaList);
        initQnA();

        Snackbar.make(getWindow().getDecorView().getRootView(),
                R.string.msg_reset, Snackbar.LENGTH_SHORT).show();

        animateUI();
    }

    private void updateQna() {
        mQnaIndex++;
        if (mQnaIndex == mQnaList.size()) {
            showResultActivity();
            return;
        }

        initQnA();
        mOkButton.setEnabled(false);
    }

    private void updateQuestionText() {
        mTextQuestion.setText(mQnaList.get(mQnaIndex).getQuestion());
    }

    private void updateTextProgress() {
        TextView txtProgress = (TextView) findViewById(R.id.text_progress);
        txtProgress.setText(String.format(getResources().getString(R.string.msg_progress),
                mQnaIndex + 1, mQnaList.size(), mCorrect, mMistake));
    }

    private void showMistakeDialog() {
        String msg = "Correct Answer: \n" + mQnaList.get(mQnaIndex).getAnswer();
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
        QnaAnswerState.getInstance().setQnaList(mQnaList);
        QnaAnswerState.getInstance().setMistakeQnaList(mMistakeQnaList);

        // get passing
        String assessment = "Failed!";
        if (mCorrect >= getPassingScore() && mCorrect != 0)
            assessment = "Passed!";

        startActivity(QnaResultActivity
                .getStartIntent(this, assessment, mCorrect));
        finish();
    }

    private int getPassingScore() {
        int passing;
        int total = mQnaList.size();
        if (total % 2 == 0)
            passing = total / 2;
        else
            passing = (total / 2) + 1;

        return passing;
    }

    private void toggleSound(MenuItem soundItem) {
        mIsSoundEnabled = !mIsSoundEnabled;
        soundItem.setChecked(mIsSoundEnabled);

        SharedPreferences.Editor prefsEditor = mPreferences.edit();
        prefsEditor.putBoolean(KEY_SOUND_ENABLED, mIsSoundEnabled);
        prefsEditor.apply();
    }

    private void animateUI() {
        Animation questionAnimation = AnimationUtils
                .loadAnimation(this, R.anim.fade_in);

        final int inBetweenDelay = 70;
        for (int i = 0; i < mRadioList.size(); i++) {
            final RadioButton radioButton = mRadioList.get(i);

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

    private void quitApplication() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
