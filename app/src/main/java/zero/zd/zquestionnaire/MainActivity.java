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

    public void onClickButtonOne(View view) {
        buttonClick(view, 0);
    }

    public void onClickButtonTwo(View view) { buttonClick(view, 1); }

    public void onClickButtonThree(View view) {
        buttonClick(view, 2);
    }

    public void onClickButtonFour(View view) { buttonClick(view, 3); }

    private void populateQnA() {
        mQnAList = new ArrayList<>();
        mQnAList.add(new QnA("The product that software professionals build then support over long term",
                "Software"));
        mQnAList.add(new QnA("When executed provides desired features, function and performance",
                "Instructions"));
        mQnAList.add(new QnA("Enables the programs to adequately store and manipulate information",
                "Data Structures"));
        mQnAList.add(new QnA("Describes the operation and use of the programs", "Documentation"));
        mQnAList.add(new QnA("Stand-alone systems that are marketed and sold to any customer who wishes to buy them",
                "Generic Products"));
        mQnAList.add(new QnA("Software that is commissioned by a specific customer to meet their own needs",
                "Customized Products"));
        mQnAList.add(new QnA("Is concerned with theories, methods and tools for professional software development",
                "Software Engineering"));
        mQnAList.add(new QnA("Often dominate computer system costs. The costs of software on a PC are often greater than the hardware cost",
                "Software costs"));
        mQnAList.add(new QnA("Is developed or engineered , it is not manufactured in the classical sense which has quality problem",
                "Software"));
        mQnAList.add(new QnA("A software application such as compilers, editors, file management utilities",
                "System Software"));
        mQnAList.add(new QnA("A stand-alone programs for specific needs", "Application Software"));
        mQnAList.add(new QnA("Characterized by \"number crunching\" algorithms, such as automotive stress analysis, molecular biology, orbital dynamics etc.",
                "Engineering/Scientific Software"));
        mQnAList.add(new QnA("Resides within a product or system", "Embedded Software"));
        mQnAList.add(new QnA("Foccus on a limited marketplace to address mass consumer market",
                "Product-line Software"));
        mQnAList.add(new QnA("Network centric software, more sophisticated computing environment is supported integrated with remote database and business application",
                "WebApps"));
        mQnAList.add(new QnA("Software uses non-commercial algorithm to solve complex problems",
                "AI"));
        mQnAList.add(new QnA("Pervasive, ubiquitous, distributed computing due to wireless networking",
                "Open world computing"));
        mQnAList.add(new QnA("The web as a computing engine. How to architect simple and sophisticated applications to target end-users worldwide.",
                "Netsourcing"));
        mQnAList.add(new QnA("\"free\" source code open to the computing community", "Open Source"));
        mQnAList.add(new QnA("Is The establishment and use of sound engineering principles n order to obtain economically software that is reliable and works efficiently on real machines",
                "Software Engineering"));
        mQnAList.add(new QnA("The application of a semantic disciplined, quantifiable approach to the development, operation, and maintenance of software",
                "Software Engineering"));
        mQnAList.add(new QnA("Computer programs, data structures and associated documentation. Software products may be developed for a particular customer or may be developed for a general market.",
                "Software"));
        mQnAList.add(new QnA("Characteristic of software that should be written in such a way so that it can evolve to meet the changing needs of customers. This is a critical attribute because software change is an inevitable requirement of a changing business environment.",
                "Maintainability"));
        mQnAList.add(new QnA("Software dependability includes a range of characteristics including reliability, security and safety. Dependable software should not cause physical or economic damage in the event of system failure. Malicious users should not be  able to access or damage the system.",
                "Dependability and security"));
        mQnAList.add(new QnA("Software should not make wasteful use of system resources such as memory and processor cycles. Efficiency therefore includes responsiveness, processing time, memory utilisation, etc.",
                "Efficiency"));
        mQnAList.add(new QnA("Software must be acceptable to the type of users for which it is designed. This means that it must be understandable, usable and compatible with other systems that they use.",
                "Acceptability"));
        mQnAList.add(new QnA("Provides automated or semi-automated support for the process and methods",
                "Tools"));
        mQnAList.add(new QnA("Provides technical how-to’s for building software. It encompasses many tasks including communication, requirement analysis, design modeling, program construction, testing and support.",
                "Method"));
        mQnAList.add(new QnA("The foundation defines a framework with activities for effective delivery of software engineering technology. Establish the context where products (model, data, report, and forms) are produced, milestone are established, quality is ensured and change is managed",
                "Process Layer"));
        mQnAList.add(new QnA("Is a collection of activities, actions and tasks that are performed when some work product is to be created.",
                "Process"));
        mQnAList.add(new QnA("Is to deliver software in a timely manner and with sufficient quality to satisfy those who have sponsored its creation and those who will use it.",
                "Purpose of Process"));
        mQnAList.add(new QnA("Communicate with customer to understand objectives and gather requirements",
                "Communication"));
        mQnAList.add(new QnA("Creates a “map” defines the work by describing the tasks, risks and resources, work products and work schedule.",
                "Planning"));
        mQnAList.add(new QnA("Create a “sketch”, what it looks like architecturally, how the constituent parts fit together and other characteristics.",
                "Modelling"));
        mQnAList.add(new QnA("Code generation and the testing.", "Construction"));
        mQnAList.add(new QnA("Delivered to the customer who evaluates the products and provides feedback based on the evaluation.",
                "Deployment"));
        mQnAList.add(new QnA("", ""));

        Collections.shuffle(mQnAList);
    }

    private void buttonClick(View view, int num) {

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

    private void initQnA() {
        TextView textQuestion = (TextView) findViewById(R.id.text_question);
        textQuestion.setText(mQnAList.get(mQnAIndex).getQuestion());

        int[] randIndices = new int[3];
        randIndices[0] = getRandomIndex(mQnAList.size());
        for (int i = 1; i < 3; i++) {
            while (true) {
                int x = getRandomIndex(mQnAList.size());
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

    private boolean isRandomIndexExists(int[] arr, int target) {
        // false if same index and same answer
        for (int x : arr)
            if (x == target
                    || mQnAList.get(x).getAnswer().equalsIgnoreCase(mQnAList.get(target).getAnswer()))
                return true;
        return false;
    }

    private int getRandomIndex(int size) {
        Random random = new Random();
        while (true) {
            int x = random.nextInt(size);
            if (x != mQnAIndex) return x;
        }
    }

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
