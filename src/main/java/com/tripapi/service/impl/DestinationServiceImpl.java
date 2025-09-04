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
    private DestinationRepository repository;

    @Override
    public DestinationResponseDTO create(DestinationRequestDTO request) {
        Destination entity = toEntity(request);
        Destination saved = repository.save(entity);
        return toDTO(saved);
    }

    @Override
    public DestinationResponseDTO getById(Long id) {
        Destination d = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Destination not found with id: " + id));
        return toDTO(d);
    }

    @Override
    public List<DestinationResponseDTO> list(String country, String search) {
        if (country != null && !country.isBlank()) {
            return repository.findByCountry(country.trim())
                    .stream().map(this::toDTO).toList();
        }
        if (search != null && !search.isBlank()) {
            return repository.searchByCityOrCountry(search.trim())
                    .stream().map(this::toDTO).toList();
        }
        return repository.findAll().stream()
                .sorted((a, b) -> a.getCity().compareToIgnoreCase(b.getCity()))
                .map(this::toDTO)
                .toList();
    }

    @Override
    public DestinationResponseDTO update(Long id, DestinationRequestDTO request) {
        Destination d = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Destination not found with id: " + id));

        d.setCity(request.getCity());
        d.setCountry(request.getCountry());
        d.setTimezone(request.getTimezone());
        d.setCurrencyCode(request.getCurrencyCode());

        Destination updated = repository.save(d);
        return toDTO(updated);
    }

    @Override
    public void delete(Long id) {
        Destination d = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Destination not found with id: " + id));
        repository.delete(d);
    }

    // ---------- mapping ----------
    private Destination toEntity(DestinationRequestDTO r) {
        return Destination.builder()
                .city(r.getCity())
                .country(r.getCountry())
                .timezone(r.getTimezone())
                .currencyCode(r.getCurrencyCode())
                .build();
    }

    private DestinationResponseDTO toDTO(Destination d) {
        return DestinationResponseDTO.builder()
                .id(d.getId())
                .city(d.getCity())
                .country(d.getCountry())
                .timezone(d.getTimezone())
                .currencyCode(d.getCurrencyCode())
                .build();
    }
}