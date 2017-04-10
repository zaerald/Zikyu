package zero.zd.zquestionnaire;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class QnaAnswerActivity extends AppCompatActivity {

    private static final String TAG = QnaAnswerActivity.class.getSimpleName();

    private ArrayList<QnA> mQnAList;
    private int mQnAIndex;
    private int mAnswerLocationIndex;

    private boolean mIsFinished;
    private int mCorrect;
    private int mMistake;

    RadioGroup mRadioGroup;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, QnaAnswerActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qna_answer);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.title_qna);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mRadioGroup = (RadioGroup) findViewById(R.id.radio_group);

        populateQnA();
        mQnAIndex = 0;

        initQnA();
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
     * Method used to initialize/populate QnA data into QnAList
     */
    private void populateQnA() {
        mQnAList = new ArrayList<>();
        mQnAList = QnAHelper.getSoftEngQnA();
        Collections.shuffle(mQnAList);
    }

    /**
     * Methods for to check if answer is correct,
     * and update QnA
     */
    public void onClickOk(View view) {

        // get radio location
        int selectedRadioButton = mRadioGroup.indexOfChild(mRadioGroup
                .findViewById(mRadioGroup.getCheckedRadioButtonId()));

        if (selectedRadioButton != mAnswerLocationIndex) {
            showMistakeDialog();
            if (mIsFinished) {
                return;
            }
            mMistake++;
        } else {
            Toast.makeText(QnaAnswerActivity.this,
                    R.string.msg_correct, Toast.LENGTH_SHORT).show();
            mCorrect++;
        }

        mQnAIndex++;
        if (mQnAIndex == mQnAList.size()) {
            mIsFinished = true;

            AlertDialog alertDialog = new AlertDialog.Builder(QnaAnswerActivity.this).create();
            alertDialog.setTitle("Finished!");

            // get passing
            String assessment = "Failed!";
            int passingCorrectPoints = mQnAList.size() / 2;
            if (mCorrect >= passingCorrectPoints)
                assessment = "Passed!";

            alertDialog.setMessage(assessment + "\nYou got: " + mCorrect + "/"
                    + mQnAList.size() + ".");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();

            resetQnA();
        }

        if (!mIsFinished) initQnA();
    }

    /**
     * Method to initialize a question and answer,
     * updates GUI and will run on every increment of mQnAIndex
     */
    private void initQnA() {
        TextView textQuestion = (TextView) findViewById(R.id.text_question);
        textQuestion.setText(mQnAList.get(mQnAIndex).getQuestion());

        int[] randIndices = new int[3];
        randIndices[0] = getRandomIndex();
        for (int i = 1; i < 3; i++) {
            while (true) {
                int x = getRandomIndex();
                if (!isRandomIndexExists(randIndices, x)) {
                    randIndices[i] = x;
                    break;
                }
            }
        }
        Log.i(TAG, "Random Indices: " + Arrays.toString(randIndices));

        Random random = new Random();
        mAnswerLocationIndex = random.nextInt(4);

        RadioButton btnOne = (RadioButton) findViewById(R.id.radio_one);
        RadioButton btnTwo = (RadioButton) findViewById(R.id.radio_two);
        RadioButton btnThree = (RadioButton) findViewById(R.id.radio_three);
        RadioButton btnFour = (RadioButton) findViewById(R.id.radio_four);
        mRadioGroup.check(R.id.radio_one);

        ArrayList<RadioButton> radioList = new ArrayList<>();
        radioList.add(btnOne);
        radioList.add(btnTwo);
        radioList.add(btnThree);
        radioList.add(btnFour);

//        Log.i(TAG, "AnswerLoc: " + mAnswerLocationIndex);
        radioList.get(mAnswerLocationIndex).setText(mQnAList.get(mQnAIndex).getAnswer());
        int randIndex = 0;
        for (int i = 0; i < 4; i++) {
            if (i == mAnswerLocationIndex)
                continue;
            radioList.get(i).setText(mQnAList.get(randIndices[randIndex]).getAnswer());
            randIndex++;
        }

        TextView txtProgress = (TextView) findViewById(R.id.text_progress);
        txtProgress.setText(String.format(getResources().getString(R.string.msg_progress),
                mQnAIndex + 1, mQnAList.size(), mCorrect, mMistake));
    }

    /**
     * Checks if the answer index generated from {@code getRandomIndex()}
     * if the index already exists on the generated index on array
     * {@code randIndices} for 3 invalid answers
     *
     * @param arr    the array of 3 index of invalid answers to check
     * @param target the newly generated index to compare to {@code arr}
     * @return {@code true} if the array randIndices already contains the
     * newly generated index of answer
     * {@code false} no same index or answer already existed at {@code randIndices}
     * @see #getRandomIndex()
     */
    private boolean isRandomIndexExists(int[] arr, int target) {
        for (int x : arr)
            if (x == target
                    || mQnAList.get(x).getAnswer()
                    .equalsIgnoreCase(mQnAList.get(target).getAnswer()))
                return true;
        return false;
    }

    /**
     * Generates an invalid random answer index and returns an index
     * which is not the same as the answer
     *
     * @return random index
     */
    private int getRandomIndex() {
        Random random = new Random();
        while (true) {
            int x = random.nextInt(mQnAList.size());
            if (x != mQnAIndex && !mQnAList.get(x).getAnswer()
                    .equalsIgnoreCase(mQnAList.get(mQnAIndex).getAnswer())) return x;
        }
    }

    /**
     * Resets the states of the variables, for resetting QnA
     */
    private void resetQnA() {
        mIsFinished = false;
        mQnAIndex = 0;
        mCorrect = 0;
        mMistake = 0;

        populateQnA();
        initQnA();

        if (!mIsFinished)
            Snackbar.make(getWindow().getDecorView().getRootView(),
                    R.string.msg_reset, Snackbar.LENGTH_SHORT).show();
    }

    private void showMistakeDialog() {
        String msg = "Correct Answer: \n" + mQnAList.get(mQnAIndex).getAnswer();
        new AlertDialog.Builder(QnaAnswerActivity.this)
                .setTitle("Mistake!")
                .setMessage(msg)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

}
