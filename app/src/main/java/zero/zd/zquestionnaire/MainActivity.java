package zero.zd.zquestionnaire;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    public static Intent getStartIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickAnswer(View view) {
        startActivity(LoadQnaActivity.getStartIntent(MainActivity.this));
    }

    public void onClickBuilder(View view) {
        startActivity(QnaBuilderActivity.getStartIntent(MainActivity.this));
    }

}
