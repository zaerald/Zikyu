package zero.zd.zquestionnaire;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class QnaResultActivity extends AppCompatActivity {

    private static final String TAG = QnaResultActivity.class.getSimpleName();
    private static final String EXTRA_ASSESSMENT = "EXTRA_ASSESSMENT";
    private static final String EXTRA_CORRECT = "EXTRA_CORRECT";

    public static Intent getStartIntent(
            Context context, String assessment, int correct) {
        Intent intent = new Intent(context, QnaResultActivity.class);
        intent.putExtra(EXTRA_ASSESSMENT, assessment);
        intent.putExtra(EXTRA_CORRECT, correct);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qna_result);

        // get values
        String assessment = getIntent().getStringExtra(EXTRA_ASSESSMENT);
        int correct = getIntent().getIntExtra(EXTRA_CORRECT, -1);
        int qnaTotal = QnaState.getInstance().getQnaList(false).size();
        Log.d(TAG, "Correct: " + correct);

        // update assessment message
        TextView textAssessment = (TextView) findViewById(R.id.text_msg_assessment);
        textAssessment.setText(assessment);
        if (assessment.equalsIgnoreCase("passed!")) {
            textAssessment.setTextColor(ResourcesCompat
                    .getColor(getResources(), R.color.assessment_passed, null));
        } else {
            textAssessment.setTextColor(ResourcesCompat
                    .getColor(getResources(), R.color.assessment_failed, null));
        }

        // update result message
        TextView textResult = (TextView) findViewById(R.id.text_msg_result);
        textResult.setText(getResources().getString(R.string.msg_result, correct, qnaTotal));
    }

    public void onClickAnswerMistakes(View view) {
        startActivity(QnaAnswerActivity.getStartIntent(QnaResultActivity.this, true));
        finish();
    }
}
