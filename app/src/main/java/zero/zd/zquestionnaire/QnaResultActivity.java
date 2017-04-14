package zero.zd.zquestionnaire;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class QnaResultActivity extends AppCompatActivity {

    private static final String TAG = QnaResultActivity.class.getSimpleName();
    private static final String EXTRA_CORRECT = "EXTRA_CORRECT";
    private static final String EXTRA_MISTAKE = "EXTRA_MISTAKE";

    public static Intent getStartIntent(Context context, int correct, int mistake) {
        Intent intent = new Intent(context, QnaResultActivity.class);
        intent.putExtra(EXTRA_CORRECT, correct);
        intent.putExtra(EXTRA_MISTAKE, mistake);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qna_result);

        int correct = getIntent().getIntExtra(EXTRA_CORRECT, -1);
        int mistake = getIntent().getIntExtra(EXTRA_MISTAKE, -1);

        Log.d(TAG, "Correct: " + correct + "\nMistake: " + mistake);
    }
}
