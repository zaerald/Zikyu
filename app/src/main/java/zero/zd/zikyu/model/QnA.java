package zero.zd.zikyu.model;

/**
 * QnA class for creating Questions and Answers, this class provides
 * methods for accessing questions and answers
 */

public class QnA {

    private String mQuestion;
    private String mAnswer;
    private String[] mRandomAnswers;

    public QnA(String question, String answer) {
        this(question, answer, new String[0]);
    }

    public QnA(String question, String answer, String[] randomAnswers) {
        question = question.substring(0, 1).toUpperCase() + question.substring(1);
        this.mQuestion = question;
        this.mAnswer = answer;
        this.mRandomAnswers = randomAnswers;
    }

    public String getQuestion() {
        return mQuestion;
    }

    public void setQuestion(String question) {
        this.mQuestion = question;
    }

    public String getAnswer() {
        return mAnswer;
    }

    public void setAnswer(String answer) {
        this.mAnswer = answer;
    }

    public String[] getRandomAnswers() {
        return mRandomAnswers;
    }

    public void setRandomAnswers(String[] randomAnswers) {
        mRandomAnswers = randomAnswers;
    }

    @Override
    public String toString() {
        return "Question: " + getQuestion() + " : Answer: " + getAnswer();
    }
}
