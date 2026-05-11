package com.example.repository;

import com.example.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findAllByOrderByIdAsc();

    Page<Question> findAll(Pageable pageable);

    @Query("SELECT q FROM Question q WHERE LOWER(q.text) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Question> findByTextContaining(@Param("keyword") String keyword);

    long count();

    List<Question> findAllByOrderByIdDesc();
}
