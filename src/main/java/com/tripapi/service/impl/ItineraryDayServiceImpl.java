package com.tripapi.service.impl;

import com.tripapi.dto.ItineraryDay.ItineraryDayRequestDTO;
import com.tripapi.dto.ItineraryDay.ItineraryDayResponseDTO;
import com.tripapi.model.ItineraryDay;
import com.tripapi.model.Trip;
import com.tripapi.repository.ItineraryDayRepository;
import com.tripapi.repository.TripRepository;
import com.tripapi.service.interfaces.ItineraryDayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItineraryDayServiceImpl implements ItineraryDayService {

    @Autowired
    private ItineraryDayRepository itineraryDayRepository;

    @Autowired
    private TripRepository tripRepository;

    @Override
    public List<ItineraryDayResponseDTO> findAll() {
        return itineraryDayRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public ItineraryDayResponseDTO findById(Long id) {
        ItineraryDay day = itineraryDayRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Itinerary day not found with ID: " + id));
        return toDTO(day);
    }

    @Override
    public ItineraryDayResponseDTO create(ItineraryDayRequestDTO dto) {
        Trip tripRef = tripRepository.findById(dto.getTripId())
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found with ID: " + dto.getTripId()));

        ItineraryDay entity = ItineraryDay.builder()
                .trip(tripRef)
                .date(dto.getDate())
                .dayNumber(dto.getDayNumber())
                .title(dto.getTitle())
                .notes(dto.getNotes())
                .build();

        ItineraryDay saved = itineraryDayRepository.save(entity);
        return toDTO(saved);
    }

    @Override
    public ItineraryDayResponseDTO update(Long id, ItineraryDayRequestDTO dto) {
        ItineraryDay day = itineraryDayRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Itinerary day not found with ID: " + id));

        Trip tripRef = tripRepository.findById(dto.getTripId())
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found with ID: " + dto.getTripId()));

        day.setTrip(tripRef);
        day.setDate(dto.getDate());
        day.setDayNumber(dto.getDayNumber());
        day.setTitle(dto.getTitle());
        day.setNotes(dto.getNotes());

        ItineraryDay updated = itineraryDayRepository.save(day);
        return toDTO(updated);
    }

    @Override
    public void delete(Long id) {
        if (!itineraryDayRepository.existsById(id)) {
            throw new ResourceNotFoundException("Itinerary day not found with ID: " + id);
        }
        itineraryDayRepository.deleteById(id);
    }

    // ---- mapping helper ----
    private ItineraryDayResponseDTO toDTO(ItineraryDay d) {
        return ItineraryDayResponseDTO.builder()
                .id(d.getId())
                .tripId(d.getTrip().getId())
                .tripName(d.getTrip().getName())
                .date(d.getDate())
                .dayNumber(d.getDayNumber())
                .title(d.getTitle())
                .notes(d.getNotes())
                .build();
    }

    // Inline exception (MarineConservation style)
    public static class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String message) { super(message); }
    }
}