package dev.selestrel.spring.weathertracker.controllers;

import dev.selestrel.spring.weathertracker.dto.SensorDTO;
import dev.selestrel.spring.weathertracker.models.Sensor;
import dev.selestrel.spring.weathertracker.services.SensorService;
import dev.selestrel.spring.weathertracker.util.SensorAlreadyExistException;
import dev.selestrel.spring.weathertracker.util.SensorValidator;
import dev.selestrel.spring.weathertracker.util.TrackerErrorResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sensors")
public class SensorController {

    private final SensorService sensorService;
    private final ModelMapper modelMapper;
    private final SensorValidator sensorValidator;

    @Autowired
    public SensorController(SensorService sensorService, ModelMapper modelMapper, SensorValidator sensorValidator) {
        this.sensorService = sensorService;
        this.modelMapper = modelMapper;
        this.sensorValidator = sensorValidator;
    }

    @PostMapping("/registration")
    public ResponseEntity<HttpStatus> registration(@RequestBody SensorDTO sensorDTO, BindingResult bindingResult) {
        sensorValidator.validate(sensorDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            throw new SensorAlreadyExistException(bindingResult.getFieldError("name").getDefaultMessage());
        }

        sensorService.save(convertToSensor(sensorDTO));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    public ResponseEntity<TrackerErrorResponse> handleException(SensorAlreadyExistException e) {
        TrackerErrorResponse response = new TrackerErrorResponse(e.getMessage(), System.currentTimeMillis());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private Sensor convertToSensor(SensorDTO sensorDTO) {
        return modelMapper.map(sensorDTO, Sensor.class);
    }
}
