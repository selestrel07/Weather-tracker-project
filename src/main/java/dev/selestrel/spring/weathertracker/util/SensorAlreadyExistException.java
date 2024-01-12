package dev.selestrel.spring.weathertracker.util;

public class SensorAlreadyExistException extends RuntimeException {
    public SensorAlreadyExistException(String msg) {
        super(msg);
    }
}
