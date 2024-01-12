package dev.selestrel.spring.weathertracker.util;

import dev.selestrel.spring.weathertracker.dto.SensorDTO;
import dev.selestrel.spring.weathertracker.models.Sensor;
import dev.selestrel.spring.weathertracker.services.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class SensorValidator implements Validator {

    private final SensorService sensorService;

    @Autowired
    public SensorValidator(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Sensor.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SensorDTO sensor = (SensorDTO) target;
        String sensorName = sensor.getName();

        if (sensorService.findByName(sensorName).isPresent()) {
            errors.rejectValue("name", "", "Sensor with name %s already exist".formatted(sensorName));
        }
    }
}
