package zero.zd.zikyu;

import java.util.ArrayList;

import zero.zd.zikyu.model.QnA;
import zero.zd.zikyu.model.QnaSubject;

public class QnaAnswerState {
    private static final QnaAnswerState S_QNA_ANSWER_STATE_INSTANCE = new QnaAnswerState();

    private QnaSubject mQnaSubject;
    private ArrayList<QnA> mQnaList;
    private ArrayList<QnA> mMistakeQnaList;
    private String[] mRandomAnswers;

    private QnaAnswerState() {
    }

    public static QnaAnswerState getInstance() {
        return S_QNA_ANSWER_STATE_INSTANCE;
    }

    public QnaSubject getQnaSubject() {
        return mQnaSubject;
    }

    public void setQnaSubject(QnaSubject qnaSubject) {
        mQnaSubject = qnaSubject;
    }

    public ArrayList<QnA> getQnaList(boolean isOriginalList) {
        if (isOriginalList) return mQnaSubject.getQnaList();
        return mQnaList;
    }

    public void setQnaList(ArrayList<QnA> qnaList) {
        mQnaList = qnaList;
    }

    public ArrayList<QnA> getMistakeQnaList() {
        return mMistakeQnaList;
    }

    public void setMistakeQnaList(ArrayList<QnA> mistakeQnaList) {
        mMistakeQnaList = mistakeQnaList;
    }

    public String[] getRandomAnswers() {
        return mRandomAnswers;
    }

    public void setRandomAnswers(String[] randomAnswers) {
        mRandomAnswers = randomAnswers;
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
