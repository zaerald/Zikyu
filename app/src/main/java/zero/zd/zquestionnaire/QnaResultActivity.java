package zero.zd.zquestionnaire;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class QnaResultActivity extends AppCompatActivity {

    public static Intent getStartIntent(Context context) {
        return new Intent(context, QnaResultActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qna_result);
    }
}
