package zero.zd.zquestionnaire;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class QnaResultActivity extends AppCompatActivity {

    private static final String TAG = QnaResultActivity.class.getSimpleName();
    private static final String EXTRA_ASSESSMENT = "EXTRA_ASSESSMENT";
    private static final String EXTRA_CORRECT = "EXTRA_CORRECT";
    private static final String EXTRA_MISTAKE = "EXTRA_MISTAKE";

    public static Intent getStartIntent(
            Context context, String assessment, int correct, int mistake) {
        Intent intent = new Intent(context, QnaResultActivity.class);
        intent.putExtra(EXTRA_ASSESSMENT, assessment);
        intent.putExtra(EXTRA_CORRECT, correct);
        intent.putExtra(EXTRA_MISTAKE, mistake);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qna_result);

        // get values
        String assessment = getIntent().getStringExtra(EXTRA_ASSESSMENT);
        int correct = getIntent().getIntExtra(EXTRA_CORRECT, -1);
        int mistake = getIntent().getIntExtra(EXTRA_MISTAKE, -1);
        Log.d(TAG, "Correct: " + correct + "\nMistake: " + mistake);

        // update assessment message
        TextView textAssessment = (TextView) findViewById(R.id.text_msg_assessment);
        textAssessment.setText(assessment);

        // update result message
        TextView textResult = (TextView) findViewById(R.id.text_msg_result);
        String correctMessage = getResources()
                .getQuantityString(R.plurals.msg_result_correct, correct, correct);
        String mistakeMessage = getResources()
                .getQuantityString(R.plurals.msg_result_mistake, mistake, mistake);
        textResult.setText(correctMessage + " " + mistakeMessage);
    }
}
