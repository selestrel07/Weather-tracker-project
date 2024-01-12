package dev.selestrel.spring.weathertracker.services;

import dev.selestrel.spring.weathertracker.models.Measurement;
import dev.selestrel.spring.weathertracker.repositories.MeasurementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MeasurementService {

    private final MeasurementRepository measurementRepository;

    @Autowired
    public MeasurementService(MeasurementRepository measurementRepository) {
        this.measurementRepository = measurementRepository;
    }

    public List<Measurement> findAll() {
        return measurementRepository.findAll();
    }

    public int findRainDaysCount() {
        return measurementRepository.findAllByRainingIs(true).size();
    }

    public void save(Measurement measurement) {
        measurementRepository.save(measurement);
    }
}
