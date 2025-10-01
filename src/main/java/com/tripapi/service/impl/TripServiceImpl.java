package com.tripapi.service.impl;

import com.tripapi.dto.Trip.TripRequestDTO;
import com.tripapi.dto.Trip.TripResponseDTO;
import com.tripapi.model.Destination;
import com.tripapi.model.Trip;
import com.tripapi.model.User;
import com.tripapi.repository.DestinationRepository;
import com.tripapi.repository.TripRepository;
import com.tripapi.security.CurrentUser;
import com.tripapi.service.interfaces.TripService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TripServiceImpl implements TripService {

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private DestinationRepository destinationRepository;

    @Autowired
    private CurrentUser currentUser;

    @Override
    public List<TripResponseDTO> findAll() {
        User me = currentUser.require();
        return tripRepository.findByOwnerId(me.getId())
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public TripResponseDTO findById(Long id) {
        User me = currentUser.require();
        Trip t = tripRepository.findById(id)
                .filter(trip -> trip.getOwner() != null && trip.getOwner().getId().equals(me.getId())) // 👈
                .orElseThrow(() -> new DestinationServiceImpl.ResourceNotFoundException("Trip not found with ID: " + id));
        return toDTO(t);
    }

    @Override
    public TripResponseDTO create(TripRequestDTO dto) {
        User me = currentUser.require();

        Destination destRef = destinationRepository.findById(dto.getDestinationId())
                .orElseThrow(() -> new DestinationServiceImpl.ResourceNotFoundException("Destination not found with ID: " + dto.getDestinationId()));

        Trip entity = Trip.builder()
                .name(dto.getName())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .destination(destRef)
                .notes(dto.getNotes())
                .tripType(dto.getTripType())
                .owner(me)
                .build();

        return toDTO(tripRepository.save(entity));
    }


    @Override
    public TripResponseDTO update(Long id, TripRequestDTO dto) {
        User me = currentUser.require();

        Trip t = tripRepository.findById(id)
                .filter(trip -> trip.getOwner() != null && trip.getOwner().getId().equals(me.getId()))
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

    @Transactional
    @Override
    public void delete(Long id) {
        User me = currentUser.require();
        Trip trip = tripRepository.findById(id)
                .filter(t -> t.getOwner() != null && t.getOwner().getId().equals(me.getId()))
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found with ID: " + id));
        tripRepository.delete(trip);
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
