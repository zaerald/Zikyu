package zero.zd.zquestionnaire;

import java.util.ArrayList;

public class QnaState {
    private static final QnaState sQnaStateInstance = new QnaState();

    private ArrayList<QnA> mQnAList;


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
}
