package dev.selestrel.spring.weathertracker.services;

import dev.selestrel.spring.weathertracker.models.Sensor;
import dev.selestrel.spring.weathertracker.repositories.SensorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SensorService {

    private final SensorRepository sensorRepository;

    @Autowired
    public SensorService(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    public void save(Sensor sensor) {
        sensorRepository.save(sensor);
    }
}
