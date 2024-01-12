package dev.selestrel.spring.weathertracker.repositories;

import dev.selestrel.spring.weathertracker.models.Measurement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeasurementRepository extends JpaRepository<Measurement, Integer> {

    List<Measurement> findAllByRainingIs(boolean raining);
}
