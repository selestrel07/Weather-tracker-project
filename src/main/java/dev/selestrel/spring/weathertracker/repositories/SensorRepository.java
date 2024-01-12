package dev.selestrel.spring.weathertracker.repositories;

import dev.selestrel.spring.weathertracker.models.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, Integer> {
}
