package dev.selestrel.spring.weathertracker.util;

import java.time.LocalDateTime;

public class TrackerErrorResponse {

    private String message;
    private long timestamp;

    public TrackerErrorResponse(String message, long timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
