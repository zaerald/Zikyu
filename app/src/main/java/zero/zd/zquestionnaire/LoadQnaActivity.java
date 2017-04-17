package zero.zd.zquestionnaire;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import zero.zd.zquestionnaire.model.QnaSubject;

public class LoadQnaActivity extends AppCompatActivity {

    private static final String TAG = LoadQnaActivity.class.getSimpleName();

    private ArrayList<QnaSubject> mSubjectList;
    private int mSelectedIndex;


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

        mSubjectList = new ArrayList<>();
        mSubjectList.add(QnaHelper.getBasicQnA());
        mSubjectList.add(QnaHelper.getBasicQnA());
        mSubjectList.add(QnaHelper.getBasicQnA());

        ListView list = (ListView) findViewById(R.id.list);
        ArrayAdapter adapter =
                new SubjectArrayAdapter(this, R.layout.list_subject, mSubjectList);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSelectedIndex = position;
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
        Toast.makeText(LoadQnaActivity.this, mSelectedIndex, Toast.LENGTH_SHORT).show();
        startActivity(QnaAnswerActivity.getStartIntent(LoadQnaActivity.this));
    }

    private static class ViewHolder {
        private TextView subjectName;
        private TextView qnaCount;
    }

    private class SubjectArrayAdapter extends ArrayAdapter {
        private Context mContext;
        private int mResource;
        private ArrayList<QnaSubject> mSubjectList;


        public SubjectArrayAdapter(
                Context context, int resource, ArrayList<QnaSubject> subjectList) {
            super(context, resource, subjectList);
            mContext = context;
            mResource = resource;
            mSubjectList = subjectList;
        }

        @Override
        public int getCount() {
            return mSubjectList.size();
        }

        @Nullable
        @Override
        public QnaSubject getItem(int position) {
            return mSubjectList.get(position);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            ViewHolder viewHolder;

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater)
                        mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(mResource, parent, false);

                viewHolder = new ViewHolder();
                viewHolder.subjectName = (TextView) convertView.findViewById(R.id.text_subject_name);
                viewHolder.qnaCount = (TextView) convertView.findViewById(R.id.text_qna_count);

                convertView.setTag(viewHolder);
            } else viewHolder = (ViewHolder) convertView.getTag();

            QnaSubject qnASubject = mSubjectList.get(position);

            viewHolder.subjectName.setText(qnASubject.getSubjectName());
            viewHolder.qnaCount.setText(getResources()
                    .getString(R.string.list_qna_count, qnASubject.getQnaList().size()));

            return convertView;
        }
    }
}
