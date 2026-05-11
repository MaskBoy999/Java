package com.example.service;

import com.example.entity.Player;
import com.example.entity.GameResult;
import com.example.entity.Game;
import com.example.repository.PlayerRepository;
import com.example.repository.GameResultRepository;
import com.example.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class GameService {

    private final PlayerRepository playerRepository;
    private final GameResultRepository gameResultRepository;
    private final GameRepository gameRepository;

    @Autowired
    public GameService(PlayerRepository playerRepository,
                       GameResultRepository gameResultRepository,
                       GameRepository gameRepository) {
        this.playerRepository = playerRepository;
        this.gameResultRepository = gameResultRepository;
        this.gameRepository = gameRepository;
    }

    @Transactional
    public Player savePlayer(Player player) {
        return playerRepository.save(player);
    }

    @Transactional(readOnly = true)
    public Player findPlayerByName(String name) {
        return playerRepository.findByNameIgnoreCase(name).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Player> getAllPlayersSortedByScore() {
        return playerRepository.findAllByOrderByCorrectAnswersDescTotalResponseTimeAsc();
    }

    @Transactional(readOnly = true)
    public Page<Player> getPlayersPaginated(Pageable pageable) {
        return playerRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public List<Player> searchPlayersByName(String pattern) {
        return playerRepository.findByNameContaining(pattern);
    }

    @Transactional(readOnly = true)
    public List<Player> getTopPlayers(int limit) {
        return playerRepository.findTopByOrderByCorrectAnswersDescTotalResponseTimeAsc(PageRequest.of(0, limit));
    }

    @Transactional(readOnly = true)
    public List<Player> findPlayersWithSpecifications(Integer minCorrectAnswers, Integer minGamesPlayed) {
        Specification<Player> spec = Specification.where(null);

        if (minCorrectAnswers != null) {
            spec = spec.and((root, query, cb) ->
                cb.greaterThan(root.get("correctAnswers"), minCorrectAnswers));
        }

        if (minGamesPlayed != null) {
            spec = spec.and((root, query, cb) ->
                cb.greaterThan(root.get("gamesPlayed"), minGamesPlayed));
        }

        return playerRepository.findAll(spec);
    }

    @Transactional
    public Game saveGame(Game game) {
        return gameRepository.save(game);
    }

    @Transactional(readOnly = true)
    public Game findGameById(Long id) {
        return gameRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Game> getActiveGames() {
        return gameRepository.findByActiveTrueOrderByCreatedAtDesc();
    }

    @Transactional(readOnly = true)
    public Page<Game> getGamesPaginated(Pageable pageable) {
        return gameRepository.findByActiveTrue(pageable);
    }

    @Transactional(readOnly = true)
    public List<Game> findGamesByStatus(com.example.entity.GameStatus status) {
        return gameRepository.findByStatus(status);
    }

    @Transactional
    public GameResult saveGameResult(GameResult gameResult) {
        return gameResultRepository.save(gameResult);
    }

    @Transactional(readOnly = true)
    public List<GameResult> getGameResultsForPlayer(Player player) {
        return gameResultRepository.findByPlayerOrderByPlayedAtDesc(player);
    }

    @Transactional(readOnly = true)
    public List<GameResult> getTopGameResults(int limit) {
        return gameResultRepository.findTopNByOrderByCorrectAnswersDescTotalResponseTimeAsc(PageRequest.of(0, limit));
    }

    @Transactional(readOnly = true)
    public Page<GameResult> getGameResultsPaginated(Player player, Pageable pageable) {
        return gameResultRepository.findByPlayer(player, pageable);
    }

    @Transactional(readOnly = true)
    public List<GameResult> getResultsBetween(LocalDateTime start, LocalDateTime end) {
        return gameResultRepository.findResultsBetween(start, end);
    }

    @Transactional(readOnly = true)
    public Double getAverageScoreForPlayer(Player player) {
        return gameResultRepository.averageCorrectAnswersByPlayer(player);
    }

    @Transactional(readOnly = true)
    public List<GameResult> findResultsWithSpecifications(Integer minScore, LocalDateTime startDate) {
        Specification<GameResult> spec = Specification.where(null);

        if (minScore != null) {
            spec = spec.and((root, query, cb) ->
                cb.greaterThan(root.get("score"), minScore));
        }

        if (startDate != null) {
            spec = spec.and((root, query, cb) ->
                cb.greaterThan(root.get("playedAt"), startDate));
        }

        return gameResultRepository.findAll(spec);
    }

    @Transactional(readOnly = true)
    public GameStatistics getStatistics() {
        GameStatistics stats = new GameStatistics();
        stats.setTotalPlayers(playerRepository.count());
        stats.setTotalGames(gameRepository.count());
        stats.setTotalResults(gameResultRepository.countTotalResults());
        stats.setMaxScore(gameResultRepository.findMaxScore());
        stats.setAverageResponseTime(gameResultRepository.findAverageResponseTime());
        return stats;
    }

    public static class GameStatistics {
        private long totalPlayers;
        private long totalGames;
        private long totalResults;
        private Integer maxScore;
        private Double averageResponseTime;

        public long getTotalPlayers() { return totalPlayers; }
        public void setTotalPlayers(long totalPlayers) { this.totalPlayers = totalPlayers; }
        public long getTotalGames() { return totalGames; }
        public void setTotalGames(long totalGames) { this.totalGames = totalGames; }
        public long getTotalResults() { return totalResults; }
        public void setTotalResults(long totalResults) { this.totalResults = totalResults; }
        public Integer getMaxScore() { return maxScore; }
        public void setMaxScore(Integer maxScore) { this.maxScore = maxScore; }
        public Double getAverageResponseTime() { return averageResponseTime; }
        public void setAverageResponseTime(Double averageResponseTime) { this.averageResponseTime = averageResponseTime; }
    }
}
