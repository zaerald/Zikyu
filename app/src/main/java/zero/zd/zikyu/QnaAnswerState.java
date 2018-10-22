package zero.zd.zikyu;

import java.util.ArrayList;

import zero.zd.zikyu.model.QnA;
import zero.zd.zikyu.model.QnaSubject;

public class QnaAnswerState {
    private static final String TAG = QnaAnswerState.class.getSimpleName();
    private static final QnaAnswerState S_QNA_ANSWER_STATE_INSTANCE = new QnaAnswerState();

    private QnaSubject qnaSubject;
    private ArrayList<QnA> qnaList;
    private ArrayList<QnA> mistakeQnaList;
    private String[] randomAnswers;

    private QnaAnswerState() {
    }

    public static QnaAnswerState getInstance() {
        return S_QNA_ANSWER_STATE_INSTANCE;
    }

    public QnaSubject getQnaSubject() {
        return qnaSubject;
    }

    public void setQnaSubject(QnaSubject qnaSubject) {
        qnaSubject = qnaSubject;
    }

    public ArrayList<QnA> getQnaList(boolean isOriginalList) {
        if (isOriginalList)
            return qnaSubject.getQnaList();
        return qnaList;
    }

    public void setQnaList(ArrayList<QnA> qnaList) {
        qnaList = qnaList;
    }

    public ArrayList<QnA> getMistakeQnaList() {
        return mistakeQnaList;
    }

    public void setMistakeQnaList(ArrayList<QnA> mistakeQnaList) {
        mistakeQnaList = mistakeQnaList;
    }

    public String[] getRandomAnswers() {
        return randomAnswers;
    }

    public void setRandomAnswers(String[] randomAnswers) {
        randomAnswers = randomAnswers;
    }

    @Override
    public String toString() {
        String out = "QNA LIST: \n";
        for (QnA qna : qnaSubject.getQnaList()) {
            out += qna.toString() + "\n";
        }
        out += "END";
        return out;
    }
}
