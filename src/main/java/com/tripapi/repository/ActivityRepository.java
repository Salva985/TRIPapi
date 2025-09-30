package com.tripapi.repository;

import com.tripapi.model.Activity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    @Query("""
     SELECT a FROM Activity a
     WHERE (:q IS NULL OR
            LOWER(a.title) LIKE LOWER(CONCAT('%', :q, '%')) OR
            LOWER(COALESCE(a.notes, '')) LIKE LOWER(CONCAT('%', :q, '%')) OR
            LOWER(CAST(a.type as string)) LIKE LOWER(CONCAT('%', :q, '%'))
     )
  """)
    Page<Activity> search(@Param("q") String query, Pageable pageable);
}
