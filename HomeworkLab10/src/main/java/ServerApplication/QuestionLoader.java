package ServerApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class QuestionLoader {
    public static List<Question> loadQuestions(String resourcePath) {
        List<Question> questions = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(QuestionLoader.class.getClassLoader().getResourceAsStream(resourcePath)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 6) {
                    int id = Integer.parseInt(parts[0].trim());
                    String text = parts[1].trim();
                    String correctAnswer = parts[2].trim();
                    String[] options = {
                        parts[2].trim(),
                        parts[3].trim(),
                        parts[4].trim(),
                        parts[5].trim()
                    };
                    questions.add(new Question(id, text, correctAnswer, options));
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to load questions: " + e.getMessage());
        }
        return questions;
    }
}