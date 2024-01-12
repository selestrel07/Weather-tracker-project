package dev.selestrel.spring.weathertracker.util;

public class SensorNotFoundException extends RuntimeException {
    public SensorNotFoundException(String msg) {
        super(msg);
    }
}
