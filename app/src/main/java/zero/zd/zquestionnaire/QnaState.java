package zero.zd.zquestionnaire;

public class QnaState {
    private static final QnaState qnaStateInstance = new QnaState();

    public static QnaState getInstance() {
        return qnaStateInstance;
    }

    private QnaState() {}
}
