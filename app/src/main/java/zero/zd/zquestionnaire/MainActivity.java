package zero.zd.zquestionnaire;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private ArrayList<QnA> mQnAList;
    private int mQnAIndex;
    private int mAnswerLocationIndex;

    boolean mIsFinished;
    private int mCorrect;
    private int mMistake;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(R.string.title_qna);

        populateQnA();
        mQnAIndex = 0;

        initQnA();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

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

    public void onClickButtonOne(View view) { buttonClick(0); }

    public void onClickButtonTwo(View view) { buttonClick(1); }

    public void onClickButtonThree(View view) {
        buttonClick(2);
    }

    public void onClickButtonFour(View view) { buttonClick(3); }

    /**
     * Method used to initialize/populate data in QnAList
     */
    private void populateQnA() {
        mQnAList = new ArrayList<>();
        mQnAList = QnAHelper.getSoftEngQnA();
        Collections.shuffle(mQnAList);
    }

    /**
     * One method for the 4 answer buttons for DRY practice
     * @param num
     *      the index/position of the button clicked.
     */
    private void buttonClick(int num) {

        View view = findViewById(R.id.activity_main);

        if (num != mAnswerLocationIndex) {
            if (mIsFinished)
                return;
            String out = "Mistake!\nCorrect Answer: " + mQnAList.get(mQnAIndex).getAnswer();
            Snackbar.make(view, out, Snackbar.LENGTH_SHORT).show();
            mMistake++;
        } else {
            Snackbar.make(view, R.string.msg_correct, Snackbar.LENGTH_SHORT).show();
            mCorrect++;
        }

        mQnAIndex++;
        if (mQnAIndex == mQnAList.size()) {
            mIsFinished = true;

            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
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

        Button btnOne = (Button) findViewById(R.id.button_one);
        Button btnTwo = (Button) findViewById(R.id.button_two);
        Button btnThree = (Button) findViewById(R.id.button_three);
        Button btnFour = (Button) findViewById(R.id.button_four);

        ArrayList<Button> btnList = new ArrayList<>();
        btnList.add(btnOne);
        btnList.add(btnTwo);
        btnList.add(btnThree);
        btnList.add(btnFour);

//        Log.i(TAG, "AnswerLoc: " + mAnswerLocationIndex);
        btnList.get(mAnswerLocationIndex).setText(mQnAList.get(mQnAIndex).getAnswer());
        int randIndex = 0;
        for (int i = 0; i < 4; i++) {
            if (i == mAnswerLocationIndex)
                continue;
            btnList.get(i).setText(mQnAList.get(randIndices[randIndex]).getAnswer());
            randIndex++;
        }

        TextView txtProgress = (TextView) findViewById(R.id.text_progress);
        txtProgress.setText(String.format(getResources().getString(R.string.msg_progress),
                mQnAIndex + 1, mQnAList.size(), mCorrect, mMistake));
    }

    /**
     * Checks if the answer index generated from {@code getRandomIndex()}
     * if the index already exists on the generated index on array
     * randIndices for 3 invalid answers
     *
     * @param arr the array of 3 index of invalid answers
     * @param target the newly generated index
     * @return
     *      {@code true} if the array randIndices already contains the
     *          newly generated index of answer
     *      {@code false}
     *
     * @see #getRandomIndex()
     */
    private boolean isRandomIndexExists(int[] arr, int target) {
        // false if same index and same answer
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

}
