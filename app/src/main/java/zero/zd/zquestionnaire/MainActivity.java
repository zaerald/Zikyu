package zero.zd.zquestionnaire;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // create a folder
        File folder = new File(Environment.getExternalStorageDirectory().getPath() + "/ZQuestionnaire/");
        if (!folder.exists()) {
            if (!folder.mkdirs()) Log.e(TAG, "Failed on creating folders.");
        }
    }

    public void onClickAnswer(View view) {
        startActivity(LoadQnaActivity.getStartIntent(MainActivity.this));
    }

    public void onClickBuilder(View view) {
        startActivity(QnaBuilderActivity.getStartIntent(MainActivity.this));
    }

}
