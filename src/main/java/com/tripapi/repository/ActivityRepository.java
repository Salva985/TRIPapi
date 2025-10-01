package com.tripapi.repository;

import com.tripapi.model.Activity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    @Query("""
    SELECT a FROM Activity a
    LEFT JOIN a.trip t
    WHERE t.owner.id = :ownerId
      AND (
        :q IS NULL
        OR LOWER(a.title) LIKE LOWER(CONCAT('%', :q, '%'))
        OR LOWER(CAST(a.type AS string)) LIKE LOWER(CONCAT('%', :q, '%'))
        OR LOWER(t.name) LIKE LOWER(CONCAT('%', :q, '%'))
            )
    """)

    Page<Activity> search(@Param("q") String q, @Param("ownerId") Long ownerId, Pageable pageable);

    @Query("SELECT a FROM Activity a WHERE a.id = :id AND a.trip.owner.id = :ownerId")
    Optional<Activity> findByIdAndOwner(@Param("id") Long id, @Param("ownerId") Long ownerId);

}
