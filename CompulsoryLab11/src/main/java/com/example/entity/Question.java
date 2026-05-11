package com.example.entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "questions")
@NamedQueries({
    @NamedQuery(name = "Question.findByText",
        query = "SELECT q FROM Question q WHERE q.text LIKE :text"),
    @NamedQuery(name = "Question.countAll",
        query = "SELECT COUNT(q) FROM Question q")
})
public class Question implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Long id;

    @Column(name = "question_text", nullable = false, columnDefinition = "TEXT")
    private String text;

    @Column(name = "correct_answer", nullable = false)
    private String correctAnswer;

    @Column(name = "option1")
    private String option1;

    @Column(name = "option2")
    private String option2;

    @Column(name = "option3")
    private String option3;

    @Column(name = "option4")
    private String option4;

    public Question() {
    }

    public Question(String text, String correctAnswer, String option1, String option2, String option3, String option4) {
        this.text = text;
        this.correctAnswer = correctAnswer;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public String getOption4() {
        return option4;
    }

    public void setOption4(String option4) {
        this.option4 = option4;
    }

    @Transient
    public String[] getOptions() {
        return new String[]{option1, option2, option3, option4};
    }

    @Transient
    public boolean isCorrect(String answer) {
        return correctAnswer.equalsIgnoreCase(answer.trim());
    }

    @Transient
    public String getFormattedQuestion() {
        StringBuilder sb = new StringBuilder();
        sb.append("Q").append(id).append(": ").append(text).append("\n");
        sb.append("1. ").append(option1).append("\n");
        sb.append("2. ").append(option2).append("\n");
        sb.append("3. ").append(option3).append("\n");
        sb.append("4. ").append(option4).append("\n");
        return sb.toString();
    }

    @Override
    public String toString() {
        return "Question{id=" + id + ", text='" + text + "'}";
    }
}