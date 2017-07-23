package zero.zd.zikyu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class QnaResultActivity extends AppCompatActivity {

    private static final String TAG = QnaResultActivity.class.getSimpleName();
    private static final String EXTRA_ASSESSMENT = "EXTRA_ASSESSMENT";
    private static final String EXTRA_CORRECT = "EXTRA_CORRECT";

    private TextView mAssessmentText;
    private boolean mIsReset;

    public static Intent getStartIntent(Context context, String assessment, int correct) {
        Intent intent = new Intent(context, QnaResultActivity.class);
        intent.putExtra(EXTRA_ASSESSMENT, assessment);
        intent.putExtra(EXTRA_CORRECT, correct);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qna_result);

        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(R.string.title_result_activity);

        // get values
        String assessment = getIntent().getStringExtra(EXTRA_ASSESSMENT);
        int correct = getIntent().getIntExtra(EXTRA_CORRECT, -1);
        int qnaTotal = QnaAnswerState.getInstance().getQnaList(false).size();
        Log.d(TAG, "Correct: " + correct);

        mAssessmentText = (TextView) findViewById(R.id.text_msg_assessment);
        mAssessmentText.setText(assessment);
        if (assessment.equalsIgnoreCase("passed!")) {
            updateAssessmentTextColor(R.color.assessment_passed);
        } else {
            updateAssessmentTextColor(R.color.assessment_failed);
        }

        // update result message
        TextView textResult = (TextView) findViewById(R.id.text_msg_result);
        textResult.setText(getResources().getString(R.string.msg_result, correct, qnaTotal));

        // update button
        Button mAnswerButton = (Button) findViewById(R.id.button_answer);
        if (correct == qnaTotal) {
            mAnswerButton.setText(getResources().getString(R.string.action_reset_qna));
            mIsReset = true;
        } else {
            mAnswerButton.setText(getResources().getString(R.string.action_answer_mistake));
            mIsReset = false;
        }
    }

    public void onClickAnswer(View view) {
        startActivity(QnaAnswerActivity
                .getStartIntent(QnaResultActivity.this, !mIsReset));
        finish();
    }

    public void onClickLoadDifferentQna(View view) {
        startActivity(QnaLoadActivity
                .getStartIntent(QnaResultActivity.this));
        finish();
    }

    private void updateAssessmentTextColor(int color) {
        mAssessmentText.setTextColor(ResourcesCompat
                .getColor(getResources(), color, null));
    }
}
