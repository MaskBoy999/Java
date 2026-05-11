package com.example.repository;

import com.example.entity.Game;
import com.example.entity.GameStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game, Long>, JpaSpecificationExecutor<Game> {

    List<Game> findByActiveTrueOrderByCreatedAtDesc();

    List<Game> findByStatus(GameStatus status);

    Page<Game> findByActiveTrue(Pageable pageable);

    @Query("SELECT g FROM Game g WHERE g.status = 'FINISHED' AND g.endedAt BETWEEN :start AND :end ORDER BY g.endedAt DESC")
    List<Game> findCompletedGamesBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COUNT(g) FROM Game g WHERE g.status = :status AND g.active = true")
    long countActiveGamesByStatus(@Param("status") GameStatus status);

    @Query("SELECT g FROM Game g WHERE g.active = true ORDER BY g.startedAt DESC")
    List<Game> findActiveGamesJPQL();

    @Query("SELECT g FROM Game g WHERE g.currentPlayers >= g.maxPlayers AND g.active = true")
    List<Game> findFullGames();

    List<Game> findByCurrentPlayersGreaterThan(int minPlayers);
}
