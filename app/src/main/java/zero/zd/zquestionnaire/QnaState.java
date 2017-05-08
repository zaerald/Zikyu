package zero.zd.zquestionnaire;

import java.util.ArrayList;

import zero.zd.zquestionnaire.model.QnA;
import zero.zd.zquestionnaire.model.QnaSubject;

public class QnaState {

    private static final String TAG = QnaState.class.getSimpleName();
    private static final QnaState sQnaStateInstance = new QnaState();

    private QnaSubject mQnaSubject;
    private ArrayList<QnA> mMistakeQnaList;


    private QnaState() {
    }

    public static QnaState getInstance() {
        return sQnaStateInstance;
    }

    public QnaSubject getQnaSubject() {
        return mQnaSubject;
    }

    public void setQnaSubject(QnaSubject qnaSubject) {
        mQnaSubject = qnaSubject;
    }

    public ArrayList<QnA> getQnAList() {
        return mQnaSubject.getQnaList();
    }

    public void setQnAList(ArrayList<QnA> qnAList) {
        mQnaSubject.setQnaList(qnAList);
    }

    public ArrayList<QnA> getMistakeQnaList() {
        return mMistakeQnaList;
    }

    public void setMistakeQnaList(ArrayList<QnA> mistakeQnaList) {
        mMistakeQnaList = mistakeQnaList;
    }

    @Override
    public String toString() {
        String out = "QNA LIST: \n";
        for (QnA qna : mQnaSubject.getQnaList()) {
            out += qna.toString() + "\n";
        }
        out += "END";
        return out;
    }
}
