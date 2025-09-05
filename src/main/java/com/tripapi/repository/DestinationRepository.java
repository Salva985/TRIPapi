package com.tripapi.repository;

import com.tripapi.model.Destination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DestinationRepository extends JpaRepository<Destination, Long> {
}
 /*   @Query("""
           SELECT d FROM Destination d
           WHERE LOWER(d.country) = LOWER(:country)
           ORDER BY d.city ASC
           """)
    List<Destination> findByCountry(@Param("country") String country);

    @Query("""
           SELECT d FROM Destination d
           WHERE LOWER(d.city)    LIKE LOWER(CONCAT('%', :search, '%'))
              OR LOWER(d.country) LIKE LOWER(CONCAT('%', :search, '%'))
           ORDER BY d.city ASC
           """)
    List<Destination> searchByCityOrCountry(@Param("search") String search);/*
