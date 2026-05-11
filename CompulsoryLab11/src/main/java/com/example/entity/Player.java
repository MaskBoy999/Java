package com.example.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "players")
public class Player extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "player_id")
    private Long id;

    @Column(name = "player_name", nullable = false, length = 100)
    private String name;

    @Column(name = "correct_answers")
    private int correctAnswers = 0;

    @Column(name = "total_response_time")
    private long totalResponseTime = 0;

    @Column(name = "games_played")
    private int gamesPlayed = 0;

    public Player() {
    }

    public Player(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(int correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public long getTotalResponseTime() {
        return totalResponseTime;
    }

    public void setTotalResponseTime(long totalResponseTime) {
        this.totalResponseTime = totalResponseTime;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public void incrementGamesPlayed() {
        this.gamesPlayed++;
    }

    public void recordAnswer(boolean correct, long responseTime) {
        if (correct) {
            this.correctAnswers++;
        }
        this.totalResponseTime += responseTime;
    }

    public boolean isBetterThan(Player other) {
        if (this.correctAnswers != other.correctAnswers) {
            return this.correctAnswers > other.correctAnswers;
        }
        return this.totalResponseTime < other.totalResponseTime;
    }

    @Override
    public String toString() {
        return "Player{id=" + id + ", name='" + name + "', correctAnswers=" + correctAnswers
                + ", totalResponseTime=" + totalResponseTime + '}';
    }
}
