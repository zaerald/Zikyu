package zero.zd.zquestionnaire;

import java.util.ArrayList;

import zero.zd.zquestionnaire.model.QnA;

public class QnaState {
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
}
