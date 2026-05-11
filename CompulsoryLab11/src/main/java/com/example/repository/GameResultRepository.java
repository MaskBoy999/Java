package com.example.repository;

import com.example.entity.GameResult;
import com.example.entity.Player;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface GameResultRepository extends JpaRepository<GameResult, Long>, JpaSpecificationExecutor<GameResult> {

    List<GameResult> findByPlayerOrderByPlayedAtDesc(Player player);

    Page<GameResult> findByPlayer(Player player, Pageable pageable);

    @Query("SELECT gr FROM GameResult gr ORDER BY gr.correctAnswers DESC, gr.totalResponseTime ASC")
    List<GameResult> findTopNByOrderByCorrectAnswersDescTotalResponseTimeAsc(Pageable pageable);

    List<GameResult> findByPlayer(Player player);

    @Query("SELECT gr FROM GameResult gr WHERE gr.playedAt BETWEEN :start AND :end ORDER BY gr.playedAt DESC")
    List<GameResult> findResultsBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COUNT(gr) FROM GameResult gr WHERE gr.player = :player")
    long countByPlayer(@Param("player") Player player);

    @Query("SELECT AVG(gr.correctAnswers) FROM GameResult gr WHERE gr.player = :player")
    Double averageCorrectAnswersByPlayer(@Param("player") Player player);

    @Query("SELECT gr FROM GameResult gr WHERE gr.player = :player AND gr.correctAnswers = " +
           "(SELECT MAX(gr2.correctAnswers) FROM GameResult gr2 WHERE gr2.player = :player) " +
           "ORDER BY gr.totalResponseTime ASC")
    List<GameResult> findBestResultByPlayer(@Param("player") Player player);

    default GameResult getBestResultByPlayer(Player player) {
        List<GameResult> results = findBestResultByPlayer(player);
        return results.isEmpty() ? null : results.get(0);
    }

    @Query("SELECT MAX(gr.score) FROM GameResult gr")
    Integer findMaxScore();

    @Query("SELECT AVG(gr.totalResponseTime) FROM GameResult gr")
    Double findAverageResponseTime();

    @Modifying
    @Transactional
    @Query("UPDATE GameResult gr SET gr.rankPosition = :rank WHERE gr.id = :resultId")
    void updateRankPosition(@Param("resultId") Long resultId, @Param("rank") int rank);

    List<GameResult> findByScoreGreaterThan(int minScore);

    List<GameResult> findByGameIdOrderByRankPositionAsc(Long gameId);

    @Query("SELECT COUNT(gr) FROM GameResult gr")
    long countTotalResults();
}
