package com.cloop.cloop.looks.repository;

import com.cloop.cloop.looks.domain.Look;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface LookRepository extends JpaRepository<Look, Long> {

    // 날짜별 Look 조회
    // Look -> LookCloth -> Cloth 즉시 로드
    @EntityGraph(attributePaths = {"lookClothList.cloth"})
    @Query("SELECT l FROM Look l WHERE l.createdAt = :date")
    List<Look> findAllByCreatedAt(@Param("date") LocalDate date);


}
