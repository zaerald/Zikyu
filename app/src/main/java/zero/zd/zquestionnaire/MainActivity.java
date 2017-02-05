package zero.zd.zquestionnaire;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // create a folder
        File folder = new File("/sdcard/ZQuestionnaire/");
        if (!folder.exists()) folder.mkdirs();
    }

    public void onClickAnswer(View view) {
        startActivity(new Intent(this, LoadQnaActivity.class));
    }
}
