package org.example;

import com.example.config.JpaConfig;
import com.example.entity.*;
import com.example.repository.*;
import com.example.service.GameService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(JpaConfig.class);

        PlayerRepository playerRepo = context.getBean(PlayerRepository.class);
        QuestionRepository questionRepo = context.getBean(QuestionRepository.class);
        GameRepository gameRepo = context.getBean(GameRepository.class);
        GameResultRepository resultRepo = context.getBean(GameResultRepository.class);
        GameService gameService = context.getBean(GameService.class);

        Player p1 = new Player("Alice");
        p1.setCorrectAnswers(8);
        p1.setTotalResponseTime(15000L);
        p1.setGamesPlayed(3);
        playerRepo.save(p1);

        Player p2 = new Player("Bob");
        p2.setCorrectAnswers(8);
        p2.setTotalResponseTime(12000L);
        p2.setGamesPlayed(2);
        playerRepo.save(p2);

        Game game = new Game("Championship", 4, 30);
        game.setStatus(GameStatus.WAITING);
        game.setCurrentPlayers(2);
        gameRepo.save(game);

        GameResult r1 = new GameResult(p1, game, 8, 15000L);
        resultRepo.save(r1);

        GameResult r2 = new GameResult(p2, game, 8, 12000L);
        resultRepo.save(r2);

        Question q = new Question("What is JPA?", "Java Persistence API",
                "Java Persistence API", "JPA", "JSP", "JSON");
        questionRepo.save(q);

        System.out.println("\n=== Players sorted by score ===");
        List<Player> sorted = playerRepo.findAllByOrderByCorrectAnswersDescTotalResponseTimeAsc();
        sorted.forEach(System.out::println);

        System.out.println("\n=== Active games ===");
        List<Game> active = gameRepo.findByActiveTrueOrderByCreatedAtDesc();
        active.forEach(System.out::println);

        System.out.println("\n=== Top results ===");
        List<GameResult> top = resultRepo.findTopNByOrderByCorrectAnswersDescTotalResponseTimeAsc(
                org.springframework.data.domain.PageRequest.of(0, 5));
        top.forEach(System.out::println);

        System.out.println("\n=== Statistics ===");
        GameService.GameStatistics stats = gameService.getStatistics();
        System.out.println("Total Players: " + stats.getTotalPlayers());
        System.out.println("Total Games: " + stats.getTotalGames());
        System.out.println("Total Results: " + stats.getTotalResults());
        System.out.println("Max Score: " + stats.getMaxScore());

        System.out.println("\n=== Auditing timestamps ===");
        sorted.forEach(p -> System.out.println(p.getName() + " - created: " + p.getCreatedAt() + ", updated: " + p.getUpdatedAt()));

        context.close();
    }
}
