package com.tripapi.service.impl;

import com.tripapi.dto.Destination.DestinationRequestDTO;
import com.tripapi.dto.Destination.DestinationResponseDTO;
import com.tripapi.model.Destination;
import com.tripapi.repository.DestinationRepository;
import com.tripapi.service.interfaces.DestinationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DestinationServiceImpl implements DestinationService {

    @Autowired
    private DestinationRepository destinationRepository;

    @Override
    public List<DestinationResponseDTO> findAll() {
        return destinationRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public DestinationResponseDTO findById(Long id) {
        Destination d = destinationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Destination not found with ID: " + id));
        return toDTO(d);
    }

    @Override
    public DestinationResponseDTO create(DestinationRequestDTO dto) {
        Destination entity = Destination.builder()
                .city(dto.getCity())
                .country(dto.getCountry())
                .timezone(dto.getTimezone())
                .currencyCode(dto.getCurrencyCode())
                .build();
        return toDTO(destinationRepository.save(entity));
    }

    @Override
    public DestinationResponseDTO update(Long id, DestinationRequestDTO dto) {
        Destination d = destinationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Destination not found with ID: " + id));

        d.setCity(dto.getCity());
        d.setCountry(dto.getCountry());
        d.setTimezone(dto.getTimezone());
        d.setCurrencyCode(dto.getCurrencyCode());

        return toDTO(destinationRepository.save(d));
    }

    @Override
    public void delete(Long id) {
        if (!destinationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Destination not found with ID: " + id);
        }
        destinationRepository.deleteById(id);
    }

    // ---- mapping helper ----
    private DestinationResponseDTO toDTO(Destination d) {
        return DestinationResponseDTO.builder()
                .id(d.getId())
                .city(d.getCity())
                .country(d.getCountry())
                .timezone(d.getTimezone())
                .currencyCode(d.getCurrencyCode())
                .build();
    }

    // Exception
    public static class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String message) { super(message); }
    }
}