package zero.zd.zquestionnaire;

/**
 * QnA class for creating Questions and Answers, this class provides
 * methods for accessing questions and asnwers
 */

class QnA {

    private String mQuestion;
    private String mAnswer;

    public QnA(String question, String answer) {
        question = question.substring(0, 1).toUpperCase() + question.substring(1);
        this.mQuestion = question;
        this.mAnswer = answer;
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

    @Override
    public String toString() {
        return "Question: " + getQuestion() + " : Answer: " + getAnswer();
    }
}
