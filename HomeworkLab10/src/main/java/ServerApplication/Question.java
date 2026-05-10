package ServerApplication;

public class Question {
    private final int id;
    private final String text;
    private final String correctAnswer;
    private final String[] options;

    public Question(int id, String text, String correctAnswer, String[] options) {
        this.id = id;
        this.text = text;
        this.correctAnswer = correctAnswer;
        this.options = options;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public String[] getOptions() {
        return options;
    }

    public boolean isCorrect(String answer) {
        return correctAnswer.equalsIgnoreCase(answer.trim());
    }

    public String getFormattedQuestion() {
        StringBuilder sb = new StringBuilder();
        sb.append("Q").append(id).append(": ").append(text).append("\n");
        for (int i = 0; i < options.length; i++) {
            sb.append(i + 1).append(". ").append(options[i]).append("\n");
        }
        return sb.toString();
    }
}