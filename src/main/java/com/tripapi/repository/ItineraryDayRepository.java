package com.tripapi.repository;

import com.tripapi.model.ItineraryDay;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItineraryDayRepository extends JpaRepository<ItineraryDay, Long> {
}
