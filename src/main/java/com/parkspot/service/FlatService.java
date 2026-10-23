package com.parkspot.service;

import com.parkspot.entity.Flat;
import com.parkspot.exception.InvalidOperationException;
import com.parkspot.exception.ResourceNotFoundException;
import com.parkspot.repository.FlatRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service for managing flats (apartments) in the society.
 */
@Service
public class FlatService {

    private final FlatRepository flatRepository;

    public FlatService(FlatRepository flatRepository) {
        this.flatRepository = flatRepository;
    }

    @Transactional
    public Flat createFlat(Flat flat) {
        if (flatRepository.existsByFlatNumber(flat.getFlatNumber())) {
            throw new InvalidOperationException(
                    "Flat '" + flat.getFlatNumber() + "' already exists.");
        }
        return flatRepository.save(flat);
    }

    public List<Flat> getAllFlats() {
        return flatRepository.findAll();
    }

    public Flat getFlatByNumber(String flatNumber) {
        return flatRepository.findByFlatNumber(flatNumber)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Flat '" + flatNumber + "' not found."));
    }

    public Flat getFlatById(Long id) {
        return flatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Flat with ID " + id + " not found."));
    }

    @Transactional
    public Flat updateFlat(Long id, Flat updatedFlat) {
        Flat existing = getFlatById(id);
        existing.setOwnerName(updatedFlat.getOwnerName());
        existing.setContactNumber(updatedFlat.getContactNumber());
        return flatRepository.save(existing);
    }

    @Transactional
    public void deleteFlat(Long id) {
        Flat flat = getFlatById(id);
        flatRepository.delete(flat);
    }
}
