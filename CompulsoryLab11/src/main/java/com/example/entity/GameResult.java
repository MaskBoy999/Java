package com.example.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "game_results")
public class GameResult extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "result_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id")
    private Game game;

    @Column(name = "correct_answers")
    private int correctAnswers;

    @Column(name = "total_response_time")
    private long totalResponseTime;

    @Column(name = "score")
    private int score;

    @Column(name = "rank_position")
    private int rankPosition;

    @Column(name = "played_at")
    private LocalDateTime playedAt;

    public GameResult() {
    }

    public GameResult(Player player, int correctAnswers, long totalResponseTime) {
        this.player = player;
        this.correctAnswers = correctAnswers;
        this.totalResponseTime = totalResponseTime;
        this.playedAt = LocalDateTime.now();
        calculateScore();
    }

    public GameResult(Player player, Game game, int correctAnswers, long totalResponseTime) {
        this.player = player;
        this.game = game;
        this.correctAnswers = correctAnswers;
        this.totalResponseTime = totalResponseTime;
        this.playedAt = LocalDateTime.now();
        calculateScore();
    }

    private void calculateScore() {
        this.score = (correctAnswers * 100) - (int)(totalResponseTime / 100);
    }

    @PrePersist
    protected void onCreate() {
        if (playedAt == null) {
            playedAt = LocalDateTime.now();
        }
        calculateScore();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(int correctAnswers) {
        this.correctAnswers = correctAnswers;
        calculateScore();
    }

    public long getTotalResponseTime() {
        return totalResponseTime;
    }

    public void setTotalResponseTime(long totalResponseTime) {
        this.totalResponseTime = totalResponseTime;
        calculateScore();
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getRankPosition() {
        return rankPosition;
    }

    public void setRankPosition(int rankPosition) {
        this.rankPosition = rankPosition;
    }

    public LocalDateTime getPlayedAt() {
        return playedAt;
    }

    public void setPlayedAt(LocalDateTime playedAt) {
        this.playedAt = playedAt;
    }

    @Override
    public String toString() {
        return "GameResult{id=" + id + ", player=" + (player != null ? player.getName() : "null")
                + ", correctAnswers=" + correctAnswers + ", score=" + score + '}';
    }
}
