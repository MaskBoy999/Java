package com.example.repository;

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

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long>, JpaSpecificationExecutor<Player> {

    Optional<Player> findByNameIgnoreCase(String name);

    List<Player> findAllByOrderByCorrectAnswersDescTotalResponseTimeAsc();

    @Query("SELECT p FROM Player p ORDER BY p.correctAnswers DESC, p.totalResponseTime ASC")
    List<Player> findTopByOrderByCorrectAnswersDescTotalResponseTimeAsc(Pageable pageable);

    @Query("SELECT p FROM Player p WHERE p.correctAnswers > :minAnswers ORDER BY p.correctAnswers DESC")
    List<Player> findPlayersWithMinCorrectAnswers(@Param("minAnswers") int minAnswers);

    @Query("SELECT p FROM Player p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :pattern, '%'))")
    List<Player> findByNameContaining(@Param("pattern") String pattern);

    @Modifying
    @Transactional
    @Query("UPDATE Player p SET p.totalResponseTime = :time WHERE p.id = :playerId")
    void updateTotalResponseTime(@Param("playerId") Long playerId, @Param("time") long time);

    @Modifying
    @Transactional
    @Query("UPDATE Player p SET p.gamesPlayed = p.gamesPlayed + 1 WHERE p.id = :playerId")
    void incrementGamesPlayed(@Param("playerId") Long playerId);

    Page<Player> findAll(Pageable pageable);

    @Query("SELECT COUNT(p) FROM Player p WHERE p.correctAnswers > 0")
    long countPlayersWithCorrectAnswers();

    List<Player> findByGamesPlayedGreaterThan(int minGames);
}
