package dev.selestrel.spring.weathertracker.controllers;

import dev.selestrel.spring.weathertracker.dto.MeasurementDTO;
import dev.selestrel.spring.weathertracker.models.Measurement;
import dev.selestrel.spring.weathertracker.models.Sensor;
import dev.selestrel.spring.weathertracker.services.MeasurementService;
import dev.selestrel.spring.weathertracker.services.SensorService;
import dev.selestrel.spring.weathertracker.util.MeasurementDataException;
import dev.selestrel.spring.weathertracker.util.SensorNotFoundException;
import dev.selestrel.spring.weathertracker.util.TrackerErrorResponse;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/measurements")
public class MeasurementController {

    private final MeasurementService measurementService;
    private final SensorService sensorService;
    private final ModelMapper modelMapper;

    @Autowired
    public MeasurementController(MeasurementService measurementService, SensorService sensorService, ModelMapper modelMapper) {
        this.measurementService = measurementService;
        this.sensorService = sensorService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public List<MeasurementDTO> getAll() {
        return measurementService.findAll().stream().map(this::convertToMeasurementDTO).collect(Collectors.toList());
    }

    @GetMapping("/rainyDaysCount")
    public int getRainyDaysCount() {
        return measurementService.findRainDaysCount();
    }

    @PostMapping("/add")
    public ResponseEntity<HttpStatus> add(@RequestBody @Valid MeasurementDTO measurementDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();

            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError err : errors) {
                errorMsg.append("%s - %s;".formatted(err.getField(), err.getDefaultMessage()));
            }

            throw new MeasurementDataException(errorMsg.toString());
        }

        Measurement measurement = convertToMeasurement(measurementDTO);
        String sensorName = measurement.getSensor().getName();
        Optional<Sensor> sensor = sensorService.findByName(sensorName);

        if (sensor.isEmpty()) {
            throw new SensorNotFoundException("Sensor with name %s wasn't registered".formatted(sensorName));
        }

        measurement.setSensor(sensor.get());
        measurementService.save(measurement);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    private Measurement convertToMeasurement(MeasurementDTO measurementDTO) {
        return modelMapper.map(measurementDTO, Measurement.class);
    }

    private MeasurementDTO convertToMeasurementDTO(Measurement measurement) {
        return modelMapper.map(measurement, MeasurementDTO.class);
    }

    @ExceptionHandler
    public ResponseEntity<TrackerErrorResponse> handleException(MeasurementDataException e) {
        TrackerErrorResponse response = new TrackerErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<TrackerErrorResponse> handleException(SensorNotFoundException e) {
        TrackerErrorResponse response = new TrackerErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
