package com.tripapi.service.impl;

import com.tripapi.dto.Trip.TripRequestDTO;
import com.tripapi.dto.Trip.TripResponseDTO;
import com.tripapi.model.Destination;
import com.tripapi.model.Trip;
import com.tripapi.repository.DestinationRepository;
import com.tripapi.repository.TripRepository;
import com.tripapi.service.interfaces.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TripServiceImpl implements TripService {

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private DestinationRepository destinationRepository;

    @Override
    public List<TripResponseDTO> findAll() {
        return tripRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public TripResponseDTO findById(Long id) {
        Trip t = tripRepository.findById(id)
                .orElseThrow(() -> new DestinationServiceImpl.ResourceNotFoundException("Trip not found with ID: " + id));
        return toDTO(t);
    }

    @Override
    public TripResponseDTO create(TripRequestDTO dto) {
        Destination destRef = destinationRepository.findById(dto.getDestinationId())
                .orElseThrow(() -> new DestinationServiceImpl.ResourceNotFoundException("Destination not found with ID: " + dto.getDestinationId()));

        Trip entity = Trip.builder()
                .name(dto.getName())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .destination(destRef)
                .notes(dto.getNotes())
                .tripType(dto.getTripType())
                .build();

        return toDTO(tripRepository.save(entity));
    }


    @Override
    public TripResponseDTO update(Long id, TripRequestDTO dto) {
        Trip t = tripRepository.findById(id)
                .orElseThrow(() -> new DestinationServiceImpl.ResourceNotFoundException("Trip not found with ID: " + id));

        Destination destRef = destinationRepository.findById(dto.getDestinationId())
                .orElseThrow(() -> new DestinationServiceImpl.ResourceNotFoundException("Destination not found with ID: " + dto.getDestinationId()));

        t.setName(dto.getName());
        t.setStartDate(dto.getStartDate());
        t.setEndDate(dto.getEndDate());
        t.setDestination(destRef);
        t.setNotes(dto.getNotes());
        t.setTripType(dto.getTripType());

        return toDTO(tripRepository.save(t));
    }

    @Override
    public void delete(Long id) {
        if (!tripRepository.existsById(id)) {
            throw new DestinationServiceImpl.ResourceNotFoundException("Trip not found with ID: " + id);
        }
        tripRepository.deleteById(id);
    }

    // ---- mapping helper ----
    private TripResponseDTO toDTO(Trip t) {
        return TripResponseDTO.builder()
                .id(t.getId())
                .name(t.getName())
                .startDate(t.getStartDate())
                .endDate(t.getEndDate())
                .destinationId(t.getDestination().getId())
                .destinationCity(t.getDestination().getCity())
                .destinationCountry(t.getDestination().getCountry())
                .notes(t.getNotes())
                .tripType(t.getTripType())
                .build();
    }

    // Exception
    public static class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String message) { super(message); }
    }
}
