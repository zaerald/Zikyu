package zero.zd.zquestionnaire;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LoadQnaActivity extends AppCompatActivity {

    private static final String TAG = LoadQnaActivity.class.getSimpleName();

    private List<String> mQnaFileList;
    private String mSelectedName;


    public static Intent getStartIntent(Context context) {
        return new Intent(context, LoadQnaActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_qna);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mQnaFileList = new ArrayList<>();

        File folder = new File(Environment.getExternalStorageDirectory().getPath() + "/ZQuestionnaire/");
        File files[] = folder.listFiles();

        if (files.length != 0) {
            for (File f : files) {
                String name = f.getName();
                if (!name.contains(".zdq"))
                    continue;
                name = name.substring(0, name.length() - 4);
                mQnaFileList.add(name);
            }
        }

        ListView list = (ListView) findViewById(R.id.list);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mQnaFileList);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSelectedName = mQnaFileList.get(position);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClickLoad(View view) {
        Toast.makeText(LoadQnaActivity.this, mSelectedName, Toast.LENGTH_SHORT).show();
        startActivity(QnaAnswerActivity.getStartIntent(LoadQnaActivity.this));
    }
}
