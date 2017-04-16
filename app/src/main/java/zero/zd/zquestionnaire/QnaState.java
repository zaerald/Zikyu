package zero.zd.zquestionnaire;

import android.util.Log;

import java.util.ArrayList;

import zero.zd.zquestionnaire.model.QnA;

public class QnaState {

    private static final String TAG = QnaState.class.getSimpleName();
    private static final QnaState sQnaStateInstance = new QnaState();

    private ArrayList<QnA> mQnAList;
    private ArrayList<QnA> mMistakeQnaList;


    public static QnaState getInstance() {
        return sQnaStateInstance;
    }

    private QnaState() {}

    public ArrayList<QnA> getQnAList() {
        return mQnAList;
    }

    public void setQnAList(ArrayList<QnA> qnAList) {
        mQnAList = qnAList;
    }

    public ArrayList<QnA> getMistakeQnaList() {
        return mMistakeQnaList;
    }

    public void setMistakeQnaList(ArrayList<QnA> mistakeQnaList) {
        mMistakeQnaList = mistakeQnaList;
    }

    public void updateListFromMistakes() {
        mQnAList = new ArrayList<>(mMistakeQnaList);
        Log.d(TAG, "mQnAList Size: " + mQnAList.size());
        mMistakeQnaList = new ArrayList<>();
        Log.d(TAG, "Clean mistakeList");
        Log.d(TAG, "mQnAList Size: " + mQnAList.size());
    }
}
