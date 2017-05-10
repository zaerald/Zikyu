package zero.zd.zquestionnaire;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

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
    private boolean isInitialized;

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
            actionBar.setTitle(QnaState.getInstance().getQnaSubject().getSubjectName());
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mOkButton = (Button) findViewById(R.id.btn_ok);
        mRadioGroup = (RadioGroup) findViewById(R.id.radio_group);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (isInitialized) mOkButton.setEnabled(true);
            }
        });
        initRadioGroup();
        mTextQuestion = (TextView) findViewById(R.id.text_question);

        mQnaList = new ArrayList<>();
        mMistakeQnaList = new ArrayList<>();

        // check if mistake is loaded
        boolean isMistakesLoaded = getIntent()
                .getBooleanExtra(EXTRA_IS_MISTAKE_LOADED, false);
        if (isMistakesLoaded) {
            mQnaList = new ArrayList<>(QnaState.getInstance().getMistakeQnaList());
            QnaState.getInstance().setQnaList(mQnaList);
            Log.d(TAG, "mQnaList Size: " + mQnaList.size());
            mMistakeQnaList.clear();
            Log.d(TAG, "Clean mistakeList");
            Log.d(TAG, "mQnaList Size: " + mQnaList.size());
            Log.d(TAG, "Mistakes Loaded!");
        }
        // set qna list
        mQnaList = QnaState.getInstance().getQnaList(!isMistakesLoaded);

        // retrieve saved instances
        if (savedInstanceState != null) {
            mMistakeQnaList = new ArrayList<>();
            mQnaList = new ArrayList<>();
            mMistakeQnaList = QnaState.getInstance().getMistakeQnaList();
            mQnaList = QnaState.getInstance().getQnaList(false);
            mRandomAnswers = QnaState.getInstance().getRandomAnswers();

            mQnaIndex = savedInstanceState.getInt(SAVED_QNA_INDEX);
            mAnswerLocationIndex = savedInstanceState.getInt(SAVED_ANSWER_LOCATION_INDEX);
            mCorrect = savedInstanceState.getInt(SAVED_CORRECT_ANSWER);
            mMistake = savedInstanceState.getInt(SAVED_MISTAKE_ANSWER);
            isInitialized = savedInstanceState.getBoolean(SAVED_IS_INITIALIZED);

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
        QnaState.getInstance().setQnaList(mQnaList);
        QnaState.getInstance().setMistakeQnaList(mMistakeQnaList);
        QnaState.getInstance().setRandomAnswers(mRandomAnswers);

        outState.putInt(SAVED_QNA_INDEX, mQnaIndex);
        outState.putInt(SAVED_ANSWER_LOCATION_INDEX, mAnswerLocationIndex);
        outState.putInt(SAVED_CORRECT_ANSWER, mCorrect);
        outState.putInt(SAVED_MISTAKE_ANSWER, mMistake);
        outState.putBoolean(SAVED_IS_INITIALIZED, isInitialized);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_qna_answer, menu);
        return super.onCreateOptionsMenu(menu);
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

            case R.id.action_quit:
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Methods for to check if answer is correct,
     * and update QnA
     */
    public void onClickOk(View view) {

        // get radio location
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        int selectedRadioButton = radioGroup.indexOfChild(radioGroup
                .findViewById(radioGroup.getCheckedRadioButtonId()));

        if (selectedRadioButton != mAnswerLocationIndex) {
            // add QnA to mistake list
            mMistakeQnaList.add(mQnaList.get(mQnaIndex));

            showMistakeDialog();
            mMistake++;
        } else {
            Snackbar.make(view, R.string.msg_correct, Snackbar.LENGTH_SHORT).show();
            mCorrect++;
            updateQna();
        }
    }

    /**
     * Method to initialize a question and answer,
     * updates GUI and will run on every increment of mQnaIndex
     */
    private void initQnA() {
        updateQuestionText();
        mRandomAnswers = generateRandomAnswers();

        Random random = new Random();
        mAnswerLocationIndex = random.nextInt(4);
        updateRadioText();

        updateTextProgress();
        mRadioGroup.clearCheck();
        isInitialized = true;
    }

    private void initRadioGroup() {
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

    /**
     * Generates an invalid random answers
     * which is not the same as the answer
     *
     * @return answerArray - generated random array of answers
     */
    private String[] generateRandomAnswers() {
        String[] answerArray = new String[3];

        ArrayList<QnA> originalList = QnaState.getInstance().getQnaList(true);
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

    /**
     * Checks if the newly generated random answer exists
     * on the array of random answer
     *
     * @param answerArray  the array of random answers
     * @param randomAnswer the newly generated random answer
     * @return true if the generated random answer is already at the array
     */
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

    /**
     * Resets the states of the variables, for resetting QnA
     */
    private void resetQnA() {
        mQnaIndex = 0;
        mCorrect = 0;
        mMistake = 0;

        mQnaList = QnaState.getInstance().getQnaList(true);
        Collections.shuffle(mQnaList);
        initQnA();

        Snackbar.make(getWindow().getDecorView().getRootView(),
                R.string.msg_reset, Snackbar.LENGTH_SHORT).show();
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
        QnaState.getInstance().setQnaList(mQnaList);
        QnaState.getInstance().setMistakeQnaList(mMistakeQnaList);

        // get passing
        String assessment = "Failed!";
        int passingCorrectPoints = mQnaList.size() / 2;
        if (mCorrect >= passingCorrectPoints && mCorrect != 0)
            assessment = "Passed!";

        startActivity(QnaResultActivity
                .getStartIntent(this, assessment, mCorrect));
        finish();
    }
}
