package zero.zd.zquestionnaire;

/**
 * QnA class for creating Questions and Answers, this class provides
 * methods for accessing questions and asnwers
 */

public class QnA {

    private String question;
    private String answer;

    public QnA(String question, String answer) {
        question = question.substring(0, 1).toUpperCase() + question.substring(1);
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        return "Question: " + getQuestion() + " : Answer: " + getAnswer();
    }
}
