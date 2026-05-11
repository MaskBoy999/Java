package ServerApplication;

import com.example.entity.Question;
import com.example.repository.QuestionRepository;
import com.example.config.JpaConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads questions from a resource file and optionally persists them via JPA.
 */
public class QuestionLoader {

    /**
     * Loads questions from a resource file using the original format.
     *
     * @param resourcePath the classpath resource path
     * @return a list of Question entities
     */
    public static List<Question> loadQuestions(String resourcePath) {
        List<Question> questions = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(QuestionLoader.class.getClassLoader().getResourceAsStream(resourcePath)))) {
            String line;
            int idCounter = 1;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 6) {
                    String text = parts[1].trim();
                    String correctAnswer = parts[2].trim();
                    String option1 = parts[2].trim(); // correct answer is option 1
                    String option2 = parts[3].trim();
                    String option3 = parts[4].trim();
                    String option4 = parts[5].trim();

                    Question question = new Question(text, correctAnswer, option1, option2, option3, option4);
                    questions.add(question);
                }
            }
            System.out.println("Loaded " + questions.size() + " questions from resource file.");
        } catch (IOException e) {
            System.err.println("Failed to load questions: " + e.getMessage());
        }
        return questions;
    }

    /**
     * Persists a list of questions to the database via Spring Data JPA.
     *
     * @param questions the list of questions to persist
     * @return the saved questions
     */
    public static List<Question> persistQuestions(List<Question> questions) {
        try {
            AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(JpaConfig.class);
            QuestionRepository questionRepo = context.getBean(QuestionRepository.class);

            // Check if questions already exist
            List<Question> existing = questionRepo.findAllByOrderByIdAsc();
            if (!existing.isEmpty()) {
                System.out.println("Questions already exist in database (" + existing.size() + " questions).");
                context.close();
                return existing;
            }

            // Save all new questions
            List<Question> saved = new ArrayList<>();
            for (Question q : questions) {
                saved.add(questionRepo.save(q));
            }
            System.out.println("Persisted " + saved.size() + " questions to database.");
            context.close();
            return saved;
        } catch (Exception e) {
            System.err.println("Warning: Could not persist questions to database: " + e.getMessage());
            return questions;
        }
    }
}